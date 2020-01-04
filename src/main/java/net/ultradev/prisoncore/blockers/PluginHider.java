/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.blockers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.protocol.Protocol;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PluginHider implements Listener {
    private static List<String> blockedCommands = new ArrayList<>();
    private static List<String> pluginCommands = new ArrayList<>();
    private static Map<String, String> replacementMap = new HashMap<>();

    static {
        blockedCommands.add("/ver");
        blockedCommands.add("/bukkit:ver");
        blockedCommands.add("/version");
        blockedCommands.add("/bukkit:version");
        blockedCommands.add("/about");
        blockedCommands.add("/bukkit:about");
        blockedCommands.add("/help");
        blockedCommands.add("/bukkit:help");
        blockedCommands.add("/?");
        blockedCommands.add("/bukkit:?");
        pluginCommands.add("/pl");
        pluginCommands.add("/plugins");
        pluginCommands.add("/bukkit:pl");
        pluginCommands.add("/bukkit:plugins");

        replacementMap.put("0", "０");
        replacementMap.put("1", "１");
        replacementMap.put("2", "２");
        replacementMap.put("3", "３");
        replacementMap.put("4", "４");
        replacementMap.put("5", "５");
        replacementMap.put("6", "６");
        replacementMap.put("7", "７");
        replacementMap.put("8", "８");
        replacementMap.put("9", "９");
    }

    private Main plugin;

    public PluginHider(Main plugin) {
        this.plugin = plugin;
    }

    private static String applyChanges(String json) {
        AtomicReference<String> copy = new AtomicReference<>(json);
        replacementMap.forEach((key, val) ->
                copy.set(copy.get().replaceAll(key, val)));
        System.out.println("Applied changes: " + copy.get());
        return copy.get();
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        Protocol.addListener(new PacketAdapter(this.plugin, PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer cont = event.getPacket();
                String str = cont.getStrings().read(0);
                if (str.startsWith("/") && !str.contains(" "))
                    event.setCancelled(true);
                for (String s : blockedCommands) {
                    if (str.startsWith(s))
                        event.setCancelled(true);
                }
            }
        });
        Protocol.addListener(new PacketAdapter(this.plugin, PacketType.Play.Client.CHAT) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer cont = event.getPacket();
                String str = cont.getStrings().read(0);
                if (str.startsWith("/aacadditionpro") || str.startsWith("/aacap") || (str.startsWith("/aacadmin") && !event.getPlayer().hasPermission("AAC.admin")) || (str.startsWith("/matrix") && !event.getPlayer().hasPermission("ultraprison.owner"))) {
                    Synchronizer.synchronize(() -> event.getPlayer().performCommand("thisisaninvalidcommandpleasedontdothis"));
                    event.setCancelled(true);
                }
            }
        });
        Protocol.addListener(new PacketAdapter(this.plugin, PacketType.Play.Server.CHAT) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer cont = event.getPacket();
                WrappedChatComponent comp = cont.getChatComponents().read(0);
                if (comp == null)
                    return;
                String str = comp.getJson();
                if (str.equals("{\"extra\":[{\"color\":\"gray\",\"text\":\"[\"},{\"bold\":true,\"color\":\"red\",\"text\":\"Anti\"},{\"bold\":true,\"color\":\"white\",\"text\":\"Cheat\"},{\"color\":\"gray\",\"text\":\"] \"},{\"color\":\"gray\",\"text\":\"AAC 3.6.4: Haxor rekker (~konsolas)\"}],\"text\":\"\"}"))
                    event.setCancelled(true);
                if (str.equals("{\"extra\":[{\"color\":\"gray\",\"text\":\"[\"},{\"bold\":true,\"color\":\"red\",\"text\":\"Anti\"},{\"bold\":true,\"color\":\"white\",\"text\":\"Cheat\"},{\"color\":\"gray\",\"text\":\"] \"},{\"color\":\"gray\",\"text\":\"ID: aac90e6e8e5f6f14401c27bdffc5b646315/9581003\"}],\"text\":\"\"}")) {
                    Synchronizer.synchronize(() -> event.getPlayer().performCommand("thisisaninvalidcommandpleasedontdothis"));
                    event.setCancelled(true);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmd(PlayerCommandPreprocessEvent e) {
        String str = e.getMessage().toLowerCase();
        if (pluginCommands.contains(str) && !e.getPlayer().hasPermission("anitcheat.bypass")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cNope.");
            return;
        }
        if (str.equalsIgnoreCase("/help"))
            return;
        for (String s : blockedCommands) {
            if (str.equalsIgnoreCase(s) || str.startsWith(s + " ")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cNope.");
            }
        }
    }
}
