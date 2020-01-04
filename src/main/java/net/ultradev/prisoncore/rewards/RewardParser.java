/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rewards;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;


public class RewardParser {
    @NotNull
    private static String[] parseText(@NotNull String str) {
        return str.substring(0, str.length() - 1).split("[(,]");
    }

    @Nullable
    public static Reward getReward(String text) {
        try {
            String[] strs = parseText(text);
            Class<?> clazz = Class.forName("net.ultradev.prisoncore.rewards.rewards." + strs[0] + "Reward");
            Constructor<?> constructor = clazz.getConstructor(String[].class);
            Object obj = constructor.newInstance(new Object[]{strs});
            return (Reward) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
