/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.time;

import net.ultradev.prisoncore.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtils {
    private static Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    private static Map<String, Long> getCooldowns(UUID str) {
        if (!cooldowns.containsKey(str)) {
            return new HashMap<>();
        }
        return cooldowns.get(str);
    }

    private static void setCooldowns(UUID str, Map<String, Long> cds) {
        cooldowns.put(str, cds);
    }

    /**
     * Sets a cooldown for the player
     *
     * @param player   Player to set the cooldown for
     * @param str      Name of the cooldown
     * @param cooldown Cooldown time in seconds
     */
    public static void setCooldown(@NotNull Player player, String str, int cooldown) {
        long timestamp = DateUtils.getMilliTimeStamp() + 1000 * cooldown;
        Map<String, Long> ts = getCooldowns(player.getUniqueId());
        ts.put(str, timestamp);
        setCooldowns(player.getUniqueId(), ts);
    }

    /**
     * Checks whether a player's cooldown has expired
     *
     * @param player Player to check
     * @param str    Name of the cooldown
     * @return Whether cooldown has expired
     */
    public static boolean isCooldown(@NotNull Player player, String str) {
        Map<String, Long> ts = getCooldowns(player.getUniqueId());
        if (!ts.containsKey(str))
            return true;
        return (ts.get(str) < DateUtils.getMilliTimeStamp());
    }

    public static long getCooldownTime(@NotNull Player player, String str) {
        Map<String, Long> ts = getCooldowns(player.getUniqueId());
        if (!ts.containsKey(str))
            return 0;
        return ts.get(str) - DateUtils.getMilliTimeStamp();
    }

    public static void saveCooldowns() {
        for (UUID player : cooldowns.keySet()) {
            saveCooldown(Bukkit.getOfflinePlayer(player));
        }
    }

    public static void saveCooldown(OfflinePlayer pl) {
        Map<String, Long> cds = getCooldowns(pl.getUniqueId());
        if (cds.isEmpty()) {
            return;
        }
        FileConfiguration conf = PlayerData.getPlayerData(pl);
        for (Map.Entry<String, Long> entry : cds.entrySet()) {
            conf.set("cooldowns." + entry.getKey(), entry.getValue());
        }
        PlayerData.savePlayerData(pl);
    }

    public static void loadCooldowns() {
        cooldowns = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadCooldown(player);
        }
    }

    public static void loadCooldown(OfflinePlayer pl) {

    }
}
