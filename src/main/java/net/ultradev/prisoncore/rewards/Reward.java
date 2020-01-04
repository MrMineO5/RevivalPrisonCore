/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Reward {
    /**
     * Apply the reward to a player
     *
     * @param player Player
     * @param exec   Message executor
     */
    public void applyReward(Player player, MessageExecutor exec) {
        getApplicator(exec).applyReward(player);
    }

    /**
     * Get the RewardApplicator for a specific message executor
     *
     * @param exec Message executor
     * @return Reward Applicator for the reward
     */
    public abstract RewardApplicator getApplicator(MessageExecutor exec);

    /**
     * Check whether the reward can be applied to the player
     *
     * @param player Player
     * @return <code>true</code> if the reward can be applied
     */
    public abstract boolean canApply(Player player);

    /**
     * Get the item that is displayed in the preview GUIs
     *
     * @return Item
     */
    public abstract ItemStack getGUIItem();
}
