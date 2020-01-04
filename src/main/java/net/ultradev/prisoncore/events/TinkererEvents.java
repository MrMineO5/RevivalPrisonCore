/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemDust;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGems;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TinkererEvents implements Listener {
    TinkererEvents() {

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getType() == null) {
            return;
        }
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (event.getWhoClicked().getOpenInventory().getTopInventory() != null) {
                String str = event.getWhoClicked().getOpenInventory().getTopInventory().getName();
                if (!HiddenStringUtils.hasHiddenString(str)) {
                    return;
                }
                String[] data = HiddenStringUtils.extractHiddenString(str).split(":");
                if (data[0].equals("tinker_dismantle")) {
                    if (SocketGem.isSocketGem(event.getCurrentItem())) {
                        if (data.length > 9 * 5) {
                            return;
                        }
                        ArrayList<String> strs = new ArrayList<>(Arrays.asList(data));
                        String s = Objects.requireNonNull(SocketGem.fromItem(event.getCurrentItem())).serialize();
                        strs.add(s);
                        event.setCurrentItem(null);
                        String[] newStrs = new String[strs.size()];
                        toArray(newStrs, strs);
                        GUIManager.openGUI((Player) event.getWhoClicked(), "tinker_dismantle", newStrs);
                    }
                }
            }
        }
    }

    private <T> void toArray(T[] arr, List<T> str) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = str.get(i);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() == null) {
            return;
        }
        if (event.getInventory().getType() == null) {
            return;
        }
        String str = event.getInventory().getName();
        Debugger.log("Inventory name: " + str, "tinker");
        if (str.equalsIgnoreCase("§4§lDismantle")) {
            int dust = 0;
            Player pl = (Player) event.getPlayer();
            for (ItemStack item : event.getInventory().getContents()) {
                if (!SocketGem.isSocketGem(item)) {
                    InvUtils.giveItemMailbox(pl, item);
                    continue;
                }
                SocketGemTier tier = SocketGem.fromItem(item).getTier();
                dust += SocketGems.getDismantleReturns(tier);
            }
            InvUtils.giveItems(pl, SocketGemDust.getSocketGemDust(1), dust);
        }
    }
}
