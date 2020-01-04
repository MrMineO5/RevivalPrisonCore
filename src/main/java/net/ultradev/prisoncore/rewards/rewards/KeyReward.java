/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.rewards.MessageExecutor;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.RewardApplicator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class KeyReward extends Reward {
    @Getter
    private String crate;
    @Getter
    private int count;

    /**
     * Generate a key reward for a specific amount of crate keys of a specific type
     *
     * @param crate Type
     * @param count Amount
     */
    public KeyReward(String crate, int count) {
        this.crate = crate;
        this.count = count;
    }

    public KeyReward(String[] strings) {
        this(strings[1], Integer.parseInt(strings[2]));
    }

    @Override
    public RewardApplicator getApplicator(MessageExecutor message) {
        return (player) -> {
            if (message != null) {
                player.sendMessage(message.run(Objects.requireNonNull(CrateManager.getCrate(crate)).displayName + " Crate Key", count));
            }
            CrateManager.giveKeys(player, crate, count);
        };
    }

    @Override
    public boolean canApply(Player player) {
        return (CrateManager.getInvCap(player, crate) >= count);
    }

    public ItemStack getGUIItem() {
        return Objects.requireNonNull(CrateManager.getCrate(crate)).getKey(count);
    }
}
