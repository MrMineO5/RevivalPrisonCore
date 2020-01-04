/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.treasurehunt;

import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.shadytrader.MysteriousCrystal;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LootGenerator {
    private static LinkedHashMap<ItemStack, Integer> rewards = new LinkedHashMap<>();

    static {
        rewards.put(MysteriousCrystal.getItem(5), 3);
        rewards.put(MysteriousCrystal.getItem(2), 7);
        rewards.put(MysteriousCrystal.getItem(1), 15);
        rewards.put(Objects.requireNonNull(CrateManager.getCrate("legendary")).getKey(1), 50);
        rewards.put(Objects.requireNonNull(CrateManager.getCrate("legendary")).getKey(5), 15);
        rewards.put(Objects.requireNonNull(CrateManager.getCrate("mythical")).getKey(5), 7);
        rewards.put(Bombs.generateBombItem(7, 3), 20);
        rewards.put(Bombs.generateBombItem(5, 5), 100);
    }

    public static ItemStack[] generateLootChest() {
        ItemStack[] ret = new ItemStack[27];
        int itemcount = MathUtils.weightedRandom(2, 5, 2);
        for (int i = 0; i < itemcount; i++) {
            ret[MathUtils.random(0, 26)] = generateItem();
        }
        return ret;
    }

    private static ItemStack generateItem() {
        int total = rewards.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = MathUtils.random(1, total);
        int curint = 0;
        for (Map.Entry<ItemStack, Integer> set : rewards.entrySet()) {
            curint += set.getValue();
            if (randomValue <= curint) {
                return set.getKey();
            }
        }
        return null;
    }
}
