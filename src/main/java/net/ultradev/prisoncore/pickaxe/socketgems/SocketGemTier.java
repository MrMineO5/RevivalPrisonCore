/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum SocketGemTier {
    COMMON, RARE, LEGENDARY, MYTHICAL, EPIC;

    @NotNull
    @Contract(pure = true)
    public String getDisplayName() {
        return getTierColor() + getName();
    }

    @NotNull
    @Contract(pure = true)
    public String getTierColor() {
        switch (this) {
            case COMMON:
                return "§b";
            case RARE:
                return "§d";
            case LEGENDARY:
                return "§c";
            case MYTHICAL:
                return "§3";
            case EPIC:
                return "§6";
            default:
                return null;
        }
    }

    public String getName() {
        switch (this) {
            case COMMON:
                return "Common";
            case RARE:
                return "Rare";
            case LEGENDARY:
                return "Legendary";
            case MYTHICAL:
                return "Mythical";
            case EPIC:
                return "Epic";
        }
        return null;
    }

    public int getPrice() {
        switch (this) {
            case COMMON:
                return 5;
            case RARE:
                return 25;
            case LEGENDARY:
                return 100;
            case MYTHICAL:
                return 300;
            case EPIC:
                return 1500;
        }
        return 1000;
    }
}
