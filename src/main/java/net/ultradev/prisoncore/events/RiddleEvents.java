/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.riddle.PortalRiddle;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public class RiddleEvents implements Listener {
    public Plugin plugin = null;

    RiddleEvents() {

    }

    @EventHandler
    public void onEvent(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        EquipmentSlot e = event.getHand();
        if (!e.equals(EquipmentSlot.HAND)) {
            return;
        }
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();
        Debugger.log("Checking world...", "riddle_portal");
        if (b.getWorld().getName().equalsIgnoreCase("spawn")) {
            Location loc = b.getLocation();
            Debugger.log("Checking x...", "riddle_portal");
            if (loc.getBlockX() != -546) {
                return;
            }
            if (loc.getBlockY() == 168 && loc.getBlockZ() == -357) {
                p.playSound(loc, Sound.BLOCK_NOTE_HARP, 100, 0.0F);
                Debugger.log("Adding d...", "riddle_portal");
                PortalRiddle.makeAttempt(p, "d");
            }
            if (loc.getBlockY() == 166 && loc.getBlockZ() == -359) {
                p.playSound(loc, Sound.BLOCK_NOTE_HARP, 100, 1.0F);
                Debugger.log("Adding w...", "riddle_portal");
                PortalRiddle.makeAttempt(p, "w");
            }
            if (loc.getBlockY() == 167 && loc.getBlockZ() == -363) {
                p.playSound(loc, Sound.BLOCK_NOTE_HARP, 100, 1.5F);
                Debugger.log("Adding g...", "riddle_portal");
                PortalRiddle.makeAttempt(p, "g");
            }
            if (loc.getBlockY() == 165 && loc.getBlockZ() == -364) {
                p.playSound(loc, Sound.BLOCK_NOTE_HARP, 100, 2.0F);
                Debugger.log("Adding b...", "riddle_portal");
                PortalRiddle.makeAttempt(p, "b");
            }
        }
    }
}
