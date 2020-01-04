/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class AutoMinerSkipper {
    public static ItemStack generateItem(long time) {
        return new ItemFactory(Material.WATCH)
                .setName("§dAuto Miner Skipper")
                .addEnchantment(Enchantment.DIG_SPEED, 1)
                .hideFlags()
                .setLore(
                        "§7Right click to skip",
                        "§7Auto Miner time and",
                        "§7collect earnings!",
                        "§7Approximately half as",
                        "§7effective as a normal",
                        "§7Auto Miner.",
                        "§7",
                        "§7Time: §e" + DateUtils.convertTime(time / 1000L))
                .addNBT("type", "autominerSkipper")
                .addNBT("time", time)
                .create();
    }

    public static boolean isAutoMinerSkipper(ItemStack item) {
        return ItemUtils.isType(item, "autominerSkipper");
    }

    public static long getTime(ItemStack item) {
        if (!isAutoMinerSkipper(item)) {
            return -1;
        }
        return NBTUtils.getLong(item, "time");
    }
}
