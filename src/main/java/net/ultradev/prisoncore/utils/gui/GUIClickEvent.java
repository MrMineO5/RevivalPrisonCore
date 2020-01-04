/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUIClickEvent implements Listener {
    public GUIClickEvent() {

    }

    @EventHandler
    public void onInvClick(@NotNull InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        if (NBTUtils.hasTag(item, "itemscript_general")) {
            event.setCancelled(true);
            GUIUtils.runClick((Player) event.getWhoClicked(), event.getClickedInventory(), event, item);
        }
        GUIClickType type = null;
        ClickType oType = event.getClick();
        if (oType.isLeftClick()) {
            type = GUIClickType.LEFTCLICK;
        }
        if (oType.isRightClick()) {
            type = GUIClickType.RIGHTCLICK;
        }
        if (oType.isCreativeAction()) {
            type = GUIClickType.MIDDLECLICK;
        }
        if (oType.isShiftClick()) {
            type = GUIClickType.SHIFTCLICK;
        }
        if (type == null) {
            return;
        }
        if (NBTUtils.hasTag(item, "itemscript_" + type.toString())) {
            event.setCancelled(true);
            GUIUtils.runClick((Player) event.getWhoClicked(), event.getClickedInventory(), event, item, type);
        }
    }
}
