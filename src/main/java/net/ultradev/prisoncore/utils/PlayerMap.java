/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import net.ultradev.prisoncore.ConfigManager;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class
PlayerMap implements Listener {
    private static Map<String, UUID> playerMap = new HashMap<>();

    public static void init(Main plugin) {
        Bukkit.getPluginManager().registerEvents(new PlayerMap(), plugin);
        load();
        Scheduler.scheduleSyncRepeatingTask(PlayerMap::save, 10, 120);
    }

    private static void load() {
        FileConfiguration config = ConfigManager.getConfig("playermap");
        for (String str : config.getKeys(false)) {
            playerMap.put(str.toLowerCase(), UUID.fromString(config.getString(str)));
        }
    }

    public static void deinit() {
        save();
    }

    public static void save() {
        FileConfiguration config = ConfigManager.getConfig("playermap");
        playerMap.forEach((name, uuid) -> {
            config.set(name, uuid.toString());
        });
        ConfigManager.setConfig(config, "playermap");
    }

    @Nullable
    public static OfflinePlayer getPlayer(@NotNull String name) {
        Debugger.log("Getting player by name: " + name, "playerMap");
        if (!playerMap.containsKey(name.toLowerCase())) {
            Debugger.log("Player does not exist.", "playerMap");
            return Bukkit.getOfflinePlayer(name);
        }
        Debugger.log("Getting player from database!", "playerMap");
        return Bukkit.getOfflinePlayer(playerMap.get(name.toLowerCase()));
    }

    @Nullable
    public static UUID getUniqueId(@NotNull String name) {
        if (!playerMap.containsKey(name.toLowerCase())) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            if (p == null) {
                return null;
            }
            return p.getUniqueId();
        }
        return playerMap.get(name.toLowerCase());
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        playerMap.put(player.getName().toLowerCase(), player.getUniqueId());
    }
}
