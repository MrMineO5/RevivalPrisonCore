/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.logging.log;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class User {
    private UserType type;
    private UUID userId;

    public String getName() {
        if (type == null) {
            return "None";
        }
        switch (type) {
            case CONSOLE:
                return "Console";
            case PLAYER:
                return Bukkit.getOfflinePlayer(userId).getName();
        }
        return "None";
    }

    public enum UserType {
        CONSOLE, PLAYER
    }
}