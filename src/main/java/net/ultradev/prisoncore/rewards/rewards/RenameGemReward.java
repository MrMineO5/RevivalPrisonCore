/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.pickaxe.RenameGem;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class RenameGemReward extends ItemReward {
    public RenameGemReward() {
    }

    public RenameGemReward(String[] strings) {
        this();
    }

    @Override
    public ItemStack getItem() {
        return RenameGem.getRenameGem();
    }
}
