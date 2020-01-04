/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import lombok.Getter;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutAnimation;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.multipliers.MultiplierManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutoMinerAI {
    /**
     * Tool for the auto miner to be holding
     */
    private static ItemStack tool = new ItemFactory(Material.DIAMOND_PICKAXE)
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
            .create();
    /**
     * <code>true</code> if the auto miner should run
     */
    public boolean run = true;
    /**
     * Currently loaded auto miner data
     */
    public AutoMinerData data;
    /**
     * Current state of the auto miner
     */
    @Getter
    private State state = new State(MinerState.WAITING, ThoughtState.NO_TARGET, ScanState.NARROW_SCAN);
    @Getter
    private State previousState = state;
    /**
     * How far the auto miner will mine
     */
    @Getter
    private int armReach = 3;
    /**
     * Maximum scan distance for the auto miner
     */
    @Getter
    private int wideScanRadius = 26;
    /**
     * Current block target
     * <code>null</code> if no target is selected
     */
    private Block target;
    /**
     * Time of last auto miner update
     */
    @Getter
    private long spawnTime;
    /**
     * Owner of the auto miner
     */
    @Getter
    private Player player;
    /**
     * Block breaker to use for scanning
     */
    @Getter
    private BlockBreaker breaker;
    /**
     * NPC for the auto miner
     */
    @Getter
    private NPC npc;
    private long lastBlockBreak = -1;

    /**
     * Create a new auto miner AI for an npc
     *
     * @param npc NPC
     */
    AutoMinerAI(NPC npc) {
        Debugger.log("Creating autominer AI.", "autominer");
        assert npc != null;
        this.npc = npc;
        if (!this.npc.hasTrait(Equipment.class)) {
            Debugger.log("Adding equipment trait.", "autominer");
            this.npc.addTrait(new Equipment());
        }
        Debugger.log("Adding tool.", "autominer");
        this.npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, tool);
        Debugger.log("Getting player.", "autominer");
        Optional<Map.Entry<Player, NPC>> optional = AutominerCore.getCore().npcs.entrySet()
                .stream()
                .filter((entry) -> entry.getValue() == npc)
                .findFirst();
        if (!optional.isPresent()) {
            npc.destroy();
            this.run = false;
            return;
        }
        player = optional.get().getKey();
    }

    /**
     * Get the head location of an entity
     *
     * @param ent Entity
     * @return Head Location
     */
    public static Location getHead(@NotNull Entity ent) {
        return ent.getLocation().add(0, ent.getHeight(), 0);
    }

    String getSkin() {
        return data.getSkin();
    }

    public void setSkin(String str) {
        data.setSkin(str);
    }

    /**
     * Run all necessary functions to spawn the auto miner
     */
    void onSpawn() {
        this.spawnTime = DateUtils.getMilliTimeStamp();
        this.breaker = new DefaultBlockBreaker(this);
        lastBlockBreak = DateUtils.getMilliTimeStamp();
    }

    /**
     * Run the main auto miner algorithm
     * and perform an appropriate action
     */
    public void run() {
        if (run && npc.isSpawned()) {
            Debugger.log("Running autominer...", "autominer");
            if ((data.getAutominerTime().compareTo(BigInteger.valueOf(DateUtils.getMilliTimeStamp() - spawnTime)) < 0)) {
                data.setAutominerTime(BigInteger.ZERO);
                saveConfig();
                player.sendMessage(Messages.AUTOMINER_RAN_OUT_OF_TIME.get());
                npc.despawn();
            } else {
                removeAutominerTime(DateUtils.getMilliTimeStamp() - spawnTime);
                BaseComponent[] actionbar = TextComponent.fromLegacyText("§eAuto Miner: §b" + DateUtils.convertTime(getAutominerTime().divide(BigInteger.valueOf(1000))));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                spawnTime = DateUtils.getMilliTimeStamp();
            }

            Debugger.log("Current state: " + state.toString(), "autominer");
            switch (state.getMinerState()) {
                case WAITING:
                    Debugger.log("Autominer is waiting, checking what to do", "autominer");
                    if (state.getThoughtState().equals(ThoughtState.NO_TARGET)) {
                        Debugger.log("Finding target...", "autominer");
                        if (state.getScanState().equals(ScanState.WIDE_SCAN)) {
                            List<Block> wideScan = breaker.wideScan();
                            if (!wideScan.isEmpty()) {
                                target = getClosest(wideScan);
                                assert target != null;
                                if (target.getLocation().distance(npc.getEntity().getLocation()) >= armReach) {
                                    npc.getNavigator().setTarget(target.getLocation());
                                    updateState(new State(MinerState.WALKING, ThoughtState.FAR_TARGET, ScanState.WIDE_SCAN));
                                } else {
                                    updateState(new State(MinerState.WALKING, ThoughtState.CLOSE_TARGET, ScanState.NARROW_SCAN));
                                }
                            } else {
                                this.state.setMinerState(MinerState.STUCK);
                            }
                        } else {
                            Block candidate = getClosest(breaker.narrowScan());
                            if (candidate == null) {
                                updateState(new State(MinerState.WAITING, ThoughtState.NO_TARGET, ScanState.WIDE_SCAN));
                            } else {
                                target = candidate;
                                updateState(new State(MinerState.WALKING, ThoughtState.CLOSE_TARGET, ScanState.NARROW_SCAN));
                            }
                        }
                    }
                    break;
                case WALKING:
                    Debugger.log("Walking...", "autominer");
                    if (target == null) {
                        updateState(new State(MinerState.WAITING, ThoughtState.NO_TARGET, ScanState.NARROW_SCAN));
                        return;
                    }
                    if (target.getLocation().distance(getHead(npc.getEntity())) >= armReach) {
                        Debugger.log("Distance far, navigating...", "autominer");
                        if (!npc.getNavigator().isNavigating()) {
                            if (npc.getEntity().isOnGround()) {
                                Debugger.log("Setting navigator target...", "autominer");
                                if (npc.isSpawned()) {
                                    if (DateUtils.getMilliTimeStamp() - lastBlockBreak > 5000) {
                                        npc.despawn();
                                        npc.spawn(player.getLocation());
                                    }
                                    Debugger.log("Setting navigator target...", "autominer");
                                    Navigator nav = npc.getNavigator();
                                    if (nav == null) {
                                        Debugger.error("NPC has no navigator", "autominer");
                                        return;
                                    }
                                    nav.setPaused(false);
                                    if (target == null) {
                                        Debugger.error("NPC has no target", "autominer");
                                        return;
                                    }
                                    nav.setTarget(target.getLocation());
                                }
                            }
                        } else {
                            Debugger.log("Stuck.", "autominer");
                            updateState(new State(MinerState.STUCK, ThoughtState.NO_TARGET, ScanState.WIDE_SCAN));
                        }
                        // clearBlocked();
                    } else {
                        updateState(new State(MinerState.MINING, ThoughtState.CLOSE_TARGET, ScanState.NARROW_SCAN));
                    }
                    break;
                case MINING:
                    breakBlock(target);
                    target = null;
                    lastBlockBreak = DateUtils.getMilliTimeStamp();
                    updateState(new State(MinerState.WAITING, ThoughtState.NO_TARGET, ScanState.NARROW_SCAN));
                    break;
                case STUCK:
                    List<Block> scan = breaker.wideScan();
                    if (scan.isEmpty()) {
                        player.sendMessage("§cYour autominer is stuck, despawning it...");
                        npc.despawn();
                    } else {
                        scan = scan.stream().filter((it) -> it != target).collect(Collectors.toList());
                        target = getClosest(scan);
                        updateState(new State(MinerState.WALKING, ThoughtState.FAR_TARGET, ScanState.NARROW_SCAN));
                    }
            }
        }
    }

    /**
     * Update the current state of the auto miner
     *
     * @param state New state
     */
    private void updateState(State state) {
        previousState = this.state;
        this.state = state;
    }

    private void updateState(MinerState state) {
        previousState = this.state;
        ThoughtState ts = previousState.getThoughtState();
        ScanState ss = previousState.getScanState();
        this.state = new State(state, ts, ss);
    }

    private void updateState(ThoughtState state) {
        previousState = this.state;
        MinerState ms = previousState.getMinerState();
        ScanState ss = previousState.getScanState();
        this.state = new State(ms, state, ss);
    }

    /*
    /**
     * Clear the area around the auto miner to avoid getting blocked in
     *
    private void clearBlocked() {
        for (int i = -1; i <= 1; i ++) {
            for (int j = -1; j <= 1; j ++) {
                for (int k = -1; k <= 1; k ++) {
                    Block block = npc.getEntity().getLocation().getBlock().getRelative(i, j, k);
                    if (MineManager.isInMine(block) && !block.getType().equals(Material.AIR)) {
                        breakBlock(block);
                    }
                }
            }
        }
    }*/

    private void updateState(ScanState state) {
        previousState = this.state;
        MinerState ms = previousState.getMinerState();
        ThoughtState ts = previousState.getThoughtState();
        this.state = new State(ms, ts, state);
    }

    /**
     * Get the nearest block to the NPC
     *
     * @param blocks List of blocks to check
     * @return Closest block from list
     */
    @Nullable
    private Block getClosest(@NotNull List<Block> blocks) {
        if (blocks.isEmpty()) {
            return null;
        }
        Location loc = npc.getEntity().getLocation();
        Block best = blocks.get(0);
        for (Block block : blocks) {
            if (block.getLocation().distance(loc) < best.getLocation().distance(loc)) {
                best = block;
            }
        }
        return best;
    }

    /**
     * Simulate the NPC breaking a block
     *
     * @param block Block to break
     */
    private void breakBlock(@NotNull Block block) {
        npc.faceLocation(block.getLocation().add(0.5, 0.5, 0.5));
        Packet packet = new PacketPlayOutAnimation(((CraftPlayer) npc.getEntity()).getHandle(), 0);
        npc.getEntity().getNearbyEntities(15.0, 15.0, 15.0)
                .stream()
                .filter(ent -> ent instanceof Player)
                .map(ent -> ((CraftPlayer) ent).getHandle().playerConnection)
                .forEach(con -> con.sendPacket(packet));
        doRewards(block.getType());
        npc.getEntity().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType().getId());
        block.setType(Material.AIR);
    }

    public void skip(int seconds) {
        Integer tokens = Main.getSellPrice(Material.STONE);
        if (tokens == null) {
            tokens = 0;
        }
        double multi = MultiplierManager.getMultiplier(player, Multiplier.MultiplierType.AM_TOKEN);
        if (multi == 0.0) {
            multi = 1.0;
        }
        multi *= (1.0 + 0.01 * getUpgrade(AutoMinerUpgrade.MORE_TOKENS));
        addTokens(MathUtils.roundRand(tokens * multi * seconds));
        double keyChance = 0.01 + 0.0005 * getUpgrade(AutoMinerUpgrade.MORE_KEYS);
        int keys = MathUtils.roundRand(keyChance * seconds);
        if (keys > 0) {
            int mineKeys = keys;
            int rareKeys = 0;
            int legendaryKeys = 0;
            int level = getUpgrade(AutoMinerUpgrade.BETTER_KEYS);
            double chanceUpMine = Math.floorDiv(level, 20) + 5;
            double chanceUpRare = Math.floorDiv(level, 50) - 2;
            int deltaMine = Math.min(
                    MathUtils.roundRand(chanceUpMine * seconds),
                    mineKeys
            );
            mineKeys -= deltaMine;
            rareKeys += deltaMine;
            int deltaRare = Math.min(
                    MathUtils.roundRand(chanceUpRare * seconds),
                    rareKeys
            );
            rareKeys -= deltaRare;
            legendaryKeys += deltaRare;
            if (mineKeys > 0) {
                addKeys("mine", mineKeys);
            }
            if (rareKeys > 0) {
                addKeys("rare", rareKeys);
            }
            if (legendaryKeys > 0) {
                addKeys("legendary", legendaryKeys);
            }
        }
        double dustChance = 0.01 + 0.0005 * getUpgrade(AutoMinerUpgrade.MORE_DUST);
        addDust(MathUtils.roundRand(dustChance * seconds));
    }

    private void doRewards(Material mat) {
        Integer tokens = Main.getSellPrice(mat);
        if (tokens == null) {
            tokens = 0;
        }
        double multi = MultiplierManager.getMultiplier(player, Multiplier.MultiplierType.AM_TOKEN);
        if (multi == 0.0) {
            multi = 1.0;
        }
        multi *= (1.0 + 0.01 * getUpgrade(AutoMinerUpgrade.MORE_TOKENS));
        addTokens(MathUtils.roundRand(tokens * multi));
        double keyChance = 1.0 + 0.05 * getUpgrade(AutoMinerUpgrade.MORE_KEYS);
        boolean key = MathUtils.isRandom(keyChance, 100.0);
        if (key) {
            String keyType = "mine";
            int level = getUpgrade(AutoMinerUpgrade.BETTER_KEYS);
            double chanceUpMine = Math.floorDiv(level, 20) + 5;
            double chanceUpRare = Math.floorDiv(level, 50) - 2;
            if (MathUtils.isRandom(chanceUpMine, 100.0)) {
                if (MathUtils.isRandom(chanceUpRare, 100.0)) {
                    keyType = "legendary";
                } else {
                    keyType = "rare";
                }
            }
            addKeys(keyType, 1);
        }
        double dustChance = 1.0 + 0.05 * getUpgrade(AutoMinerUpgrade.MORE_DUST);
        if (MathUtils.isRandom(dustChance, 100.0)) {
            addDust(1);
        }
    }

    private long getTokens() {
        return data.getRewards().getTokens();
    }

    private void setTokens(long amount) {
        data.getRewards().setTokens(amount);
    }

    private void addTokens(long amount) {
        setTokens(getTokens() + amount);
    }

    private void removeTokens(long amount) {
        addTokens(-amount);
    }

    private long getKeys(@NotNull String name) {
        return data.getRewards().getKeys().getOrDefault(name.toLowerCase(), 0L);
    }

    private void setKeys(@NotNull String name, long amount) {
        data.getRewards().getKeys().put(name.toLowerCase(), amount);
    }

    private void addKeys(String name, int amount) {
        setKeys(name, getKeys(name) + amount);
    }

    private void removeKeys(String name, int amount) {
        addKeys(name, -amount);
    }

    private long getDust() {
        return data.getRewards().getSocketGemDust();
    }

    private void setDust(long amount) {
        data.getRewards().setSocketGemDust(amount);
    }

    private void addDust(long amount) {
        setDust(getDust() + amount);
    }

    private void removeDust(long amount) {
        addDust(-amount);
    }

    /**
     * Reset the Auto Miner AI
     */
    void reset() {
        state = new State(MinerState.WAITING, ThoughtState.NO_TARGET, ScanState.NARROW_SCAN);
        previousState = state;
        target = null;
        run = true;
    }

    /**
     * Load the autominer data from the player
     */
    void loadConfig() {
        this.data = PlayerData.getAutominerData(player);
    }

    /**
     * Save the current autominer data
     */
    void saveConfig() {
        PlayerData.setAutominerData(player, this.data);
    }

    /**
     * Get the player's current auto miner time
     *
     * @return Auto miner time
     */
    public BigInteger getAutominerTime() {
        return data.getAutominerTime();
    }

    /**
     * Set the player's auto miner time
     *
     * @param time Auto miner time
     */
    private void setAutominerTime(BigInteger time) {
        data.setAutominerTime(time);
        saveConfig();
    }

    /**
     * Add auto miner time to the player
     *
     * @param time Time to add
     */
    public void addAutominerTime(BigInteger time) {
        setAutominerTime(getAutominerTime().add(time));
    }
    public void addAutominerTime(long time) {
        addAutominerTime(BigInteger.valueOf(time));
    }

    /**
     * Remove auto miner time from the player
     *
     * @param time Time in millis
     */
    private void removeAutominerTime(BigInteger time) {
        addAutominerTime(time.negate());
    }
    private void removeAutominerTime(long time) {
        removeAutominerTime(BigInteger.valueOf(time));
    }

    /**
     * Get the current level of an upgrade
     *
     * @param upgrade Upgrade
     * @return Level of the upgrade
     */
    public int getUpgrade(AutoMinerUpgrade upgrade) {
        return data.getUpgrades().getOrDefault(upgrade, 0);
    }

    /**
     * Set the level of an upgrade
     *
     * @param upgrade Upgrade
     * @param value   Level of upgrade
     */
    public void setUpgrade(AutoMinerUpgrade upgrade, int value) {
        data.getUpgrades().put(upgrade, value);
        saveConfig();
    }

    /**
     * Add levels to an upgrade
     *
     * @param upgrade Upgrade
     * @param amount  Levels to add
     */
    public void addUpgrade(AutoMinerUpgrade upgrade, int amount) {
        setUpgrade(upgrade, getUpgrade(upgrade) + amount);
    }

    /**
     * Remove levels from an upgrade
     *
     * @param upgrade Upgrade
     * @param amount  Levels to remove
     */
    public void removeUpgrade(AutoMinerUpgrade upgrade, int amount) {
        addUpgrade(upgrade, -amount);
    }
}
