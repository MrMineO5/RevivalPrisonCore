/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;

public class ShadyTraderEvents implements Listener {
    public Plugin plugin = null;

    ShadyTraderEvents() {

    }

    @EventHandler
    public void onEvent(PlayerInteractAtEntityEvent event) {
        event.getPlayer().sendMessage(event.getRightClicked().getName());
        if (event.getRightClicked().getName().equalsIgnoreCase("§c§lShady Trader")) {
            GUIManager.openGUI(event.getPlayer(), "shadytrader");
        }
    }
}
