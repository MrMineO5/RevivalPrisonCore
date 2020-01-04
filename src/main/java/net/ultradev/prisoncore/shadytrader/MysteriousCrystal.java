/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.shadytrader;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class MysteriousCrystal {
    public static ItemStack getItem(int amount) {
        return new ItemFactory(Material.DIAMOND, amount)
                .setName("§b§lMysterious Crystal")
                .setLore("§7A lightweight, beautiful crystal", "§7with an unknown purpose.")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .addNBT("type", "mysteriousCrystal")
                .create();
    }

    public static boolean isMysteriousCrystal(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type"))
            return false;
        return Objects.equals(NBTUtils.getString(item, "type"), "mysteriousCrystal");
    }

    public static int countMysteriousCrystals(ItemStack[] items) {
        return Arrays.stream(items)
                .filter(MysteriousCrystal::isMysteriousCrystal)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }
}
