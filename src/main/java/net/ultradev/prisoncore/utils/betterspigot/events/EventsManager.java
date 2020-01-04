/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsManager implements Listener {
    public EventsManager(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void callEvent(Event e) {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        switch (e.getAction()) {
            case RIGHT_CLICK_AIR:
                PlayerRightClickAirEvent prcae = new PlayerRightClickAirEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(prcae);
                if (prcae.isCancelled()) {
                    e.setCancelled(true);
                }

                PlayerRightClickEvent prce = new PlayerRightClickEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(prce);
                if (prce.isCancelled()) {
                    e.setCancelled(true);
                }
                break;
            case RIGHT_CLICK_BLOCK:
                PlayerRightClickBlockEvent prcbe = new PlayerRightClickBlockEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(prcbe);
                if (prcbe.isCancelled()) {
                    e.setCancelled(true);
                }

                prce = new PlayerRightClickEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(prce);
                if (prce.isCancelled()) {
                    e.setCancelled(true);
                }
                break;
            case LEFT_CLICK_AIR:
                PlayerLeftClickAirEvent plcae = new PlayerLeftClickAirEvent(e.getPlayer(), e.getAction(), e.getItem(), e.getHand());
                Bukkit.getPluginManager().callEvent(plcae);
                if (plcae.isCancelled()) {
                    e.setCancelled(true);
                }

                PlayerLeftClickEvent plce = new PlayerLeftClickEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(plce);
                if (plce.isCancelled()) {
                    e.setCancelled(true);
                }
                break;
            case LEFT_CLICK_BLOCK:
                PlayerLeftClickBlockEvent plcbe = new PlayerLeftClickBlockEvent(e.getPlayer(), e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace(), e.getHand());
                Bukkit.getPluginManager().callEvent(plcbe);
                if (plcbe.isCancelled()) {
                    e.setCancelled(true);
                }

                plce = new PlayerLeftClickEvent(e.getPlayer());
                Bukkit.getPluginManager().callEvent(plce);
                if (plce.isCancelled()) {
                    e.setCancelled(true);
                }
                break;
        }
    }
}
