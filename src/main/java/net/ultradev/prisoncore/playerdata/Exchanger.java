/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata;

import net.ultradev.prisoncore.autominer.AutoMinerChip;
import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.rewards.*;
import net.ultradev.prisoncore.selling.AutoSellEssence;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class Exchanger {
    public static boolean isExchangeable(ItemStack item) {
        if (Multiplier.isMultiplier(item))
            return true;
        if (AutoSellEssence.isAutoSellEssence(item))
            return true;
        if (Bombs.isBomb(item))
            return true;
        if (ScrollUtils.isScroll(item))
            return false;
        return false;
    }

    public static BigInteger getWorth(ItemStack item) {
        BigInteger worth = null;
        if (Multiplier.isMultiplier(item)) {
            Multiplier multi = Multiplier.parseFromItem(item);
            worth = BigInteger.valueOf(Math.round(2 * multi.getRemainingTime() * multi.getMulti() / 1000));
            worth = worth.multiply(BigInteger.valueOf(item.getAmount()));
        }
        if (AutoSellEssence.isAutoSellEssence(item)) {
            worth = BigInteger.ONE;
        }
        if (AutoMinerChip.isAutoMinerChip(item)) {
            worth = BigInteger.valueOf(Math.round(0.004 * NBTUtils.getLong(item, "time") / 1000));
            worth = worth.multiply(BigInteger.valueOf(item.getAmount()));
        }
        if (Bombs.isBomb(item)) {
            worth = BigInteger.valueOf(Math.round(20 * (Bombs.getPower(item) - 3)));
            worth = worth.multiply(BigInteger.valueOf(item.getAmount()));
        }
        if (ScrollUtils.isScroll(item)) {
            worth = BigInteger.TEN;
        }
        return worth;
    }

    public static boolean shouldAutoExchange(Reward rew, Player player) {
        if (!player.hasPermission("ultraprison.obsidian")) {
            Debugger.log("No permission", "exchanger");
            return false;
        }
        if (!PlayerData.getSetting(player, "auto_exchange")) {
            Debugger.log("Setting is off", "exchanger");
            return false;
        }
        if (!(rew instanceof ItemReward)) {
            Debugger.log("Not an item", "exchanger");
            return false;
        }
        Debugger.log("Running through", "exchanger");
        ItemReward ir = (ItemReward) rew;
        if (ir instanceof MultiplierReward) {
            MultiplierReward mr = (MultiplierReward) ir;
            return mr.getMulti().getMulti() < 2.0;
        }
        if (ir instanceof BombReward) {
            BombReward br = (BombReward) ir;
            return br.getPower() < 5;
        }
        if (ir instanceof AutoSellEssenceReward) {
            return true;
        }
        if (ir instanceof AutoMinerChipReward) {
            return true;
        }
        if (ir instanceof ScrollReward) {
            ScrollReward re = (ScrollReward) ir;
            Enchantment enchant = re.getEnchant();
            return new Pickaxe(player).getEnchantmentLevel(enchant) >= EnchantInfo.getMaxScrollLevel(enchant);
        }
        Debugger.log("None found", "exchanger");
        return false;
    }

    public static AutoExchangerResult autoExchange(@NotNull ItemReward reward) {
        return autoExchange(Collections.singletonList(reward));
    }

    @NotNull
    public static AutoExchangerResult autoExchange(@NotNull List<ItemReward> rewards) {
        AutoExchangerResult res = new AutoExchangerResult();
        for (ItemReward rew : rewards) {
            ItemStack item = rew.getItem();
            BigInteger worth = getWorth(item);
            res.tokens = res.tokens.add(worth);
            res.itemcount += item.getAmount();
        }
        return res;
    }

    public static class AutoExchangerResult {
        public BigInteger tokens;
        public int itemcount;

        public AutoExchangerResult() {
            this.tokens = BigInteger.ZERO;
            this.itemcount = 0;
        }
    }
}
