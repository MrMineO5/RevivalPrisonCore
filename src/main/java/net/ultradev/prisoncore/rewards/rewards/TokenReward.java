/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.rewards.MessageExecutor;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.RewardApplicator;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class TokenReward extends Reward {
    @Getter
    private BigInteger amount;

    /**
     * Create a token reward for a specific amount of tokens
     *
     * @param amount Amount of tokens
     */
    public TokenReward(BigInteger amount) {
        this.amount = amount;
    }

    /**
     * Create a token reward for a specific amount of tokens
     *
     * @param amount Amount of tokens
     */
    public TokenReward(long amount) {
        this.amount = BigInteger.valueOf(amount);
    }

    public TokenReward(String[] strings) {
        this(new BigInteger(strings[1]));
    }

    public RewardApplicator getApplicator(MessageExecutor message) {
        return (player) -> {
            Economy.tokens.addBalance(player, amount);
            if (message != null) {
                player.sendMessage(message.run("", amount.intValue()));
            }
        };
    }

    public boolean canApply(Player player) {
        return true;
    }

    public ItemStack getGUIItem() {
        return new ItemFactory(Material.DOUBLE_PLANT)
                .setName("§6§l" + amount + " tokens")
                .hideFlags()
                .create();
    }
}
