/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultType;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KeyVaultReward extends ItemReward {
    private KeyVaultType type;

    public KeyVaultReward(KeyVaultType type) {
        this.type = type;
    }

    public KeyVaultReward(String[] strings) {
        this(KeyVaultType.valueOf(strings[1].toUpperCase()));
    }

    @Override
    public ItemStack getItem() {
        return KeyVaultManager.generateKeyVault(type);
    }

    @Override
    public boolean canApply(Player player) {
        ItemStack[] items = player.getInventory().getContents();
        for (int i = 0; i < 36; i++) {
            if (items[i] == null) {
                return true;
            }
        }
        return false;
    }
}
