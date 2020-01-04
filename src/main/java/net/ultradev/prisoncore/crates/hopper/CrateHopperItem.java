/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.crates.hopper;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class CrateHopperItem {
    public static ItemStack getCrateHopperItem(int amount) {
        return (new ItemFactory(Material.HOPPER, amount))
                .setName("Hopper")
                .setLore(
                        "§7Place this above a chest!", "§7Automatically opens the keys in it"
                ).addNBT("type", "cratehopper")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .create();
    }


    public static boolean isCrateHopperItem(ItemStack item) {
        return ItemUtils.isType(item, "cratehopper");
    }
}
