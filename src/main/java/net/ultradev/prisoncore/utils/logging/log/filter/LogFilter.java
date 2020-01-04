/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log.filter;

import net.ultradev.prisoncore.utils.logging.log.LogEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public interface LogFilter {
    LinkedHashMap<Long, LogEntry> filter(Map<Long, LogEntry> map);
}
