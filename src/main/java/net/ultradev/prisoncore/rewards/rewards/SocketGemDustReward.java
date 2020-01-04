/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemDust;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class SocketGemDustReward extends ItemReward {
    private int dust;

    public SocketGemDustReward(int dust) {
        this.dust = dust;
    }

    public SocketGemDustReward(String[] strings) {
        this(Integer.parseInt(strings[1]));
    }

    @Override
    public ItemStack getItem() {
        return SocketGemDust.getSocketGemDust(dust);
    }
}
