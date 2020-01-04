/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class PetXp {
    public static void addXp(Player player, XpType type, int amount) {
        for (PetType val : PetType.values()) {
            if (val.getXpType().equals(type)) {
                int slot = PetManager.getPet(player, val);
                if (slot != -1) {
                    ItemStack pet = player.getInventory().getItem(slot);
                    player.getInventory().setItem(slot, PetManager.addXp(pet, amount));
                }
            }
        }
    }

    public enum XpType {
        MINING, BLOCKS_SOLD, CRATES_OPENED, TOKENS_EARNED
    }
}
