/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.selling.AutoSellEssence;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class AutoSellEssenceReward extends ItemReward {
    @Getter
    private BigInteger time;

    public AutoSellEssenceReward(int time) {
        this.time = BigInteger.valueOf(time);
    }

    public AutoSellEssenceReward(BigInteger time) {
        this.time = time;
    }

    public AutoSellEssenceReward(String[] strs) {
        this(new BigInteger(strs[1]));
    }

    @Override
    public ItemStack getItem() {
        return AutoSellEssence.getAutoSellEssence(time);
    }
}
