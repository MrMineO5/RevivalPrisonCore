/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.tasks;

import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskQueue {
    private List<Runnable> tasks;
    private List<Integer> runningTasks;
    private Runnable onComplete = null;
    private boolean async;
    private boolean doneAdding;

    TaskQueue(boolean async) {
        tasks = new ArrayList<>();
        runningTasks = new ArrayList<>();
        this.async = async;
        this.doneAdding = false;
    }

    public void queueTask(Runnable run) {
        tasks.add(run);
        checkTasks();
    }

    public void queueTasks(Runnable... runs) {
        tasks.addAll(Arrays.asList(runs));
        checkTasks();
    }

    public void onCompletion(Runnable run) {
        this.onComplete = run;
    }

    public void complete() {
        this.doneAdding = true;
    }

    private void checkTasks() {
        if (tasks.isEmpty()) {
            if (runningTasks.isEmpty()) {
                if (doneAdding && onComplete != null) {
                    Synchronizer.synchronize(onComplete);
                    onComplete = null;
                }
            }
            return;
        }
        if (runningTasks.size() < TaskManager.maxThreads) {
            runTask();
            checkTasks();
        }
    }

    private void runTask() {
        if (tasks.isEmpty()) {
            return;
        }
        Runnable run = transformRunnable(tasks.get(0));
        tasks.remove(0);
        int id;
        if (async) {
            id = Synchronizer.desynchronize(run);
        } else {
            id = Synchronizer.synchronize(run);
        }
        runningTasks.add(id);
    }

    private void taskDone() {
        Debugger.log("Task done", "taskqueue");
        if (runningTasks == null || runningTasks.isEmpty()) {
            Debugger.log("No running tasks exist", "taskqueue");
            runningTasks = new ArrayList<>();
            checkTasks();
            return;
        }
        Debugger.log("Running tasks exist, streaming through checker", "taskqueue");
        try {
            runningTasks = runningTasks.stream()
                    .filter((i) -> {
                        Debugger.log("Checking task with id: " + i, "taskqueue");
                        return Bukkit.getScheduler().isCurrentlyRunning(i) || Bukkit.getScheduler().isQueued(i);
                    })
                    .collect(Collectors.toList());
        } catch (Exception ignored) {
        }
        checkTasks();
    }

    private Runnable transformRunnable(Runnable run) {
        return () -> {
            run.run();
            taskDone();
        };
    }
}