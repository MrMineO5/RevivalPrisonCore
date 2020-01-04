/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Debugger {
    public static Map<String, List<Player>> debugged = new HashMap<>();
    public static boolean debug_console = false;
    public static List<Player> debugged_all = new ArrayList<>();

    public static void addPlayer(Player player, String str) {
        if (!debugged.containsKey(str)) {
            debugged.put(str, new ArrayList<>());
        }
        List<Player> pls = debugged.get(str);
        pls.add(player);
    }

    public static void removePlayer(Player player, String str) {
        List<Player> pls = debugged.get(str);
        pls.remove(player);
    }

    public static void log(String str, String clazz) {
        if (debug_console) {
            System.out.println("[Debug] " + str);
        }
        if (!debugged_all.isEmpty()) {
            for (Player p : debugged_all) {
                p.sendMessage("§8[§bDebug§8] §7" + str);
            }
        }
        if (!debugged.containsKey(clazz)) {
            return;
        }
        for (Player p : debugged.get(clazz)) {
            p.sendMessage("§8[§bDebug§8] (§a" + clazz + "§8) §7" + str);
        }
    }

    public static void error(String str, String clazz) {
        System.out.println("[Error] " + str);
        if (!debugged_all.isEmpty()) {
            for (Player p : debugged_all) {
                p.sendMessage("§8[§bDebug§8] §cError: §7" + str);
            }
        }
        if (!debugged.containsKey(clazz)) {
            return;
        }
        for (Player p : debugged.get(clazz)) {
            p.sendMessage("§8[§bDebug§8] (§a" + clazz + "§8) §7" + str);
        }
    }
}
