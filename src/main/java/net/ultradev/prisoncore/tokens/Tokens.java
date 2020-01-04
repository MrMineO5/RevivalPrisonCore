/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.tokens;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Tokens {
    public static ItemStack generateItem(Double amount) {
        return new ItemFactory(Material.DOUBLE_PLANT).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .hideFlags().setName("§eTokens")
                .setLore("§7Right click to redeem", "§7 for Tokens!", "§7",
                        "§7Quantity: §e" + NumberUtils.formatFull(amount))
                .addNBT("type", "tokensItem").addNBT("tokens", amount).create();
    }

    public static boolean isTokenItem(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type"))
            return false;
        return (NBTUtils.getString(item, "type").equals("tokensItem"));
    }

    public static double getTokens(ItemStack item) {
        if (!isTokenItem(item)) {
            return 0.0;
        }
        return NBTUtils.getDouble(item, "tokens");
    }
}
