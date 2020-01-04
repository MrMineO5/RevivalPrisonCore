/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log.filter;

import net.ultradev.prisoncore.utils.collectors.UltraCollectors;
import net.ultradev.prisoncore.utils.logging.log.LogEntry;
import net.ultradev.prisoncore.utils.logging.log.User;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UserFilter implements LogFilter {
    private User.UserType userType;
    private UUID userId;

    public UserFilter(User.UserType userType, UUID userId) {
        this.userType = userType;
        this.userId = userId;
    }

    @Override
    public LinkedHashMap<Long, LogEntry> filter(Map<Long, LogEntry> map) {
        return map.entrySet()
                .stream()
                .filter(e ->
                        e.getValue().getUser().getType().equals(userType)
                                && e.getValue().getUser().getUserId().equals(userId)
                )
                .collect(UltraCollectors.toLinkedHashMap());
    }
}
