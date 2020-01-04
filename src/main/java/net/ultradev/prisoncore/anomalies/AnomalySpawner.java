/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.anomalies;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AnomalySpawner {
    public static ItemStack generateItem(AnomalyType type) {
        return new ItemFactory(Material.SKULL_ITEM, 1, (short) 1)
                .setName("§cAnomaly Spawner")
                .setLore(
                        "§7Spawn an anomaly at the",
                        "§7Mine you're stood within!",
                        "",
                        "§7Type: §4§l" + type.getName().toUpperCase()
                )
                .addNBT("type", "anomalySpawner")
                .addNBT("tier", type.name())
                .create();
    }
}
