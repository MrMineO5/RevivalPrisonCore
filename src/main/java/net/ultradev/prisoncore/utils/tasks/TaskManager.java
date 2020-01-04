/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.tasks;

public class TaskManager {
    static final int maxThreads = 10;

    public static TaskQueue getNewAsyncQueue() {
        return new TaskQueue(true);
    }

    public static TaskQueue getNewQueue() {
        return new TaskQueue(false);
    }
}
