/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pets;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum PetType {
    EXCHANGER, SELL, MERCHANT, AUTOSELL, FORTUNE, LIGHTNING;

    @NotNull
    @Contract(pure = true)
    public String defaultName() {
        switch (this) {
            case EXCHANGER:
                return "§aExchanger Pet";
            case SELL:
                return "§aSell Pet";
            case MERCHANT:
                return "§aMerchant Pet";
            case AUTOSELL:
                return "§aAutoSell Pet";
            case FORTUNE:
                return "§aFortune Pet";
            case LIGHTNING:
                return "§aLightning Pet";
        }
        return "§4Error Pet";
    }

    @Contract(pure = true)
    public int getMaxLevel() {
        switch (this) {
            case EXCHANGER:
            case SELL:
            case MERCHANT:
            case FORTUNE:
            case LIGHTNING:
                return 5;
            case AUTOSELL:
                return 1;
        }
        return 0;
    }

    @Contract(pure = true)
    public int getRequiredXp(int level) {
        switch (this) {
            case EXCHANGER:
                switch (level) {
                    case 1:
                        return 15000;
                    case 2:
                        return 25000;
                    case 3:
                        return 50000;
                    case 4:
                        return 100000;
                }
                return 100000;
            case SELL:
                switch (level) {
                    case 1:
                        return 200000;
                    case 2:
                        return 500000;
                    case 3:
                        return 1000000;
                    case 4:
                        return 2000000;
                }
                return 5000000;
            case AUTOSELL:
                return 100000000;
            case FORTUNE:
            case LIGHTNING:
                switch (level) {
                    case 1:
                        return 50000;
                    case 2:
                        return 250000;
                    case 3:
                        return 500000;
                    case 4:
                        return 1000000;
                }
                return 10000000;
            case MERCHANT:
                return 25000000 * level;
        }
        return -1;
    }

    @Contract(pure = true)
    @NotNull
    public Material getMaterial() {
        switch (this) {
            case EXCHANGER:
                return Material.HOPPER;
            case SELL:
                return Material.GOLD_INGOT;
            case AUTOSELL:
                return Material.MONSTER_EGG;
            case MERCHANT:
                return Material.NAME_TAG;
            case LIGHTNING:
                return Material.NETHER_STAR;
            case FORTUNE:
                return Material.DIAMOND_BLOCK;
        }
        return Material.AIR;
    }

    @NotNull
    public List<String> getDescription() {
        List<String> ret = new ArrayList<>();
        switch (this) {
            case EXCHANGER:
                ret.add("§7Exchanges items from crates");
                ret.add("§7Right click for settings");
                ret.add("§7Open crates to level up!");
                break;
            case SELL:
                ret.add("§7Get more tokens from mining!");
                ret.add("§7Sell blocks to level up!");
                break;
            case MERCHANT:
                ret.add("§7Gives you a chance of doubling your sell!");
                ret.add("§7Sell blocks to level up!");
                break;
            case LIGHTNING:
                ret.add("§7Gives lightning extra chance to activate!");
                break;
            case AUTOSELL:
                ret.add("§7Never run out of AutoSell time!");
                break;
            case FORTUNE:
                ret.add("§7Gives more blocks when mining!");
                ret.add("§7Mine blocks to level up!");
                break;
        }
        return ret;
    }

    @Contract(pure = true)
    public boolean hasGUI() {
        switch (this) {
            case EXCHANGER:
                return true;
        }
        return false;
    }

    @Contract(pure = true)
    public PetXp.XpType getXpType() {
        switch (this) {
            case EXCHANGER:
                return PetXp.XpType.CRATES_OPENED;
            case SELL:
                return PetXp.XpType.BLOCKS_SOLD;
            case FORTUNE:
            case LIGHTNING:
                return PetXp.XpType.MINING;
            case MERCHANT:
                return PetXp.XpType.TOKENS_EARNED;
        }
        return PetXp.XpType.MINING;
    }
}
