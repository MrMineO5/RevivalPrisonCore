/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SignGUI {

    private ProtocolManager protocolManager;
    private PacketAdapter packetListener;
    private Map<String, SignGUIListener> listeners;
    private Map<String, Vector> signLocations;

    public SignGUI(Plugin plugin) {
        protocolManager = ProtocolLibrary.getProtocolManager();
        packetListener = new PacketListener(plugin);
        protocolManager.addPacketListener(packetListener);
        listeners = new ConcurrentHashMap<>();
        signLocations = new ConcurrentHashMap<>();
    }

    public void open(Player player, SignGUIListener response) {
        open(player, null, response);
    }

    public void open(Player player, Location signLocation, SignGUIListener response) {
        int x = 0, y = 0, z = 0;
        if (signLocation != null) {
            x = signLocation.getBlockX();
            y = signLocation.getBlockY();
            z = signLocation.getBlockZ();
        }

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        packet.getBlockPositionModifier()
                .write(0, new BlockPosition(x, y, z));

        try {
            protocolManager.sendServerPacket(player, packet);
            signLocations.put(player.getName(), new Vector(x, y, z));
            listeners.put(player.getName(), response);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        protocolManager.removePacketListener(packetListener);
        listeners.clear();
        signLocations.clear();
    }

    public interface SignGUIListener {
        void onSignDone(Player player, String[] lines);
    }

    class PacketListener extends PacketAdapter {

        Plugin plugin;

        public PacketListener(Plugin plugin) {
            super(plugin, PacketType.Play.Client.UPDATE_SIGN);
            this.plugin = plugin;
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            final Player player = event.getPlayer();
            Vector v = signLocations.remove(player.getName());
            if (v == null) return;
            BlockPosition block = event.getPacket().getBlockPositionModifier().getValues().get(0);
            if (block.getX() != v.getBlockX()) return;
            if (block.getY() != v.getBlockY()) return;
            if (block.getZ() != v.getBlockZ()) return;

            final String[] lines = event.getPacket().getStringArrays().getValues().get(0);
            final SignGUIListener response = listeners.remove(event.getPlayer().getName());
            if (response != null) {
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> response.onSignDone(player, lines));
            }
        }

    }

}