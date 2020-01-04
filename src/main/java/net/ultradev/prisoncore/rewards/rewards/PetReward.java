/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class PetReward
        extends ItemReward {
    private PetType type;

    public PetReward(PetType type) {
        this.type = type;
    }


    public PetReward(String[] strings) {
        this(PetType.valueOf(strings[1]));
    }


    public ItemStack getItem() {
        return PetManager.createPet(this.type);
    }


    public boolean canApply(@NotNull Player player) {
        ItemStack[] items = player.getInventory().getContents();
        for (int i = 0; i < 36; i++) {
            if (items[i] == null) {
                return true;
            }
        }
        return false;
    }
}
