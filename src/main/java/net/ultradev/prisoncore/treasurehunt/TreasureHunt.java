/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.treasurehunt;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreasureHunt {
    public static int titleCount;
    private static long countdown = -1;
    private static long lastUpdate;
    private static List<Integer> alertTimes = new ArrayList<>();
    private static List<Integer> alertsToGo;
    private static int task = -1;
    private static int runId;

    static {
        alertTimes.add(1800);
        alertTimes.add(600);
        alertTimes.add(300);
        alertTimes.add(10);
        alertTimes.add(5);
        alertTimes.add(4);
        alertTimes.add(3);
        alertTimes.add(2);
        alertTimes.add(1);
    }

    private static String alertToString(int i) {
        if (i > 60) {
            int j = Math.floorDiv(i, 60);
            if (j == 1) {
                return "§dThe Treasure Hunt will end in 1 minute!";
            }
            return "§dThe Treasure Hunt will end in " + j + " minutes!";
        } else {
            if (i == 1) {
                return "§dThe Treasure Hunt will end in 1 second!";
            }
            return "§dThe Treasure Hunt will end in " + i + " seconds!";
        }
    }

    private static void runTitles() {
        final String[] possibleColors = {"§b", "§a", "§e", "§d", "§c"};
        runId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            String color = possibleColors[MathUtils.random(0, possibleColors.length - 1)];
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(color + "Treasure Hunt Active!", "§eType /spawn!", 0, 5, 0);
            }
            titleCount++;
            if (titleCount >= 20) {
                stopTitles();
            }
        }, 0, 1);
    }

    private static void stopTitles() {
        Bukkit.getScheduler().cancelTask(runId);
    }

    public static void start(long time) {
        if (countdown != -1) {
            return;
        }
        runTitles();
        Debugger.log("Starting treasure hunt", "thunt");
        Portal.open();
        countdown = time * 1000;
        Debugger.log("Starting treasure hunt timer", "thunt");
        startTimer();
    }

    private static void startTimer() {
        lastUpdate = DateUtils.getMilliTimeStamp();
        alertsToGo = alertTimes.stream()
                .filter(a -> a * 1000 <= countdown)
                .collect(Collectors.toList());
        task = Scheduler.scheduleSyncRepeatingTask(TreasureHunt::updateTimer, 1, 1);
    }

    private static void updateTimer() {
        countdown -= DateUtils.getMilliTimeStamp() - lastUpdate;
        lastUpdate = DateUtils.getMilliTimeStamp();
        long secs = (long) Math.floor(countdown);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§dTreasure Hunt: §e" + DateUtils.convertTime(secs / 1000L)));
        }
        updateAlerts();
        if (countdown <= 0) {
            stop();
        }
    }

    private static void updateAlerts() {
        if (alertsToGo.isEmpty()) {
            return;
        }
        int i = alertsToGo.get(0);
        if (countdown > i * 1000) {
            return;
        }
        Bukkit.broadcastMessage(alertToString(i));
        alertsToGo.remove(0);
        updateAlerts();
    }

    private static void stopTimer() {
        if (task == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(task);
        task = -1;
    }

    public static void stop() {
        stopTimer();
        if (countdown == -1) {
            return;
        }
        Portal.close();
        countdown = -1;
        World w = Bukkit.getWorld("Mines");
        for (Player player : w.getPlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockZ() >= 1235 && loc.getBlockZ() <= 1486) {
                if (loc.getBlockX() >= -527 && loc.getBlockX() <= -276) {
                    player.performCommand("spawn");
                }
            }
        }
    }

    public static void extend(int time) {
        countdown += time * 1000;
    }

    public static boolean isActive() {
        return countdown != -1;
    }

    public static class Portal {
        private static boolean open = false;

        static void open() {
            if (open) {
                return;
            }
            Debugger.log("Opening portal", "thunt_portal");
            setRegion("spawn", -538, 199, -388, -532, 199, -388, Material.GLOWSTONE);
            setRegion("spawn", -538, 209, -388, -532, 209, -388, Material.GLOWSTONE);
            setRegion("spawn", -532, 200, -388, -532, 208, -388, Material.GLOWSTONE);
            setRegion("spawn", -538, 200, -388, -538, 208, -388, Material.GLOWSTONE);
            setRegion("spawn", -537, 200, -388, -533, 208, -388, Material.PORTAL);
            open = true;
            Debugger.log("Portal Open", "thunt_portal");
        }

        static void close() {
            if (!open) {
                return;
            }
            setRegion("spawn", -538, 199, -388, -532, 209, -388, Material.AIR);
            open = false;
        }

        public static boolean isOpen() {
            return open;
        }

        private static void setRegion(String world, int x1, int y1, int z1, int x2, int y2, int z2, Material mat) {
            Debugger.log("Filling region in world " + world + " (" + x1 + ", " + y1 + ", " + z1 + "), (" + x2 + ", " + y2 + ", " + z2 + ") with " + mat.toString(), "thunt_portal");
            World w = Bukkit.getWorld(world);
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        w.getBlockAt(x, y, z).setType(mat, false);
                    }
                }
            }
            /*EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(world)).build();
            Vector min = new Vector(x1, y1, z1);
            Vector max = new Vector(x2, y2, z2);
            try {
                editSession.setBlocks(new CuboidRegion(min, max), new SingleBlockPattern(new BaseBlock(Material.PORTAL.getId())));
                editSession.flushQueue();
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }*/
        }
    }
}
