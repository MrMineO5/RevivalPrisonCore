/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.gangs;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum GangRank {
    MEMBER(0), MOD(1), OWNER(99);

    int level;

    GangRank(int level) {
        this.level = level;
    }

    @NotNull
    @Contract(pure = true)
    public String getName() {
        switch (this) {
            case MEMBER:
                return "Member";
            case MOD:
                return "Mod";
            case OWNER:
                return "Owner";
        }
        return "";
    }
}
