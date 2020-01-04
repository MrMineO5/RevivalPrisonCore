/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards.rewards;

import lombok.Getter;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.rewards.ItemReward;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ScrollReward extends ItemReward {
    @Getter
    private Enchantment enchant;

    public ScrollReward(Enchantment enchant) {
        this.enchant = enchant;
    }

    public ScrollReward(String[] strings) {
        this(CustomEnchant.getByName(strings[1]));
    }

    @Override
    public ItemStack getItem() {
        return ScrollUtils.createScroll(enchant);
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
