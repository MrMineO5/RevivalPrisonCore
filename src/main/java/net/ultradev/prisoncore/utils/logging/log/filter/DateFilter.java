/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log.filter;

import net.ultradev.prisoncore.utils.collectors.UltraCollectors;
import net.ultradev.prisoncore.utils.logging.log.LogEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public class DateFilter implements LogFilter {
    private long timeStampLeast;
    private long timeStampMost;

    public DateFilter(long timeStampLeast, long timeStampMost) {
        this.timeStampLeast = timeStampLeast;
        this.timeStampMost = timeStampMost;
    }

    @Override
    public LinkedHashMap<Long, LogEntry> filter(Map<Long, LogEntry> map) {
        return map.entrySet()
                .stream()
                .filter(e -> e.getKey() < timeStampMost)
                .filter(e -> e.getKey() > timeStampLeast)
                .collect(UltraCollectors.toLinkedHashMap());
    }
}
