/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.mines;

import net.ultradev.prisoncore.ConfigManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.Scheduler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineManager {
    public static HashMap<String, Mine> mines = new HashMap<>();

    private static void updateAll() {
        mines.values().forEach(Mine::update);
    }

    public static Mine getMine(String name) {
        if (mineExists(name)) {
            return mines.get(name.toLowerCase());
        }
        System.out.println("Tried to access non-existant mine: " + name);
        return null;
    }

    public static boolean createMine(String name) {
        if (mineExists(name)) {
            return false;
        }
        Mine mine = new Mine(name);
        mines.put(name.toLowerCase(), mine);
        return true;
    }

    public static boolean mineExists(String name) {
        return (mines.containsKey(name.toLowerCase()));
    }

    public static boolean setSpawnLocation(String m, Player player) {
        String mine = m.toLowerCase();
        if (!mineExists(mine)) {
            return false;
        }
        Mine mi = mines.get(mine);
        mi.setTpPos(player.getLocation());
        mines.put(mine, mi);
        return true;
    }

    public static boolean resetMine(String m) {
        String mine = m.toLowerCase();
        if (!mineExists(mine)) {
            return false;
        }
        Mine mi = mines.get(mine);
        mi.resetMine();
        return true;
    }

    public static void saveMines() {
        FileConfiguration config = ConfigManager.getConfig("mines");
        for (String str : mines.keySet()) {
            Mine m = mines.get(str);
            m.save(config);
        }
        ConfigManager.setConfig(config, "mines");
    }

    public static void loadMines() {
        mines.clear();
        FileConfiguration config = ConfigManager.getConfig("mines");
        if (!config.contains("mines")) {
            return;
        }
        config.getConfigurationSection("mines").getKeys(false).forEach(str -> {
            Mine mine = Mine.load(config, str);
            mines.put(str.toLowerCase(), mine);
        });
    }

    public static List<String> getMinesPage(int i) {
        int pages = (int) Math.ceil(mines.size() / 5);
        List<String> text = new ArrayList<>();
        text.add("§8§m--------§r §6§lMines (Page " + i + "/" + pages + ") §8§m--------");
        for (int a = (i - 1) * 5; a < i * 5; a++) {
            if (a < mines.size()) {
                text.add("§7- " + mines.keySet().toArray()[a]);
            }
        }
        return text;
    }

    public static boolean isInMine(Block block) {
        return (getMineAt(block.getLocation()) != null);
    }

    public static boolean isInMine(Location loc) {
        return (getMineAt(loc) != null);
    }

    public static boolean isInMineXZ(Block block) {
        return (getMineAtXZ(block.getLocation()) != null);
    }

    public static Mine getMineAt(Location loc) {
        for (Map.Entry<String, Mine> entr : mines.entrySet()) {
            if (entr.getValue().isInside(loc)) {
                return entr.getValue();
            }
        }
        return null;
    }

    public static String getMineAtXZ(Location loc) {
        for (Map.Entry<String, Mine> entr : mines.entrySet()) {
            Mine m = entr.getValue();
            if (loc.getX() >= m.getMinX() && loc.getX() <= m.getMaxX() && loc.getZ() >= m.getMinZ()
                    && loc.getZ() <= m.getMaxZ()) {
                return entr.getKey();
            }
        }
        return null;
    }

    public static boolean isInOutMine(Location loc) {
        return (getInOutMineAt(loc) != null);
    }

    public static String getInOutMineAt(Location loc) {
        for (Map.Entry<String, Mine> entr : mines.entrySet()) {
            if (entr.getValue().isInOutMine(loc)) {
                return entr.getKey();
            }
        }
        return null;
    }

    public static void startTimer() {
        Scheduler.scheduleSyncRepeatingTask(MineManager::updateAll, 1, 5);
    }

    /*
     * private static void resetMinesTimer() { Synchronizer.desynchronize(new
     * Runnable() {
     *
     * @Override public void run() { for (Mine mine : mines.values()) { if
     * (mine.positionsSet()) { if (mine.getEmptiness() > 50) {
     * Synchronizer.synchronize(() -> mine.resetMine()); } } } } }); }
     */

    public static boolean hasPermission(Player player, String mine) {
        if (RankupManager.getIdOf(mine) != -1) {
            return PlayerData.getRank(player) >= RankupManager.getIdOf(mine);
        }
        return player.hasPermission("ultraprison.rank." + mine);
    }
}
