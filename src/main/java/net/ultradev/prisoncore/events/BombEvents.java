/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BombEvents implements Listener {
    public Plugin plugin = null;

    BombEvents() {

    }

    @EventHandler
    public void onEvent(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            Location loc = p.getLocation();
            if (!p.hasPermission("ultraprison.admin")) {
                if (loc.getBlockZ() >= 1235 && loc.getBlockZ() <= 1486) {
                    if (loc.getBlockX() >= -527 && loc.getBlockX() <= -276) {
                        return;
                    }
                }
            }
            ItemStack item = p.getInventory().getItemInMainHand();
            if (Bombs.isBomb(item)) {
                event.setCancelled(true);
                int power = Bombs.getPower(item);
                int newcount = item.getAmount() - 1;
                ItemStack i2 = item.clone();
                i2.setAmount(newcount);
                p.getInventory().setItemInMainHand(i2);
                Bombs.spawnBomb(p, power);
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        String name = e.getItem().getCustomName();
        if (HiddenStringUtils.hasHiddenString(name)) {
            if (HiddenStringUtils.extractHiddenString(name).equals("bomb")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (!e.getEntityType().equals(EntityType.DROPPED_ITEM)) {
            return;
        }
        Debugger.log("Checking if destroyed entity is bomb.", "bombEvents");
        String name = e.getEntity().getCustomName();
        Debugger.log("Name: " + name, "bombEvents");
        if (HiddenStringUtils.hasHiddenString(name)) {
            Debugger.log("Has hidden data", "bombEvents");
            if (HiddenStringUtils.extractHiddenString(name).equals("bomb")) {
                Debugger.log("Extracted data is valid", "bombEvents");
                e.setCancelled(true);
            }
        }
    }
}
