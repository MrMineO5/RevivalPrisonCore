/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SocketGemEssence {
    public static ItemStack getItem(SocketGemTier tier, int amount) {
        return new ItemFactory(Material.CLAY_BALL, amount)
                .setName(tier.getDisplayName() + "§7 Essence")
                .setLore(
                        "§7Right click to receive a",
                        "§7random " + tier.getName() + " Socket Gem!"
                )
                .addNBT("type", "socketGemEssence")
                .addNBT("tier", tier.toString())
                .create();
    }

    public static boolean isSocketGemEssence(ItemStack item) {
        return ItemUtils.isType(item, "socketGemEssence");
    }

    public static SocketGemTier getTier(ItemStack item) {
        return SocketGemTier.valueOf(NBTUtils.getString(item, "tier"));
    }
}
