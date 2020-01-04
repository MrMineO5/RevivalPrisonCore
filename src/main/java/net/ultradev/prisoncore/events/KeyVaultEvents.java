/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultType;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KeyVaultEvents implements Listener {
    @EventHandler
    public void onClickAddKeys(@NotNull InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        Debugger.log("Click is not cancelled!", "keyvault");
        if (KeyVaultManager.isKeyvault(event.getCurrentItem())) {
            Debugger.log("Item is keyvault!", "keyvault");
            ItemStack other = event.getCursor();
            if (CrateManager.isKey(other)) {
                Debugger.log("Other item is key!", "keyvault");
                event.setCancelled(true);
                Crate cr = CrateManager.getType(other);
                assert cr != null;
                ItemStack kvi = event.getCurrentItem();
                int am = other.getAmount();
                int cap = KeyVaultManager.getRemainingCapacity(kvi, cr.name);
                event.setCurrentItem(null);
                event.getWhoClicked().getInventory().setItem(event.getSlot(), KeyVaultManager.addKeys(kvi, cr.name, Math.min(am, cap)));
                ItemStack newKey = other.clone();
                newKey.setAmount(am - Math.min(am, cap));
                event.getWhoClicked().getOpenInventory().setCursor(newKey);
                Debugger.log("Done!", "keyvault");
            }
        }
    }

    @EventHandler
    public void onCraftingTable(@NotNull InventoryClickEvent e) {
        if (e.getInventory() == null) {
            return;
        }
        if (!e.getInventory().getType().equals(InventoryType.DROPPER)) {
            return;
        }
        String name = e.getInventory().getName();
        if (name == null) {
            return;
        }
        if (!HiddenStringUtils.hasHiddenString(name)) {
            return;
        }
        if (!HiddenStringUtils.extractHiddenString(name).equalsIgnoreCase("kvcraft")) {
            return;
        }
        ItemStack[] contents = e.getInventory().getContents();
        KeyVaultType is = isCrafting(contents);
        if (is == null) {
            return;
        }
        e.getInventory().clear();
        e.getInventory().setItem(4, KeyVaultManager.generateKeyVault(is));
    }

    @EventHandler
    public void onCloseinv(InventoryCloseEvent e) {
        Inventory inv = e.getPlayer().getOpenInventory().getTopInventory();
        if (inv == null) {
            return;
        }
        if (!inv.getType().equals(InventoryType.DROPPER)) {
            return;
        }
        String name = inv.getName();
        if (name == null) {
            return;
        }
        if (!HiddenStringUtils.hasHiddenString(name)) {
            return;
        }
        if (!HiddenStringUtils.extractHiddenString(name).equalsIgnoreCase("kvcraft")) {
            return;
        }
        ItemStack[] contents = inv.getContents();
        InvUtils.giveItemsMailbox((Player) e.getPlayer(), contents);
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent e) {
        if (KeyVaultManager.isKeyvault(e.getItemInHand())) {
            e.setCancelled(true);
        }
    }

    private KeyVaultType isCrafting(ItemStack[] contents) {
        KeyVaultType type = null;
        for (int i = 0; i < 9; i++) {
            if (contents[i] == null) {
                return null;
            }
            if (!KeyVaultManager.isKeyvault(contents[i])) {
                return null;
            }
            if (type == null) {
                type = KeyVaultManager.getType(contents[i]);
            } else {
                if (type != KeyVaultManager.getType(contents[i])) {
                    return null;
                }
            }
        }
        return type.getNextTier();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() == null) {
                return;
            }
            if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST)) {
                return;
            }
            if (KeyVaultManager.isKeyvault(e.getItem())) {
                GUIManager.openGUI(e.getPlayer(), "keyvault");
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Item item = e.getItem();
        Crate cr = CrateManager.getType(item.getItemStack());
        if (cr != null) {
            int am = item.getItemStack().getAmount();
            e.setCancelled(true);
            item.remove();
            CrateManager.giveKeys((Player) e.getEntity(), cr, am);
        }
    }
}
