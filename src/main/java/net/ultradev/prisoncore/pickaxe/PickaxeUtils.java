/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.enchants.EnchantUtils;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pets.PetXp;
import net.ultradev.prisoncore.pickaxe.breakblock.BlockBreaker;
import net.ultradev.prisoncore.pickaxe.breakblock.BreakBlockParameters;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.RewardApplicator;
import net.ultradev.prisoncore.rewards.rewards.KeyReward;
import net.ultradev.prisoncore.selling.AutoSell;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.*;

public class PickaxeUtils {
    public static int[] socketRequiredLevels = {0, 0, 0, 10, 20, 25, 30, 40};
    public static Map<UUID, EnchantmentResult> applicators = new HashMap<>();
    private static HashMap<Enchantment, Integer> defaultEnchants = new HashMap<>();

    static {
        defaultEnchants.put(Enchantment.DIG_SPEED, 30);
        defaultEnchants.put(Enchantment.DURABILITY, 10);
        defaultEnchants.put(CustomEnchant.LUCKY, 1);
    }

    public static boolean isPickaxe(ItemStack item) {
        return (Objects.equals(NBTUtils.getString(item, "type"), "pickaxe"));
    }

    public static ItemStack createDefaultPickaxe() {
        ItemStack pickaxe = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aStarter Pickaxe")
                .setEnchantments(defaultEnchants)
                .addNBT("type", "pickaxe")
                .addNBT("xp", BigInteger.ZERO)
                .addNBT("level", 1).create();
        return updatePickaxe(pickaxe);
    }

    public static ItemStack updatePickaxe(ItemStack item) {
        try {
            Map<Enchantment, Integer> enchants = item.getEnchantments();
            BigInteger xp = NBTUtils.getBigInteger(item, "xp");
            int level = NBTUtils.getInt(item, "level");

            List<SocketGem> gems = new ArrayList<>();
            for (int i = 0; i < getMaxSocketGems(item); i++) {
                gems.add(getSocketGem(item, i));
            }

            List<String> lore = new ArrayList<>();

            for (Enchantment enchant : enchants.keySet()) {
                lore.add(EnchantInfo.getEnchantName(enchant) + " §7" + enchants.get(enchant));
            }
            String PICKAXE_SEPERATOR = "§7§m-----------------------------";
            lore.add(PICKAXE_SEPERATOR);
            lore.addAll(PickaxeLore.generateSocketLines(gems));
            lore.add(PICKAXE_SEPERATOR);
            lore.addAll(PickaxeLore.generateLevelLines(xp, level));

            ItemStack ret = ItemUtils.setLore(item, lore);
            ItemMeta meta = ret.getItemMeta();
            if (enchants.containsKey(CustomEnchant.UNBREAKABLE) && !meta.isUnbreakable()) {
                meta.setUnbreakable(true);
            }
            if (meta.isUnbreakable() && !enchants.containsKey(CustomEnchant.UNBREAKABLE)) {
                meta.setUnbreakable(false);
            }
            ret.setItemMeta(meta);
            return ret;
        } catch (Exception ignored) {
            return item;
        }
    }

    public static ItemStack getPickaxe(@NotNull Player player) {
        ItemStack item = player.getInventory().getItem(0);
        if (item == null) {
            return PickaxeUtils.createDefaultPickaxe();
        }
        return item;
    }

    private static ItemStack addXp(Player player, int amount) {
        ItemStack pickaxe = getPickaxe(player);

        BigInteger xp;
        try {
            xp = NBTUtils.getBigInteger(pickaxe, "xp");
        } catch (Exception e) {
            xp = BigInteger.valueOf(NBTUtils.getInt(pickaxe, "xp"));
            pickaxe = NBTUtils.remove(pickaxe, "xp");
        }
        Debugger.log("Current XP: " + xp, "xp");
        int level = NBTUtils.getInt(pickaxe, "level");
        BigInteger maxxp = PickaxeLore.getXpRequired(level);
        xp = xp.add(BigInteger.valueOf(amount));
        while (xp.compareTo(maxxp) >= 0) {
            xp = xp.subtract(maxxp);
            level++;
            player.sendTitle("§6⬆ §lLEVEL UP!§r §6⬆",
                    "§7[§6§lLevel " + (level - 1) + "§7] §b❱§c❱§a❱ §7[§6§lLevel " + level + "§7]", 10, 40, 10);
            maxxp = PickaxeLore.getXpRequired(level);
        }
        player.setLevel(level);
        PlayerData.setPickaxeLevel(player, level);
        player.setExp((float) (PickaxeLore.getPercent(xp, maxxp) / 100.0));
        ItemStack ret = NBTUtils.setBigInteger(pickaxe, "xp", xp);
        ret = NBTUtils.setInt(ret, "level", level);
        return ret;
    }

