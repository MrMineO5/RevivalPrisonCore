/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.crates;

import org.bukkit.configuration.ConfigurationSection;

public class PlacedCrate {
    private String holoId;
    private String crate;

    public PlacedCrate(String holoId, String crate) {
        this.holoId = holoId;
        this.crate = crate;
    }

    public PlacedCrate(ConfigurationSection sec) {
        this.holoId = sec.getString("holoId");
        this.crate = sec.getString("crate");
    }

    public String getHoloId() {
        return this.holoId;
    }

    public String getCrate() {
        return this.crate;
    }

    public void serialize(ConfigurationSection sec) {
        sec.set("holoId", holoId);
        sec.set("crate", crate);
    }
}
