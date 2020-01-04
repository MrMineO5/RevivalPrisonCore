/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.utils.gui.GUIClickType;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIClickEvent implements Listener {
    GUIClickEvent() {

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
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

    @EventHandler
    public void putListener(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getName() == null) {
            return;
        }
        if (e.getClickedInventory().getName().contains(HiddenStringUtils.encodeString("noPut"))) {
            e.setCancelled(true);
        }
    }
}