    public static boolean breakBlock(Player player, Block block, boolean brokenByPlayer) {
        ItemStack pickaxe = getPickaxe(player);
        EnchantmentResult res = breakBlockAsync(
                pickaxe.getEnchantments(),
                getSockets(pickaxe),
                block,
                brokenByPlayer,
                player.hasPermission("ultraprison.donator"),
                PlayerData.getSetting(player, "key_message"),
                PlayerData.getSetting(player, "lb_message"),
                PetManager.getPets(player)
        );
        return applyReward(player, res);
    }

    public static boolean applyReward(Player player, @NotNull EnchantmentResult res) {
        ItemStack pickaxe = addXp(player, res.getPickaxeXP());
        pickaxe = updatePickaxe(pickaxe);
        player.getInventory().setItem(0, pickaxe);
        for (RewardApplicator rewapp : res.getRewards()) {
            rewapp.applyReward(player);
        }
        for (PlayerRunnable run : res.getRunnables()) {
            if (run == null) {
                continue;
            }
            run.run(player);
        }
        PlayerData.addBlocksMined(player, res.getBlocksMined());
        PetXp.addXp(player, PetXp.XpType.MINING, res.getBlocksMined().intValue());
        return res.getCancelled();
    }

    @NotNull
    private static EnchantmentResult breakBlockAsync(Map<Enchantment, Integer> enchants, Map<SocketGemType, Double> sockets, @NotNull Block block, boolean brokenByPlayer, boolean canBreakSLBlocks, boolean key_message, boolean lb_message, Map<PetType, Integer> pets) {
        Material mat = block.getType();
        Location loc = block.getLocation();
        return breakBlockAsync(
                enchants,
                sockets,
                loc,
                mat,
                block.getData(),
                brokenByPlayer,
                canBreakSLBlocks,
                key_message,
                lb_message,
                pets
        );
    }

    @NotNull
    public static EnchantmentResult breakBlockAsync(Map<Enchantment, Integer> enchants, Map<SocketGemType, Double> sockets, Location loc, Material mat, byte data, boolean brokenByPlayer, boolean canBreakSLBlocks, boolean key_message, boolean lb_message, Map<PetType, Integer> pets) {
        return breakBlockAsync(new BreakBlockParameters(enchants, sockets, loc, mat, data, brokenByPlayer ? BlockBreaker.PLAYER : BlockBreaker.ENCHANT, canBreakSLBlocks, key_message, lb_message, pets));
    }

