/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.rankupgrade.RankUpgrade;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class RankUpgradeReward extends ItemReward {
    public RankUpgradeReward() {

    }

    public RankUpgradeReward(String[] strings) {
        this();
    }

    @Override
    public ItemStack getItem() {
        return RankUpgrade.getItem();
    }
}
