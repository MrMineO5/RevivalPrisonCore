/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards;

import org.bukkit.entity.Player;

public interface RewardApplicator {
    /**
     * Apply the reward to the player
     *
     * @param player Player
     */
    void applyReward(Player player);
}