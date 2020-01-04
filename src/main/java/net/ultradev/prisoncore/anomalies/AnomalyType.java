/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.anomalies;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum AnomalyType {
    COMMON, RARE, EPIC, LEGENDARY;

    @Contract(pure = true)
    public double getHealth() {
        switch (this) {
            case COMMON:
                return 500;
            case RARE:
                return 750;
            case EPIC:
                return 1000;
            case LEGENDARY:
                return 1500;
        }
        return 0;
    }

    @NotNull
    @Contract(pure = true)
    public String getName() {
        switch (this) {
            case COMMON:
                return "Common Anomaly";
            case RARE:
                return "Rare Anomaly";
            case EPIC:
                return "Epic Anomaly";
            case LEGENDARY:
                return "Legendary Anomaly";
        }
        return "Error Anomaly";
    }

    @NotNull
    public String getCustomName(double health) {
        String name;
        switch (this) {
            case COMMON:
                name = "§b§lCommon Anomaly";
                break;
            case RARE:
                name = "§6§lRare Anomaly";
                break;
            case EPIC:
                name = "§5§lEpic Anomaly";
                break;
            case LEGENDARY:
                name = "§c§lLegendary Anomaly";
                break;
            default:
                name = "§b§lAnomaly";
                break;
        }
        return name + " §7[§c" + AnomalyManager.getPercentage(health, getHealth()) + "%§7]";
    }
}
