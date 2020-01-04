/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rankupgrade;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RankUpgrade {
    private static ItemStack rankUpgrade = new ItemFactory(Material.EMERALD)
            .setName("§aRank Upgrade")
            .setLore(
                    "§7Right click to upgrade",
                    "§7your donator rank!"
            )
            .addNBT("type", "rankUpgrade")
            .create();

    @Contract(pure = true)
    public static ItemStack getItem() {
        return rankUpgrade;
    }

    public static boolean isRankUpgrade(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return (NBTUtils.getString(item, "type").equals("rankUpgrade"));
    }

    public static boolean isHighestRank(@NotNull Player player) {
        return (player.hasPermission("ultraprison.ultra"));
    }

    public static void applyRankUpgrade(Player player) {
        if (isHighestRank(player)) {
            return;
        }
        if (player.hasPermission("ultraprison.obsidian")) {
            setGroup(player, "ultra");
            return;
        }
        if (player.hasPermission("ultraprison.emerald")) {
            setGroup(player, "obsidian");
            return;
        }
        if (player.hasPermission("ultraprison.diamond")) {
            setGroup(player, "emerald");
            return;
        }
        if (player.hasPermission("ultraprison.gold")) {
            setGroup(player, "diamond");
            return;
        }
        if (player.hasPermission("ultraprison.iron")) {
            setGroup(player, "gold");
            return;
        }
        if (player.hasPermission("ultraprison.coal")) {
            setGroup(player, "iron");
            return;
        }
        setGroup(player, "coal");
    }

    private static void setGroup(Player player, String group) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " addgroup " + group);
    }
}
