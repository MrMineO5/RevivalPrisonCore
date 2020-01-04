/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata;

import org.bukkit.entity.Player;

public enum StoreRank {
    NONE(0.0, "§7None", "§7None", 0),
    COAL(1.0, "Coal", "§8§lCoal", 30 * 60),
    IRON(1.5, "Iron", "§f§lIron", 30 * 60),
    GOLD(2.0, "Gold", "§e§lGold", 40 * 60),
    DIAMOND(2.5, "Diamond", "§b§lDiamond", 50 * 60),
    EMERALD(3.0, "Emerald", "§a§lEmerald", 60 * 60),
    OBSIDIAN(4.0, "Obsidian", "§5§lObsidian", 90 * 60),
    ULTRA(5.0, "Ultra", "§c§lUltra", 0);
    // TODO: Finish Autominer Bonuses

    private double multi;
    private String name;
    private String displayName;
    private int autominerBonus;

    StoreRank(double multi, String name, String displayName, int autominerBonus) {
        this.multi = multi;
        this.name = name;
        this.displayName = displayName;
        this.autominerBonus = autominerBonus;
    }

    public boolean has(Player p) {
        return (p.hasPermission("ultraprison." + name.toLowerCase()));
    }

    public static StoreRank getRank(Player p) {
        if (p.hasPermission("ultraprison.ultra")) {
            return ULTRA;
        }
        if (p.hasPermission("ultraprison.obsidian")) {
            return OBSIDIAN;
        }
        if (p.hasPermission("ultraprison.emerald")) {
            return EMERALD;
        }
        if (p.hasPermission("ultraprison.diamond")) {
            return DIAMOND;
        }
        if (p.hasPermission("ultraprison.gold")) {
            return GOLD;
        }
        if (p.hasPermission("ultraprison.iron")) {
            return IRON;
        }
        if (p.hasPermission("ultraprison.coal")) {
            return COAL;
        }
        return NONE;
    }

    public double getMultiplier() {
        return this.multi;
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

    public int getAutoMinerBonus() {
        return this.autominerBonus;
    }
}
