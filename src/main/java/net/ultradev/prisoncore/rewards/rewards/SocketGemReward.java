/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class SocketGemReward extends ItemReward {
    private SocketGem gem;

    public SocketGemReward(SocketGemType type, SocketGemTier tier, double percent) {
        this.gem = new SocketGem(type, tier, percent);
    }

    public SocketGemReward(String[] strings) {
        this(SocketGemType.valueOf(strings[1].toUpperCase()), SocketGemTier.valueOf(strings[2].toUpperCase()), Double.parseDouble(strings[3]));
    }

    public SocketGemReward(SocketGem gem) {
        this.gem = gem;
    }

    @Override
    public ItemStack getItem() {
        return this.gem.getItem();
    }
}
