/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SoundUtils {
    public static void playSound(Player player, Sound sound, String setting) {
        playSound(player, player.getLocation(), sound, setting);
    }

    public static void playSound(Player player, Location loc, Sound sound, String setting) {
        player.playSound(loc, sound, SoundCategory.MASTER, 100, 1);
    }

    public static void playSoundRadius(Location loc, int radius, Sound sound, String setting) {
        Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
        for (Entity e : nearbyEntities) {
            if (!e.getType().equals(EntityType.PLAYER))
                continue;
            playSound((Player) e, loc, sound, setting);
        }
    }
}
