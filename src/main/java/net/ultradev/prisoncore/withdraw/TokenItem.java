/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.withdraw;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TokenItem {
    public static ItemStack getTokenItem(long tokens, int amount) {
        return new ItemFactory(Material.DOUBLE_PLANT, amount)
                .setName("§eTokens")
                .setLore(
                        "§7Right click to redeem",
                        "§7Tokens: §e" + NumberUtils.formatFull(tokens)
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .hideFlags()
                .addNBT("type", "tokenItem")
                .addNBT("amount", tokens)
                .create();
    }

    public static boolean isTokenItem(ItemStack item) {
        return ItemUtils.isType(item, "tokenItem");
    }

    public static long getTokens(ItemStack item) {
        if (!isTokenItem(item)) {
            return -1;
        }
        return NBTUtils.getLong(item, "amount");
    }
}
