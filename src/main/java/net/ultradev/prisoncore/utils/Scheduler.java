/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import net.ultradev.prisoncore.Main;
import org.bukkit.Bukkit;

public class Scheduler {
    /**
     * Runs the runnable after delay seconds
     *
     * @param run   - The runnable to run
     * @param delay - The delay in seconds
     * @return The Task ID for the task created
     */
    public static int scheduleSyncDelayedTask(Runnable run, int delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), run, delay * 20);
    }

    /**
     * Runs the runnable after delay seconds
     *
     * @param run    - The runnable to run
     * @param delay  - The delay in seconds
     * @param repeat - The delay between repeats in seconds
     * @return The Task ID for the task created
     */
    public static int scheduleSyncRepeatingTask(Runnable run, int delay, int repeat) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), run, delay * 20, repeat * 20);
    }

    public static int scheduleSyncDelayedTaskT(Runnable run, int delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), run, delay);
    }

    public static int scheduleSyncRepeatingTaskT(Runnable run, int delay, int repeat) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), run, delay, repeat);
    }
}
