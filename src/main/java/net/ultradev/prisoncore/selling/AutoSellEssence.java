/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.selling;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Objects;

public class AutoSellEssence {
    public static ItemStack getAutoSellEssence(BigInteger time) {
        return new ItemFactory(Material.INK_SACK, 1, (short) 11).addEnchantment(Enchantment.DURABILITY)
                .setName("§6Auto Sell Essence")
                .setLore(
                        "§7Right click to receive",
                        "§7Auto Sell time!",
                        "§7",
                        "§7If your donator rank",
                        "§7is [§e§lGold§7] or higher",
                        "§7then you will receive",
                        "§7Auto Miner time instead!",
                        "§7",
                        "§7Duration: §e" + DateUtils.convertTime(time.divide(BigInteger.valueOf(1000)).longValue()))
                .hideFlags()
                .addNBT("type", "autoSellEssence")
                .addNBT("time", time.toString())
                .create();
    }

    public static boolean isAutoSellEssence(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return (Objects.equals(NBTUtils.getString(item, "type"), "autoSellEssence"));
    }
}
