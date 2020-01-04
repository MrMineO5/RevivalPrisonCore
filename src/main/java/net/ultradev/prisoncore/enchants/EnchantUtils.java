/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pickaxe.EnchantmentResult;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.breakblock.BlockBreaker;
import net.ultradev.prisoncore.pickaxe.breakblock.BreakBlockParameters;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.plugins.FaweAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.ultradev.prisoncore.utils.logging.Debugger.log;

public class EnchantUtils {
    private static final boolean useAsync = true;
    public static double detonation_chancePerLevel = 0.2;
    public static int detonation_radiusMin = 3;
    public static int detonation_radiusMax = 4;
    public static double saturated_chancePerLevel = 0.05;
    public static double explosive_initialChance = 5.0;
    public static double explosive_chancePerLevel = 0.18;
    public static double charity_chancePerLevel = 0.0005;
    public static int charity_amountPerLevelMin = 500;
    public static int charity_amountPerLevelMax = 600;
    public static double eruption_chancePerLevel = 0.0025;
    public static double eruption_minPerLevel = 20.0;
    public static double eruption_maxPerLevel = 25.0;
    public static double tokengreed_baseChance = 1.0;
    public static double tokengreed_chancePerLevel = 0.003;
    public static double tokengreed_baseMin = 10.0;
    public static double tokengreed_minPerLevel = 0.825;
    public static double tokengreed_baseMax = 10.0;
    public static double tokengreed_maxPerLevel = 0.825;

    public static void runEnchantment(Player player, List<Location> broken, BlockBreaker breaker) {
        if (broken.isEmpty()) {
            return;
        }

        // Get properties
        ItemStack pickaxe = PickaxeUtils.getPickaxe(player);
        final Map<Enchantment, Integer> enchants = pickaxe.getEnchantments();
        final Map<SocketGemType, Double> sockets = PickaxeUtils.getSockets(pickaxe);
        final boolean isDonator = player.hasPermission("ultraprison.donator");
        final boolean key_message = PlayerData.getSetting(player, "key_message");
        final boolean lb_message = PlayerData.getSetting(player, "lb_message");
        final Map<PetType, Integer> pets = PetManager.getPets(player);
        // Get Mine
        Location l = broken.get(0);
        Mine m = MineManager.getMineAt(l);

        // Get FAWE
        com.sk89q.worldedit.world.World asyncWorld = FaweAPI.getWorld(l.getWorld().getName());
        EditSession session = FaweAPI
                .getEditSessionBuilder(asyncWorld)
                .fastmode(true)
                .build();
        assert asyncWorld != null;

        // Break asynchronously
        Synchronizer.desynchronize(() -> {
            // Create result container
            EnchantmentResult res = new EnchantmentResult(false, 0, new ArrayList<>(), new ArrayList<>(), BigInteger.ZERO);

            // Loop through blocks
            for (Location loc : broken) {
                BaseBlock block = asyncWorld.getBlock(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                EnchantmentResult r = PickaxeUtils.breakBlockAsync(
                        new BreakBlockParameters(
                                enchants,
                                sockets,
                                loc,
                                Material.getMaterial(block.getType()),
                                (byte) block.getData(),
                                breaker, isDonator, key_message, lb_message,
                                pets
                        )
                );
                if (!r.getCancelled()) {
                    try {
                        session.setBlock(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), new SingleBlockPattern(new BaseBlock(Material.AIR.getId())));
                    } catch (MaxChangedBlocksException e) {
                        e.printStackTrace();
                    }
                    res.add(r);
                }
            }
            Synchronizer.synchronize(() -> {
                Debugger.log("Applying completion!", "enchant");
                session.flushQueue();
                assert m != null;
                m.blocksBroken(res.getBlocksMined().intValue());
                PickaxeUtils.applyReward(player, res);
            });
        });
    }

