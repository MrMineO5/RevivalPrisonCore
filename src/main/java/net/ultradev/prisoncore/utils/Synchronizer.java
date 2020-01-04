/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import net.ultradev.prisoncore.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Synchronizer {
    private static List<Runnable> taskQueue = new ArrayList<>();
    private static boolean running = false;
    private static boolean runningTask = false;

    public static int synchronize(Runnable run) {
        return Bukkit.getScheduler().runTask(Main.getInstance(), run).getTaskId();
    }

    public static int desynchronize(Runnable run) {
        return Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), run).getTaskId();
    }

    public static void queueTask(Runnable r) {
        taskQueue.add(r);
        if (!running) {
            runNextTask();
        }
    }

    public static void runNextTask() {
        if (runningTask) {
            return;
        }
        if (taskQueue.isEmpty()) {
            running = false;
            return;
        }
        running = true;
        runningTask = true;
        synchronize(() -> {
            taskQueue.get(0).run();
            desynchronize(() -> {
                runningTask = false;
                taskQueue.remove(0);
                runNextTask();
            });
        });
    }
}
