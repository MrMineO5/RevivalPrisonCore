/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {
    public static void reportError(Exception error) {
        Bukkit.getServer().getLogger().log(Level.SEVERE, "An error has occured, please report to UltraDev");
        Bukkit.getServer().getLogger().log(Level.INFO, "---------------[ ERROR ]---------------");
        error.printStackTrace();
        Bukkit.getServer().getLogger().log(Level.INFO, "---------------[ ERROR ]---------------");
    }
}
