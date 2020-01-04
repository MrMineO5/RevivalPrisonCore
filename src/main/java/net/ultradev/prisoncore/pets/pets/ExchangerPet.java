/*
 * Copyright (c) 2020. UltraDev
 */

package net.ultradev.prisoncore.pets.pets;

import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.Exchanger;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.rewards.*;
import net.ultradev.prisoncore.utils.math.BigMath;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangerPet {
    public static ExchangerPet get(ItemStack item) {
        if (item == null) {
            return null;
        }
        return new ExchangerPet(item);
    }

    private int multis;
    private int bombs;
    private int asEssence;
    private int amChips;
    private int scrolls;

    private ExchangerPet(ItemStack pet) {
        this.multis = PetManager.getSetting(pet, "Multipliers");
        this.bombs = PetManager.getSetting(pet, "Bombs");
        this.asEssence = PetManager.getSetting(pet, "AutoSell_Essence");
        this.amChips = PetManager.getSetting(pet, "AutoMiner_Chips");
        this.scrolls = PetManager.getSetting(pet, "Scrolls");
    }

    public boolean shouldModify(Reward rew) {
        if (!(rew instanceof ItemReward)) {
            return false;
        }
        ItemReward ir = (ItemReward) rew;
        if (ir instanceof MultiplierReward) {
            return multis > 0;
        }
        if (ir instanceof BombReward) {
            return bombs > 0;
        }
        if (ir instanceof AutoSellEssenceReward) {
            return asEssence > 0;
        }
        if (ir instanceof AutoMinerChipReward) {
            return amChips > 0;
        }
        if (ir instanceof ScrollReward) {
            return scrolls > 0;
        }
        return false;
    }

    public void run(Player player, List<ItemReward> rewards) {
        // Initialize variables
        List<ItemReward> toExchange = new ArrayList<>();
        BigInteger autoSellTime = BigInteger.ZERO;
        BigInteger autoMinerTime = BigInteger.ZERO;
        Map<Enchantment, Integer> enchants = new HashMap<>(PickaxeUtils.getPickaxe(player).getEnchantments());

        // Cycle through rewards
        for (ItemReward reward : rewards) {
            if (reward instanceof MultiplierReward) {
                // Multipliers cannot be auto used
                toExchange.add(reward);
                continue;
            }
            if (reward instanceof BombReward) {
                // Bombs cannot be auto used
                toExchange.add(reward);
                continue;
            }
            if (reward instanceof AutoSellEssenceReward) {
                if (asEssence == 1) {
                    toExchange.add(reward);
                } else {
                    autoSellTime = autoSellTime.add(((AutoSellEssenceReward) reward).getTime());
                }
                continue;
            }
            if (reward instanceof AutoMinerChipReward) {
                if (amChips == 1) {
                    toExchange.add(reward);
                } else {
                    autoMinerTime = autoMinerTime.add(((AutoMinerChipReward) reward).getTime().multiply(BigMath.THOUSAND));
                }
                continue;
            }
            if (reward instanceof ScrollReward) {
                if (scrolls == 1) {
                    toExchange.add(reward);
                } else {
                    ScrollReward sr = (ScrollReward) reward;
                    Enchantment ench = sr.getEnchant();
                    int level = enchants.getOrDefault(ench, 0);
                    if (level < EnchantInfo.getMaxScrollLevel(ench)) { // Go up to maximum scroll level, not higher
                        enchants.put(ench, level + 1);
                    } else {
                        toExchange.add(reward);
                    }
                }
            }
        }
        // AS Essence
        PlayerData.addAutoSellTime(player, autoSellTime);

        // AM Chips
        AutoMinerUtils.getModel(player).addAutominerTime(autoMinerTime);

        // Scrolls
        Pickaxe pick = new Pickaxe(player);
        enchants.forEach(pick::setEnchantmentLevel);
        pick.applyPickaxe();

        // Exchange
        Economy.tokens.addBalance(player, Exchanger.autoExchange(toExchange).tokens);
    }
}
