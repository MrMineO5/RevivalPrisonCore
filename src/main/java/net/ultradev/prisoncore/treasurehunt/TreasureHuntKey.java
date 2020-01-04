/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.treasurehunt;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TreasureHuntKey {
    public static ItemStack getTreasureHuntKey(int amount) {
        return new ItemFactory(Material.TRIPWIRE_HOOK, amount)
                .setName("§c§lTreasure Hunt Key")
                .setLore(
                        "§7Use in the spawn to start",
                        "§7a treasure hunt!"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .addNBT("type", "treasureHuntKey")
                .create();
    }

    public static boolean isTreasureHuntKey(ItemStack item) {
        return ItemUtils.isType(item, "treasureHuntKey");
    }
}