    public static void runLightning(Player player, Location block, int lightningLevel, double biggerRadiusChance) {
        log("Checking lightning...", "lightning_enchant");
        double lightning_chancePerLevel = 0.015;
        if (!MathUtils.isRandom(lightning_chancePerLevel * lightningLevel, 100.0)) {
            log("Chance false", "lightning_enchant");
            return;
        }
        int radius = 4;
        if (MathUtils.isRandom(biggerRadiusChance, 100.0)) {
            radius = 5;
        }
        log("Running lightning...", "lightning_enchant");
        log("Radius = " + radius, "lightning_enchant");
        final Location loc = block;
        int finalRadius = radius;

        Mine m = MineManager.getMineAt(loc);
        if (m == null) {
            return;
        }

        Synchronizer.desynchronize(() -> {
            Debugger.log("Running lightning blocks...", "lightning_enchant");
            World world = loc.getWorld();
            int blockX = loc.getBlockX();
            int blockY = loc.getBlockY();
            int blockZ = loc.getBlockZ();
            List<Location> locs = new ArrayList<>();
            for (int x = blockX - finalRadius; x <= blockX + finalRadius; x++) {
                Debugger.log("Running lightning x=" + x + "...", "lightning_enchant");
                for (int y = blockY - finalRadius; y <= blockY + finalRadius; y++) {
                    Debugger.log("Running lightning y=" + y + "...", "lightning_enchant");
                    if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) > Math.pow(finalRadius, 2))
                        continue;
                    for (int z = blockZ - finalRadius; z <= blockZ + finalRadius; z++) {
                        Debugger.log("Running lightning z=" + z + "...", "lightning_enchant");
                        if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) + Math.pow(z - blockZ, 2) < Math
                                .pow(finalRadius, 2)) {
                            Location l = new Location(world, x, y, z);
                            if (!m.isInside(l))
                                continue;
                            locs.add(l);
                        }
                    }
                }
            }
            Debugger.log("Starting enchantment runner...", "lightning_enchant");
            Synchronizer.synchronize(() -> {
                Debugger.log("Running...", "lightning_enchant");
                runEnchantment(player, locs, BlockBreaker.ENCHANT);
            });
        });
        loc.getWorld().strikeLightningEffect(loc);
    }

    public static void runDetonation(Location block, int detonationLevel, double detoSocket) {
        log("Checking detonation...", "detonation_enchant");
        if (!MathUtils.isRandom(detonation_chancePerLevel * detonationLevel, 100.0)) {
            log("Chance false", "detonation_enchant");
            return;
        }
        log("Running detonation...", "detonation_enchant");
        int radius = MathUtils.random(detonation_radiusMin, detonation_radiusMax);
        if (MathUtils.isRandom(detoSocket, 100.0)) {
            radius++;
        }
        log("Radius = " + radius, "detonation_enchant");
        final Location loc = block;
        int finalRadius = radius;
        Synchronizer.desynchronize(() -> {
            final int blockX = loc.getBlockX();
            final int blockY = loc.getBlockY();
            final int blockZ = loc.getBlockZ();
            for (int x = blockX - finalRadius; x <= blockX + finalRadius; x++) {
                final int xL = x;
                for (int y = blockY - finalRadius; y <= blockY + finalRadius; y++) {
                    if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) > Math.pow(finalRadius, 2))
                        continue;
                    final int yL = y;
                    log("Synchronizing Z runner...", "detonation_enchant");
                    Synchronizer.synchronize(() -> {
                        for (int z = blockZ - finalRadius; z <= blockZ + finalRadius; z++) {
                            if (Math.pow(xL - blockX, 2) + Math.pow(yL - blockY, 2) + Math.pow(z - blockZ, 2) < Math
                                    .pow(finalRadius, 2)) {
                                Location l = new Location(loc.getWorld(), xL, yL, z);
                                Block b = l.getBlock();
                                if (!MineManager.isInMine(b))
                                    continue;
                                if (!b.getType().equals(Material.AIR)) {
                                    b.setType(Material.MAGMA);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public static void runSaturated(Player player, int saturatedLevel) {
        log("Checking saturated...", "saturation_enchant");
        if (!MathUtils.isRandom(saturated_chancePerLevel * saturatedLevel, 100.0)) {
            log("Chance false", "saturation_enchant");
            return;
        }
        log("Running saturated...", "saturation_enchant");
        player.setFoodLevel(20);
    }

    public static void runExplosive(Player player, Location block, int explosiveLevel, double explosiveGem, int obliterateLevel, double obliterateGem) {
        log("Checking explosive...", "explosive_enchant");
        if (!MathUtils.isRandom(explosive_initialChance + explosive_chancePerLevel * explosiveLevel, 100.0)) {
            log("Chance false", "explosive_enchant");
            return;
        }
        log("Cheking obliterate...", "explosive_enchant");
        final boolean obliterate = MathUtils.isRandom(0.1 * obliterateLevel, 100.0);
        if (obliterate) {
            log("Obliterate true", "explosive_enchant");
        } else {
            log("Obliterate false", "explosive_enchant");
        }
        int radius = obliterate ? 2 : 1;
        if (radius == 2 && MathUtils.isRandom(obliterateGem, 100.0)) {
            radius += 1;
        }
        log("Radius = " + radius, "explosive_enchant");
        final Location loc = block;
        double breakChance = 19.23;
        if (MathUtils.isRandom(explosiveGem, 100.0)) {
            breakChance *= MathUtils.random(1.7, 1.9);
        }
        int finalRadius = radius;
        double finalBreakChance = breakChance;
        Synchronizer.desynchronize(() -> {
            World world = loc.getWorld();
            int blockX = loc.getBlockX();
            int blockY = loc.getBlockY();
            int blockZ = loc.getBlockZ();
            List<Location> locs = new ArrayList<>();
            for (int x = blockX - finalRadius; x <= blockX + finalRadius; x++) {
                for (int y = blockY - finalRadius; y <= blockY + finalRadius; y++) {
                    if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) > Math.pow(finalRadius, 2))
                        continue;
                    for (int z = blockZ - finalRadius; z <= blockZ + finalRadius; z++) {
                        if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) + Math.pow(z - blockZ, 2) < Math
                                .pow(finalRadius, 2)) {
                            Location l = new Location(world, x, y, z);
                            if (!MineManager.isInMine(l))
                                return;
                            if (MathUtils.isRandom(finalBreakChance, 100.0)) {
                                log("Breaking block...", "explosive_enchant");
                                locs.add(l);
                            }
                        }
                    }
                }
            }
            Synchronizer.synchronize(() -> runEnchantment(player, locs, BlockBreaker.ENCHANT));
        });
    }

    public static void runCataclysm(Player player, Location block, int cataclysmLevel) {
        Mine mine = MineManager.getMineAt(block);
        assert mine != null;
        int y = mine.getMaxY() + 10;
        for (int i = 0; i < 3; i++) {
            int x = MathUtils.random(mine.getMinX() + 5, mine.getMaxX() - 5);
            int z = MathUtils.random(mine.getMinZ() + 5, mine.getMaxZ() - 5);
        }
    }

    public static void runCharity(Player player, int charityLevel) {
        log("Checking charity...", "charity_enchant");
        if (!MathUtils.isRandom(charity_chancePerLevel * charityLevel, 100.0)) {
            log("Chance false", "charity_enchant");
            return;
        }
        log("Running charity...", "charity_enchant");
        long tokens = MathUtils.random(charity_amountPerLevelMin * charityLevel, charity_amountPerLevelMax * charityLevel);
        if (MathUtils.isRandom(PickaxeUtils.getPercent(player, SocketGemType.CHARITY), 100.0)) {
            tokens *= MathUtils.random(1.3, 1.7);
        }
        TokenReward rew = new TokenReward(tokens);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            rew.applyReward(pl, (name, amount) ->
                    "§6Reward » §7Received §e" + NumberUtils.formatFull(amount) + " Tokens§7 from §e"
                            + player.getName() + "'s §dCharity §7enchant!");
        }
    }

    public static void runEruption(Player player, int eruptionLevel) {
        log("Checking eruption...", "eruption_enchant");
        if (!MathUtils.isRandom(eruption_chancePerLevel * eruptionLevel, 100.0)) {
            log("Chance false", "eruption_enchant");
            return;
        }
        log("Running eruption...", "eruption_enchant");
        Economy.tokens.addBalance(player, (long) Math.floor(MathUtils.random(eruption_minPerLevel * eruptionLevel, eruption_maxPerLevel * eruptionLevel)));
    }

    public static void runTokenGreed(Player player, int tokenGreedLevel) {
        log("Checking token greed...", "token_greed_enchant");
        if (!MathUtils.isRandom(tokengreed_baseChance + tokengreed_chancePerLevel * tokenGreedLevel, 100.0)) {
            log("Chance false", "token_greed_enchant");
            return;
        }
        log("Running token greed...", "token_greed_enchant");
        Economy.tokens.addBalance(player, (long) MathUtils.random(tokengreed_baseMin + tokengreed_minPerLevel * tokenGreedLevel, tokengreed_baseMax + tokengreed_maxPerLevel * tokenGreedLevel));
    }
}