    @NotNull
    public static EnchantmentResult breakBlockAsync(BreakBlockParameters params) {
        Map<Enchantment, Integer> enchants = params.getEnchants();
        Map<SocketGemType, Double> sockets = params.getSockets();
        Location loc = params.getLoc();
        Material mat = params.getMat();
        byte data = params.getData();
        BlockBreaker breaker = params.getBreaker();

        List<RewardApplicator> rewards = new ArrayList<>();
        List<PlayerRunnable> runnables = new ArrayList<>();

        if (mat == null || mat.equals(Material.AIR)) {
            return new EnchantmentResult(true, 0, rewards, runnables, BigInteger.ZERO);
        }
        if (mat.equals(Material.CHEST)) {
            return new EnchantmentResult(true, 0, rewards, runnables, BigInteger.ONE);
        }

        if (mat.equals(Material.GLOWING_REDSTONE_ORE)) {
            mat = Material.REDSTONE_ORE;
        }

        int xpAmount = 1;
        if (enchants.containsKey(CustomEnchant.ADEPT)) {
            xpAmount += MathUtils.roundRand(enchants.getOrDefault(CustomEnchant.ADEPT, 0) / 75.0);
        }

        if (mat.equals(Material.SPONGE)) {
            rewards.add(LuckyBlock.getLuckBlockReward(enchants.getOrDefault(CustomEnchant.FAVORED, 0), params.isLb_message(), sockets.getOrDefault(SocketGemType.FAVORED, 0.0)));
            return new EnchantmentResult(false, xpAmount, rewards, runnables, BigInteger.ONE);
        }
        if (mat.equals(Material.ENDER_STONE)) {
            if (params.isCanBreakSLBlocks()) {
                rewards.add(LuckyBlock.getSuperLuckBlockReward(enchants.getOrDefault(CustomEnchant.FAVORED, 0), params.isLb_message(), sockets.getOrDefault(SocketGemType.FAVORED, 0.0)));
                return new EnchantmentResult(false, xpAmount, rewards, runnables, BigInteger.ONE);
            } else {
                runnables.add((player) -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou may not break Super Lucky Blocks")));
                return new EnchantmentResult(true, xpAmount, rewards, runnables, BigInteger.ZERO);
            }
        }

        int itemcount = 1;

        // Fortune
        if (enchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            int lev = enchants.get(Enchantment.LOOT_BONUS_BLOCKS);
            itemcount += Math.floorDiv(lev, 100);
        }

        // Fortune socket
        if (MathUtils.isRandom(sockets.getOrDefault(SocketGemType.FORTUNE, 0.0), 100.0)) {
            itemcount++;
        }

        // Fortune pet
        itemcount += params.getPets().getOrDefault(PetType.FORTUNE, 0);

        // Create item
        ItemStack item = new ItemFactory(mat, 1, data).create();
        // Finalize itemcount
        int finalItemcount = itemcount;
        runnables.add((player) -> {
            // Run autosell
            if (AutoSell.isAutosell(player)) {
                AutoSell.addItems(player, ItemUtils.split(item, finalItemcount));
            } else {
                if (!InvUtils.invCanHold(player, item, finalItemcount)) {
                    player.sendTitle("§cINVENTORY FULL!", "§eShift + Left Click to Sell!", 5, 20, 5);
                }
                InvUtils.giveItems(player, item, finalItemcount);
            }
        });

        double luckyChance;
        if (breaker.equals(BlockBreaker.BOMB)) {
            luckyChance = 0.25 + enchants.getOrDefault(CustomEnchant.LUCKY, 0) * 0.002355;
        } else {
            luckyChance = 0.5 + enchants.getOrDefault(CustomEnchant.LUCKY, 0) * 0.007916666666666667;
        }
        if (enchants.containsKey(CustomEnchant.LUCKY) && MathUtils.isRandom(luckyChance, 100.0)) {
            double chanceOfMineKey = enchants.getOrDefault(CustomEnchant.LUCKY, 0) >= 1 ? 100.0 : 0.0;
            double chanceOfRareKey = enchants.getOrDefault(CustomEnchant.LUCKY, 0) >= 250 ? 40.0 : 0.0;
            double chanceOfLegKey = enchants.getOrDefault(CustomEnchant.LUCKY, 0) >= 500 ? 5.0 : 0.0;
            double chanceOfMythKey = enchants.getOrDefault(CustomEnchant.LUCKY, 0) >= 1000 ? 0.25 : 0.0;

            int mineKeyCount = MathUtils.isRandom(chanceOfMineKey, 100.0) ? 1 : 0;
            int rareKeyCount = MathUtils.isRandom(chanceOfRareKey, 100.0) ? 1 : 0;
            int legKeyCount = MathUtils.isRandom(chanceOfLegKey, 100.0) ? 1 : 0;
            int mythKeyCount = MathUtils.isRandom(chanceOfMythKey, 100.0) ? 1 : 0;

            if (MathUtils.isRandom(sockets.getOrDefault(SocketGemType.LUCKY_MINE, 0.0), 100.0)) {
                mineKeyCount++;
            }
            if (MathUtils.isRandom(sockets.getOrDefault(SocketGemType.LUCKY_RARE, 0.0), 100.0)) {
                rareKeyCount++;
            }
            if (MathUtils.isRandom(sockets.getOrDefault(SocketGemType.LUCKY_LEGENDARY, 0.0), 100.0)) {
                legKeyCount++;
            }
            if (MathUtils.isRandom(sockets.getOrDefault(SocketGemType.EPIC_LUCKY, 0.0), 100.0)) {
                if (legKeyCount > 0) {
                    legKeyCount--;
                    mythKeyCount++;
                }
            }

            if (enchants.containsKey(CustomEnchant.MYSTIC)) {
                double chanceOfUpMineKey = enchants.get(CustomEnchant.MYSTIC) / 10.0;
                if (MathUtils.isRandom(chanceOfUpMineKey, 100.0)) {
                    if (mineKeyCount > 0) {
                        rareKeyCount += 1;
                        mineKeyCount -= 1;
                    }
                }
                double chanceOfUpRareKey = (enchants.get(CustomEnchant.MYSTIC) - 100.0) / 100.0;
                if (MathUtils.isRandom(chanceOfUpRareKey, 100.0)) {
                    if (rareKeyCount > 0) {
                        legKeyCount += 1;
                        rareKeyCount -= 1;
                    }
                }
                double chanceOfUpLegKey = Math.floorDiv(enchants.get(CustomEnchant.MYSTIC), 50) * 0.05 - 0.25;
                if (MathUtils.isRandom(chanceOfUpLegKey, 100.0)) {
                    if (legKeyCount > 0) {
                        mythKeyCount += 1;
                        legKeyCount -= 1;
                    }
                }
            }
            double keyMulti = 1 + (enchants.getOrDefault(CustomEnchant.MAGNETIC, 0) / 500.0);
            mineKeyCount = MathUtils.roundRand(keyMulti * mineKeyCount);
            rareKeyCount = MathUtils.roundRand(keyMulti * rareKeyCount);
            legKeyCount = MathUtils.roundRand(keyMulti * legKeyCount);
            // mythKeyCount = MathUtils.roundRand(keyMulti * mythKeyCount);

            if (mineKeyCount > 0) {
                rewards.add(new KeyReward("mine", mineKeyCount).getApplicator(params.isKey_message() ? (name, amount) -> "§e+" + amount + " " + name : null));
            }
            if (rareKeyCount > 0) {
                rewards.add(new KeyReward("rare", rareKeyCount).getApplicator(params.isKey_message() ? (name, amount) -> "§e+" + amount + " " + name : null));
            }
            if (legKeyCount > 0) {
                rewards.add(new KeyReward("legendary", legKeyCount).getApplicator(params.isKey_message() ? (name, amount) -> "§e+" + amount + " " + name : null));
            }
            if (mythKeyCount > 0) {
                rewards.add(new KeyReward("mythical", mythKeyCount).getApplicator(params.isKey_message() ? (name, amount) -> "§e+" + amount + " " + name : null));
            }
        }

        runnables.add((player) -> EnchantUtils.runTokenGreed(player, enchants.getOrDefault(CustomEnchant.TOKENGREED, 0)));

        if (enchants.containsKey(CustomEnchant.SATURATED)) {
            runnables.add((player) -> EnchantUtils.runSaturated(player, enchants.get(CustomEnchant.SATURATED)));
        }
        if (enchants.containsKey(CustomEnchant.DETONATION)) {
            EnchantUtils.runDetonation(loc, enchants.get(CustomEnchant.DETONATION), sockets.getOrDefault(SocketGemType.EPIC_DETONATION, 0.0));
        }
        if (enchants.containsKey(CustomEnchant.ERUPTION)) {
            runnables.add((player) -> EnchantUtils.runEruption(player, enchants.get(CustomEnchant.ERUPTION)));
        }
        if (breaker.equals(BlockBreaker.PLAYER)) {
            if (enchants.containsKey(CustomEnchant.LIGHTNING)) {
                runnables.add((player) -> EnchantUtils.runLightning(player, loc, enchants.get(CustomEnchant.LIGHTNING), sockets.getOrDefault(SocketGemType.LIGHTNING, 0.0)));
            }
            if (enchants.containsKey(CustomEnchant.EXPLOSIVE)) {
                runnables.add((player) -> EnchantUtils.runExplosive(player, loc, enchants.get(CustomEnchant.EXPLOSIVE), sockets.getOrDefault(SocketGemType.EXPLOSIVE, 0.0), enchants.getOrDefault(CustomEnchant.OBLITERATE, 0), sockets.getOrDefault(SocketGemType.OBLITERATE, 0.0)));
            }
            if (enchants.containsKey(CustomEnchant.CHARITY)) {
                runnables.add((player) -> EnchantUtils.runCharity(player, enchants.get(CustomEnchant.CHARITY)));
            }
        }
        return new EnchantmentResult(false, xpAmount, rewards, runnables, BigInteger.ONE);
    }

    public static double getMultiplier(Player player) {
        Pickaxe pick = new Pickaxe(player);
        Location loc = player.getLocation();
        World world = player.getWorld();

        double multi = 0;
        if (pick.hasEnchantment(CustomEnchant.MERCHANT)) {
            multi += 0.015 * pick.getEnchantmentLevel(CustomEnchant.MERCHANT);
        }
        if (pick.hasEnchantment(CustomEnchant.SURGE)) {
            if (world.isThundering()) {
                multi += 0.2 * pick.getEnchantmentLevel(CustomEnchant.SURGE);
            } else {
                if (world.hasStorm()) {
                    multi += 0.15 * pick.getEnchantmentLevel(CustomEnchant.SURGE);
                }
            }
        }
        if (pick.hasEnchantment(CustomEnchant.CLUSTER)) {
            if (MineManager.isInOutMine(loc)) {
                String mine = MineManager.getInOutMineAt(loc);
                for (Player pl : world.getPlayers()) {
                    if (Objects.equals(MineManager.getInOutMineAt(pl.getLocation()), mine)) {
                        multi += 0.035;
                    }
                }
            }
        }
        multi += getPercent(player, SocketGemType.SELL_MULTI) / 100.0;
        return multi;
    }

    public static long getRepairPrice(Player player) {
        return getRepairPrice(getPickaxe(player));
    }

    @Contract(pure = true)
    public static long getRepairPrice(ItemStack pick) {
        return 20;
    }

    @Nullable
    public static SocketGem getSocketGem(ItemStack pickaxe, int number) {
        if (NBTUtils.hasTag(pickaxe, "socketGem" + number)) {
            return SocketGem.deserialize(NBTUtils.getString(pickaxe, "socketGem" + number));
        }
        return null;
    }

    public static SocketGem getSocketGem(Player player, int number) {
        return getSocketGem(getPickaxe(player), number);
    }

    @NotNull
    public static ItemStack setSocketGem(ItemStack pickaxe, int number, @NotNull SocketGem socketGem) {
        return NBTUtils.setString(pickaxe, "socketGem" + number, socketGem.serialize());
    }

    @NotNull
    public static ItemStack setSocketGem(Player player, int number, @NotNull SocketGem socketGem) {
        return setSocketGem(getPickaxe(player), number, socketGem);
    }

    public static ItemStack removeSocketGem(ItemStack pickaxe, int number) {
        return NBTUtils.remove(pickaxe, "socketGem" + number);
    }

    public static ItemStack removeSocketGem(Player player, int number) {
        return removeSocketGem(getPickaxe(player), number);
    }

    public static int getMaxSocketGems(ItemStack pickaxe) {
        int level = NBTUtils.getInt(pickaxe, "level");
        for (int i = 0; i < socketRequiredLevels.length; i++) {
            if (level < socketRequiredLevels[i]) {
                return i;
            }
        }
        return socketRequiredLevels.length;
    }

    public static int getMaxSocketGems(Player player) {
        return getMaxSocketGems(getPickaxe(player));
    }

    public static boolean hasUnlockedSocketGem(ItemStack pickaxe, int number) {
        return number < getMaxSocketGems(pickaxe);
    }

    public static boolean hasUnlockedSocketGem(Player player, int number) {
        return number < getMaxSocketGems(player);
    }

    public static double getPercent(Player player, SocketGemType type) {
        return getPercent(getPickaxe(player), type);
    }

    public static double getPercent(ItemStack pickaxe, SocketGemType type) {
        int max = getMaxSocketGems(pickaxe);
        List<Double> percents = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            SocketGem gem = getSocketGem(pickaxe, i);
            if (gem != null) {
                if (gem.getType().equals(type)) {
                    percents.add(gem.getPercent());
                }
            }
        }
        return type.getTotal(percents);
    }

    public static Map<SocketGemType, Double> getSockets(ItemStack pickaxe) {
        Map<SocketGemType, Double> ret = new HashMap<>();
        for (int i = 0; i < getMaxSocketGems(pickaxe); i++) {
            SocketGem gem = getSocketGem(pickaxe, i);
            if (gem == null) {
                continue;
            }
            SocketGemType type = gem.getType();
            double am = ret.getOrDefault(type, 0.0) + gem.getPercent();
            ret.put(type, am);
        }
        return ret;
    }
}
