/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log;

import lombok.Getter;

@Getter
public class LogEntry {
    private EntryType type;
    private User user;
    private String value;

    public LogEntry(EntryType type, User user, String value) {
        this.type = type;
        this.user = user;
        this.value = value;
    }
}
