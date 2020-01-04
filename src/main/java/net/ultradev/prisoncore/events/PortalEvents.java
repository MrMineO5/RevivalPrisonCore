/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.riddle.PortalRiddle;
import net.ultradev.prisoncore.treasurehunt.TreasureHunt;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PortalEvents implements Listener {
    private static boolean isInPortal(@NotNull Location loc, int x1, int y1, int z1, int x2, int y2, int z2) {
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        return (x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2);
    }

    @EventHandler
    public void onMove(@NotNull PlayerMoveEvent event) {
        if (event.getFrom().distance(event.getTo()) == 0) {
            return;
        }
        Player p = event.getPlayer();
        Location to = event.getTo();
        if (PortalRiddle.isOpen()) {
            if (isInPortal(to, -543, 165, -355, -541, 168, -355)) {
                p.teleport(PortalRiddle.getDestination());
            }
        }
        if (TreasureHunt.Portal.isOpen()) {
            if (isInPortal(to, -536, 200, -388, -533, 208, -388)) {
                p.teleport(Objects.requireNonNull(MineManager.getMine("thunt")).getTpPos());
            }
        }
    }
}
