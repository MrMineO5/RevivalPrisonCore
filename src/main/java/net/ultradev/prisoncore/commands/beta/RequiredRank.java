/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import net.ultradev.prisoncore.playerdata.StaffRank;
import net.ultradev.prisoncore.playerdata.StoreRank;
import org.bukkit.command.CommandSender;

public enum RequiredRank {
    NONE, COAL, IRON, GOLD, DIAMOND, EMERALD, OBSIDIAN, ULTRA, HELPER, MOD, ADMIN, OWNER;

    public boolean hasRank(CommandSender player) {
        if (this == NONE) {
            return true;
        }
        return player.hasPermission("ultraprison." + this.name().toLowerCase());
    }

    public String getPrefix() {
        switch (this) {
            case NONE:
                return "";
            case COAL:
            case IRON:
            case GOLD:
            case DIAMOND:
            case EMERALD:
            case OBSIDIAN:
            case ULTRA:
                return StoreRank.valueOf(this.name()).getPrefix();
            case HELPER:
            case MOD:
            case ADMIN:
            case OWNER:
                return StaffRank.valueOf(this.name()).getPrefix();
        }
        return "ERROR";
    }
}
