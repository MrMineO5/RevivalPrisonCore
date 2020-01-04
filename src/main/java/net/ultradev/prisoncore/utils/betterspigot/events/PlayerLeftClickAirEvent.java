/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerLeftClickAirEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    protected ItemStack item;
    @Getter
    protected Action action;
    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    @Setter
    private Result useItemInHand;
    @Getter
    private EquipmentSlot hand;

    public PlayerLeftClickAirEvent(Player who, Action action, ItemStack item, EquipmentSlot hand) {
        super(who);
        this.action = action;
        this.item = item;
        this.hand = hand;
        this.useItemInHand = Result.DEFAULT;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
