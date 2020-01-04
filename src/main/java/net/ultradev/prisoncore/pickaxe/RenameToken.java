/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class RenameToken {
    public static ItemStack getRenameToken(int amount) {
        return new ItemFactory(Material.NAME_TAG, amount)
                .setName("§e§lRename Tokens")
                .setLore(
                        "§7Right click to rename",
                        "§7your pickaxe."
                )
                .addNBT("type", "renameToken")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .create();
    }

    public static boolean isRenameToken(ItemStack item) {
        return ItemUtils.isType(item, "renameToken");
    }
}
