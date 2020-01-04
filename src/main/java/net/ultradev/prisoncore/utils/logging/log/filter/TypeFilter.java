/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log.filter;

import net.ultradev.prisoncore.utils.collectors.UltraCollectors;
import net.ultradev.prisoncore.utils.logging.log.EntryType;
import net.ultradev.prisoncore.utils.logging.log.LogEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeFilter implements LogFilter {
    private EntryType type;

    public TypeFilter(EntryType type) {
        this.type = type;
    }

    @Override
    public LinkedHashMap<Long, LogEntry> filter(Map<Long, LogEntry> map) {
        return map.entrySet().stream()
                .filter(e -> e.getValue().getType().equals(type))
                .collect(UltraCollectors.toLinkedHashMap());
    }
}
