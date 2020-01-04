/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata.settings;

import net.ultradev.prisoncore.commands.beta.RequiredRank;

public class Setting {
    public String name;
    public String displayName;
    // public Material item;
    public boolean def;
    public String[] description;
    public RequiredRank requiredRank;

    public Setting(String name, String displayName, boolean def, RequiredRank rank, String... description) {
        this.name = name;
        this.displayName = displayName;
        // this.item = item;
        this.def = def;
        this.requiredRank = rank;
        this.description = description;
    }
}
