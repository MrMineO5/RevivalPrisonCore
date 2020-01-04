/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.bombs;

import net.ultradev.prisoncore.enchants.EnchantUtils;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.pickaxe.breakblock.BlockBreaker;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Bombs {
    public static boolean isBomb(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (!NBTUtils.hasTag(item, "type"))
            return false;
        return (NBTUtils.getString(item, "type").equals("bomb"));
    }

    public static int getPower(ItemStack item) {
        assert isBomb(item);
        return NBTUtils.getInt(item, "power");
    }

    public static ItemStack generateBombItem(int power, int amount) {
        return new ItemFactory(Material.MAGMA_CREAM, amount)
                .setName("§6Bomb").setLore("§7Right click to detonate", "§7an area! Make sure to",
                        "§7have an empty inventory.", "§7", "§7Explosion Power: §e" + power)
                .addNBT("type", "bomb").addNBT("power", power).create();
    }

    public static void spawnBomb(Player player, int power) {
        ItemStack item = new ItemFactory(Material.MAGMA_CREAM).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .setName(player.getUniqueId().toString() + "#" + MathUtils.random(0, 2000000000)).hideFlags().create();
        Location loc = player.getLocation();
        Item it = loc.getWorld().dropItemNaturally(loc.add(0, 1, 0), item);
        it.setVelocity(loc.getDirection().multiply(2));
        it.setCustomName("§e" + player.getName() + "'s Bomb [§a5§e]" + HiddenStringUtils.encodeString("bomb"));
        it.setCustomNameVisible(true);
        it.setInvulnerable(true);
        Scheduler.scheduleSyncDelayedTask(
                () -> it.setCustomName(
                        "§e" + player.getName() + "'s Bomb [§a4§e]§6§l§6§6" + HiddenStringUtils.encodeString("bomb")),
                1);
        Scheduler.scheduleSyncDelayedTask(
                () -> it.setCustomName(
                        "§e" + player.getName() + "'s Bomb [§a3§e]§6§l§6§6" + HiddenStringUtils.encodeString("bomb")),
                2);
        Scheduler.scheduleSyncDelayedTask(
                () -> it.setCustomName(
                        "§e" + player.getName() + "'s Bomb [§a2§e]§6§l§6§6" + HiddenStringUtils.encodeString("bomb")),
                3);
        Scheduler.scheduleSyncDelayedTask(
                () -> it.setCustomName(
                        "§e" + player.getName() + "'s Bomb [§a1§e]§6§l§6§6" + HiddenStringUtils.encodeString("bomb")),
                4);
        Scheduler.scheduleSyncDelayedTask(
                () -> it.setCustomName(
                        "§e" + player.getName() + "'s Bomb [§a0§e]§6§l§6§6" + HiddenStringUtils.encodeString("bomb")),
                5);
        Scheduler.scheduleSyncDelayedTask(() -> {
            runExplosion(player, it.getLocation(), power);
            it.remove();
        }, 6);
    }

    private static void runExplosion(Player player, @NotNull Location loc, int power) {
        Debugger.log("Running bomb explosion effect", "bomb_explosion");
        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, false, false);
        final int radius = power;
        List<Location> locs = new ArrayList<>();
        Synchronizer.desynchronize(() -> {
            Debugger.log("Running bomb explosion", "bomb_explosion");
            final int blockX = loc.getBlockX();
            final int blockY = loc.getBlockY();
            final int blockZ = loc.getBlockZ();
            for (int x = blockX - radius; x <= blockX + radius; x++) {
                for (int y = blockY - radius; y <= blockY + radius; y++) {
                    if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) > Math.pow(radius, 2))
                        continue;
                    for (int z = blockZ - radius; z <= blockZ + radius; z++) {
                        if (Math.pow(x - blockX, 2) + Math.pow(y - blockY, 2) + Math.pow(z - blockZ, 2) < Math
                                .pow(radius, 2)) {
                            Location l = new Location(loc.getWorld(), x, y, z);
                            Block b = l.getBlock();
                            if (!MineManager.isInMine(b))
                                continue;
                            locs.add(l);
                        }
                    }
                }
            }
            Synchronizer.synchronize(() -> EnchantUtils.runEnchantment(player, locs, BlockBreaker.BOMB));
        });
    }
}
