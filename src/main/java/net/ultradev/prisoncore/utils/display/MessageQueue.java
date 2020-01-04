/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.display;

import net.ultradev.prisoncore.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageQueue {
    private static Map<UUID, List<String>> messages = new HashMap<>();

    public static void init() {
        FileConfiguration config = ConfigManager.getConfig("messages");
        for (String str : config.getKeys(false)) {
            messages.put(UUID.fromString(str), config.getStringList(str));
        }
    }

    public static void deinit() {
        FileConfiguration config = ConfigManager.getConfig("messages");
        messages.forEach((key, value) -> config.set(key.toString(), value));
        ConfigManager.setConfig(config, "messages");
    }

    public static void queueMessage(UUID id, String message) {
        List<String> strs = new ArrayList<>();
        if (messages.containsKey(id)) {
            strs = messages.get(id);
        }
        strs.add(message);
        messages.put(id, strs);
    }

    public static void executeQueue(@NotNull Player player) {
        if (!messages.containsKey(player.getUniqueId())) {
            return;
        }
        for (String str : messages.get(player.getUniqueId())) {
            player.sendMessage(str);
        }
    }
}
