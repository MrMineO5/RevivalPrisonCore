/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class BombReward extends ItemReward {
    @Getter
    private int power;
    @Getter
    private int count;

    public BombReward(int power, int count) {
        this.power = power;
        this.count = count;
    }

    public BombReward(String[] strings) {
        this(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
    }

    @Override
    public ItemStack getItem() {
        return Bombs.generateBombItem(power, count);
    }
}
