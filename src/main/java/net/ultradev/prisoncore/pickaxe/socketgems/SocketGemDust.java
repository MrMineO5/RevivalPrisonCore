/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SocketGemDust {
    public static ItemStack getSocketGemDust(int amount) {
        return new ItemFactory(Material.SULPHUR, amount)
                .setName("§eSocket Gem Dust")
                .setLore(
                        "§7Used to craft powerful",
                        "§7Socket Gems!"
                )
                .addNBT("type", "socketGemDust")
                .create();
    }

    public static boolean isSocketGemDust(ItemStack item) {
        return ItemUtils.isType(item, "socketGemDust");
    }
}
