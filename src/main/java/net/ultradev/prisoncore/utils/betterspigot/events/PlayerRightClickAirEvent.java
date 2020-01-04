/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerRightClickAirEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    @Setter
    private boolean cancelled;

    public PlayerRightClickAirEvent(Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
