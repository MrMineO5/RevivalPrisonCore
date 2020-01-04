/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.mines;

import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class WandManager {
    public static HashMap<UUID, Location> pos1 = new HashMap<>();
    public static HashMap<UUID, Location> pos2 = new HashMap<>();

    public static boolean isWand(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return NBTUtils.getString(item, "type").equals("minewand");
    }

    public static void setPos1(Player player, Location location) {
        pos1.put(player.getUniqueId(), location);
    }

    public static void setPos2(Player player, Location location) {
        pos2.put(player.getUniqueId(), location);
    }

    public static boolean setPositions(String mine, Player player) {
        UUID uuid = player.getUniqueId();
        Mine m = MineManager.getMine(mine);
        if (m == null) {
            return false;
        }
        m.setLocations(pos1.get(uuid), pos2.get(uuid));
        return true;
    }
}
