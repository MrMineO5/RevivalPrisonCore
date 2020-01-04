/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot;

import net.ultradev.prisoncore.utils.betterspigot.events.EventsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterSpigot {
    public static void init(JavaPlugin plugin) {
        new EventsManager(plugin);
    }

    public static void deinit(JavaPlugin plugin) {

    }
}
