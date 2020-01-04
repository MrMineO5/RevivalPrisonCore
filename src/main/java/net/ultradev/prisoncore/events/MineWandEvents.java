/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.mines.WandManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MineWandEvents implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!WandManager.isWand(item)) {
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        event.setCancelled(true);
        Block block = event.getClickedBlock();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            WandManager.setPos1(player, block.getLocation());
            player.sendMessage("§aPosition 1 set!");
        } else {
            WandManager.setPos2(player, block.getLocation());
            player.sendMessage("§aPosition 2 set!");
        }
    }
}
