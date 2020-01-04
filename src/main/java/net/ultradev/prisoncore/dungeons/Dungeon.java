/*
 * Copyright (c) 2020. UltraDev
 */

package net.ultradev.prisoncore.dungeons;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;

@Data
public abstract class Dungeon {
    private String name;
    private String displayName;
    private Material mat;
    private Location spawnPoint;

    public abstract void spawnMobs();
    public abstract void update();
    public abstract void runAbility();
    public abstract void end();
}