/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.plugins;

import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HoloUtils {
    public static HologramManager holoManager = null;

    public static void init() {
        holoManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();
    }
}
