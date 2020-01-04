/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.autominer.AutoMinerChip;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.utils.math.BigMath;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class AutoMinerChipReward extends ItemReward {
    @Getter
    private BigInteger time;

    public AutoMinerChipReward(BigInteger time) {
        this.time = time;
    }
    public AutoMinerChipReward(long time) {
        this(BigInteger.valueOf(time));
    }

    public AutoMinerChipReward(String[] strings) {
        this(new BigInteger(strings[1]));
    }

    @Override
    public ItemStack getItem() {
        return AutoMinerChip.generateItem(time.multiply(BigMath.THOUSAND));
    }
}
