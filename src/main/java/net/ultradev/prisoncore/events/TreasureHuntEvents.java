/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class TreasureHuntEvents implements Listener {
    public Plugin plugin = null;

    TreasureHuntEvents() {

    }

    @EventHandler
    public void onEvent(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Debugger.log("Checking chest if in thunt mine", "THuntEvents");
            Chest c = (Chest) event.getInventory().getHolder();
            Mine m = MineManager.getMineAt(c.getLocation());
            if (m == null) {
                return;
            }
            if (m.getName().equalsIgnoreCase("thunt")) {
                Debugger.log("Chest is in thunt mine, checking for emptiness", "THuntEvents");
                for (ItemStack item : event.getInventory().getContents()) {
                    Debugger.log("Checking slot...", "THuntEvents");
                    if (item != null) {
                        Debugger.log("Slot contains item, stopping...", "THuntEvents");
                        return;
                    }
                    Debugger.log("Slot is empty", "THuntEvents");
                }
                Debugger.log("Chest is empty, breaking!", "THuntEvents");
                c.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getWorld().getName().equalsIgnoreCase("spawn")) {
                Location loc = e.getClickedBlock().getLocation();
                if (loc.getBlockX() == -535 && loc.getBlockY() == 201 && loc.getBlockZ() == -387) {
                    GUIManager.openGUI(e.getPlayer(), "thunt");
                }
            }
        }
    }
}
