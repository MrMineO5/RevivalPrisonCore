/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.betterspigot.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerLeftClickBlockEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    protected ItemStack item;
    @Getter
    protected Action action;
    @Getter
    protected Block blockClicked;
    @Getter
    protected BlockFace blockFace;
    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    private Result useClickedBlock;
    @Getter
    @Setter
    private Result useItemInHand;
    @Getter
    private EquipmentSlot hand;


    public PlayerLeftClickBlockEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace, EquipmentSlot hand) {
        super(who);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.hand = hand;
        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
