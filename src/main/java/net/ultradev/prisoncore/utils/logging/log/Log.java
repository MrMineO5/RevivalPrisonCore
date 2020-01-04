/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log;

import net.ultradev.prisoncore.utils.logging.log.filter.LogFilter;
import net.ultradev.prisoncore.utils.time.DateUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Log {
    private static Map<Long, LogEntry> log = new HashMap<>();

    public static void log(LogEntry entry) {
        log(entry, DateUtils.getMilliTimeStamp());
    }

    private static void log(LogEntry entry, long timeStamp) {
        log.put(timeStamp, entry);
    }

    public static LinkedHashMap<Long, LogEntry> filter(LogFilter[] filters) {
        LinkedHashMap<Long, LogEntry> ret = new LinkedHashMap<>(log);
        for (LogFilter filter : filters) {
            ret = filter.filter(ret);
        }
        return ret;
    }
}
