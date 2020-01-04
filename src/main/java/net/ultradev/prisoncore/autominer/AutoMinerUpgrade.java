/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum AutoMinerUpgrade {
    MORE_TOKENS, MORE_KEYS, BETTER_KEYS, MORE_DUST, LESS_TIME;

    @Contract(pure = true)
    public long getPrice(int level) {
        // TODO: Create pricing functions
        return 1000;
    }

    @NotNull
    public List<String> getDescription() {
        List<String> ret = new ArrayList<>();
        switch (this) {
            case MORE_KEYS:
                ret.add("§7Find larger amounts of");
                ret.add("§7Crate Keys");
                break;
            case BETTER_KEYS:
                ret.add("§7Find better quality");
                ret.add("§7Crate Keys");
                break;
            case MORE_DUST:
                ret.add("§7Find more Socket Gem");
                ret.add("§7Dust from blocks");
                break;
            case MORE_TOKENS:
                ret.add("§7Find more Tokens from blocks");
                break;
            case LESS_TIME:
                ret.add("§7Chance to consume less");
                ret.add("§7Auto Miner time");
                break;
        }
        return ret;
    }

    @NotNull
    @Contract(pure = true)
    public String getName() {
        switch (this) {
            case LESS_TIME:
                return "Less Time";
            case MORE_KEYS:
                return "More Keys";
            case BETTER_KEYS:
                return "Better Keys";
            case MORE_DUST:
                return "More Dust";
            case MORE_TOKENS:
                return "More Tokens";
        }
        return "ERROR";
    }

    @Contract(pure = true)
    public int getMaxLevel() {
        return 250;
    }
}
