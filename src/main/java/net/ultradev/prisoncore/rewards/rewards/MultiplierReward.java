/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.multipliers.Multiplier.MultiplierType;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MultiplierReward extends ItemReward {
    @Getter
    private Multiplier multi;

    public MultiplierReward(Multiplier multi) {
        this.multi = multi;
    }

    public MultiplierReward(MultiplierType type, int length, double multi) {
        this.multi = new Multiplier(type, length, multi);
    }

    public MultiplierReward(@NotNull String[] strings) {
        this(Multiplier.MultiplierType.valueOf(strings[1].toUpperCase()), Integer.parseInt(strings[2]), Double.parseDouble(strings[3]));
    }

    @Override
    public ItemStack getItem() {
        return this.multi.getItem();
    }
}
