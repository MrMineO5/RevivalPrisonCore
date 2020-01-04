/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemEssence;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.inventory.ItemStack;

public class SocketGemEssenceReward extends ItemReward {
    private SocketGemTier tier;
    private int amount;

    /**
     * Create a Socket Gem Essence Reward with a tier and an amount of 1
     *
     * @param tier Tiers
     */
    public SocketGemEssenceReward(SocketGemTier tier) {
        this(tier, 1);
    }

    /**
     * Create a Socket Gem Essence Reward with a tier and amount
     *
     * @param tier   Tier
     * @param amount Amount
     */
    public SocketGemEssenceReward(SocketGemTier tier, int amount) {
        this.tier = tier;
        this.amount = amount;
    }

    public SocketGemEssenceReward(String[] strings) {
        this(SocketGemTier.valueOf(strings[1].toUpperCase()));
    }

    @Override
    public ItemStack getItem() {
        return SocketGemEssence.getItem(tier, amount);
    }
}
