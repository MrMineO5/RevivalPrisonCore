/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata;

import org.bukkit.entity.Player;

public enum StaffRank {
    NONE("§7None", "§7None"),
    HELPER("Helper", "§6§lHelper"),
    MOD("Mod", "§6§lMod"),
    ADMIN("Admin", "§4§lAdmin"),
    OWNER("Owner", "§4§lOwner");

    private String name;
    private String displayName;

    StaffRank(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public static StaffRank getRank(Player p) {
        if (p.hasPermission("ultraprison.owner")) {
            return OWNER;
        }
        if (p.hasPermission("ultraprison.admin")) {
            return ADMIN;
        }
        if (p.hasPermission("ultraprison.mod")) {
            return MOD;
        }
        if (p.hasPermission("ultraprison.helper")) {
            return HELPER;
        }
        return NONE;
    }

    @Override
    public String toString() {
        return getPrefix();
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPrefix() {
        String pre = getDisplayName();
        if (pre.equals("§7None")) {
            return pre;
        }
        return "§7[" + pre + "§7]";
    }
}
