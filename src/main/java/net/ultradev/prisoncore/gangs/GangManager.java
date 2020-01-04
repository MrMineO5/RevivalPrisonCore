/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.gangs;

import net.ultradev.prisoncore.ConfigManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GangManager {
    private static Map<String, Gang> gangs = new HashMap<>();

    public static void init() {
        FileConfiguration config = ConfigManager.getConfig("gangs");
        for (String str : config.getKeys(false)) {
            gangs.put(str, Gang.loadGang(config.getConfigurationSection(str)));
        }
    }

    public static void deinit() {
        FileConfiguration config = ConfigManager.getConfig("gangs");
        gangs.forEach((name, gang) -> {
            ConfigurationSection sec = config.contains(name) ?
                    config.getConfigurationSection(name) :
                    config.createSection(name);
            gang.save(sec);
            // TODO: Test if this works, if it doesn't, add the following code:
            //   config.set(name, sec);
        });
        ConfigManager.setConfig(config, "gangs");
    }

    public static boolean createGang(UUID id, @NotNull String name) {
        if (gangs.containsKey(name.toLowerCase())) {
            return false;
        }
        gangs.put(name.toLowerCase(), new Gang(name, id));
        return true;
    }

    @Nullable
    public static Gang getGang(String gang) {
        if (gang == null) {
            return null;
        }
        if (!gangs.containsKey(gang.toLowerCase())) {
            return null;
        }
        return gangs.get(gang.toLowerCase());
    }

    @Contract("null, _ -> false")
    public static boolean invite(Gang gang, UUID id) {
        if (gang == null) {
            return false;
        }
        gang.addInvite(id);
        return true;
    }

    public static boolean invite(String gang, UUID id) {
        return invite(getGang(gang), id);
    }

    @Contract("null, _ -> false")
    public static boolean uninvite(Gang gang, UUID id) {
        if (gang == null) {
            return false;
        }
        gang.removeInvite(id);
        return true;
    }

    public static boolean uninvite(String gang, UUID id) {
        return uninvite(getGang(gang), id);
    }

    public static boolean join(@NotNull Player player, @NotNull Gang g) {
        if (g.isInvited(player.getUniqueId())) {
            g.removeInvite(player.getUniqueId());
            g.addMember(player.getUniqueId());
            PlayerData.setGang(player, g.getName());
            return true;
        }
        return false;
    }

    public static boolean join(Player player, String gang) {
        Gang g = getGang(gang);
        if (g == null) {
            return false;
        }
        return join(player, g);
    }
}
