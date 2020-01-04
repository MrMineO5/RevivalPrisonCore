/*
 * Copyright (c) 2020. UltraDev
 */

package net.ultradev.prisoncore.utils.configuration;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {
    public static Map<String, Object> serialize(Location loc) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("world", loc.getWorld().getName());
        ret.put("vec", loc.toVector());
        return ret;
    }
}
