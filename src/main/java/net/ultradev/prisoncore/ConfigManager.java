/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore;

import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigManager {
    private static HashMap<String, FileConfiguration> configs = new HashMap<>();

    static void init() {
        Main inst = Main.getInstance();
        File dataFolder = inst.getDataFolder();
        File configFolder = new File(dataFolder, "data");
        if (configFolder.mkdir()) {
            Debugger.log("Data folder created", "configmanager");
        }
        String[] temp = {"placedcrates", "mines", "cooldowns", "messages", "gangs", "playermap", "cratehoppers"};
        for (String str : temp) {
            File configFile = new File(configFolder, str + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
            configs.put(str, config);
        }
        initConfigs();
    }

    private static void initConfigs() {

    }

    public static FileConfiguration getConfig(String config) {
        return configs.get(config);
    }

    public static void setConfig(FileConfiguration config, String cfg) {
        configs.put(cfg, config);
        saveConfig(cfg);
    }

    private static void saveConfig(String cfg) {
        Main inst = Main.getInstance();
        FileConfiguration config = configs.get(cfg);
        File dataFolder = inst.getDataFolder();
        File configFolder = new File(dataFolder, "data");
        if (configFolder.mkdirs()) {
            Debugger.log("Data folder created", "configmanager");
        }
        File configFile = new File(configFolder, cfg + ".yml");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            config.save(configFile);
        } catch (IOException e) {
            System.out.println("Error while saving the config: " + cfg);
        }
    }

    public static void reloadConfig(String cfg) {
        Main inst = Main.getInstance();
        File dataFolder = inst.getDataFolder();
        File configFolder = new File(dataFolder, "data");
        if (configFolder.mkdirs()) {
            Debugger.log("Data folder created", "configmanager");
        }
        File configFile = new File(configFolder, cfg + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(cfg, config);
    }

    static void deInit() {
        deinitConfigs();
        Main inst = Main.getInstance();
        File dataFolder = inst.getDataFolder();
        File configFolder = new File(dataFolder, "data");
        if (configFolder.mkdirs()) {
            Debugger.log("Data folder created", "configmanager");
        }
        for (String str : configs.keySet()) {
            File configFile = new File(configFolder, str + ".yml");
            try {
                configs.get(str).save(configFile);
            } catch (IOException e) {
                System.out.println("Error while saving configs: " + e.getMessage());
            }
        }
    }

    private static void deinitConfigs() {

    }
}
