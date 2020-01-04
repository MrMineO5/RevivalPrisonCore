/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards;

import net.ultradev.prisoncore.utils.items.InvUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemReward extends Reward {
    /**
     * Get the item of this ItemReward
     *
     * @return Item
     */
    public abstract ItemStack getItem();

    public RewardApplicator getApplicator(MessageExecutor message) {
        return (player) -> {
            ItemStack item = getItem();
            player.getInventory().addItem(item);
            if (message != null) {
                player.sendMessage(message.run(item.getItemMeta().getDisplayName(), item.getAmount()));
            }
        };
    }

    public boolean canApply(Player player) {
        return !InvUtils.isFull(player);
    }

    public ItemStack getGUIItem() {
        return getItem();
    }
}
