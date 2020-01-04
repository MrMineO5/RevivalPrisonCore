/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.items;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvUtils {
    public static boolean isFull(Player player) {
        Inventory inv = player.getInventory();
        return inv.firstEmpty() == -1;
    }

    public static boolean invCanHold(Player player, ItemStack item) {
        if (item == null) {
            return true;
        }
        return invCanHold(player, item, item.getAmount());
    }

    public static boolean invCanHold(Player player, ItemStack item, int count) {
        Inventory inv = player.getInventory();
        int freeSlots = 0;
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < 36; i++) {
            if (contents[i] == null) {
                freeSlots += item.getMaxStackSize();
                continue;
            }
            if (contents[i].isSimilar(item)) {
                freeSlots = item.getMaxStackSize() - contents[i].getAmount();
            }
        }
        return freeSlots >= count;
    }

    public static boolean invCanHoldPartial(Player player, ItemStack item, int count) {
        Inventory inv = player.getInventory();
        int freeSlots = 0;
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < 36; i++) {
            if (contents[i] == null) {
                freeSlots += item.getMaxStackSize();
                continue;
            }
            if (contents[i].isSimilar(item)) {
                freeSlots = item.getMaxStackSize() - contents[i].getAmount();
            }
        }
        return freeSlots > 0;
    }

    public static int getSpace(@NotNull Player player, ItemStack item) {
        int cap = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for (int j = 0; j < 36; j++) {
            ItemStack i = contents[j];
            if (i == null) {
                cap += item.getMaxStackSize();
                continue;
            }
            if (i.isSimilar(item)) {
                if (i.getAmount() < i.getMaxStackSize()) {
                    cap += i.getMaxStackSize() - i.getAmount();
                }
            }
        }
        Debugger.log("Cap: " + cap, "InvUtils#getSpace");
        return cap;
    }

    public static boolean giveItemMailbox(Player player, ItemStack item) {
        if (item == null) {
            return false;
        }
        if (!invCanHold(player, item)) {
            PlayerData.addMailbox(player, item);
            return true;
        }
        player.getInventory().addItem(item);
        return false;
    }

    public static void giveItem(Player player, ItemStack item) {
        if (item == null) {
            return;
        }
        player.getInventory().addItem(item);
    }

    public static void giveItems(Player player, ItemStack[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        player.getInventory().addItem(items);
    }

    public static void giveItemsMailbox(Player player, ItemStack[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        for (ItemStack item : items) {
            giveItemMailbox(player, item);
        }
    }

    public static Inventory fillEmpty(@NotNull Inventory inv, ItemStack item) {
        Inventory newInv = Bukkit.createInventory(inv.getHolder(), inv.getSize(), inv.getTitle());
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                newInv.setItem(i, item);
            } else {
                newInv.setItem(i, inv.getItem(i));
            }
        }
        return newInv;
    }

    public static void giveItems(Player player, ItemStack item, int count) {
        if (count == 0) {
            return;
        }
        int stacksize = item.getMaxStackSize();
        if (count < stacksize) {
            ItemStack i = item.clone();
            i.setAmount(count);
            giveItem(player, i);
            return;
        }
        int stacks = Math.floorDiv(count, stacksize);
        int am = count % stacksize;
        ItemStack[] items = new ItemStack[stacks + 1];
        ItemStack stack = item.clone();
        stack.setAmount(stacksize);
        ItemStack i = item.clone();
        i.setAmount(am);
        for (int j = 0; j < stacks; j++) {
            items[j] = stack.clone();
        }
        items[stacks] = i;
        giveItems(player, items);
    }

    public static void giveItemsMailbox(Player player, ItemStack item, int count) {
        if (item == null) {
            return;
        }
        if (count == 0) {
            return;
        }
        int stacksize = item.getMaxStackSize();
        if (count < stacksize) {
            ItemStack i = item.clone();
            i.setAmount(count);
            giveItemMailbox(player, i);
            return;
        }
        int stacks = Math.floorDiv(count, stacksize);
        int am = count - stacks * stacksize;
        ItemStack stack = item.clone();
        stack.setAmount(stacksize);
        ItemStack i = item.clone();
        i.setAmount(am);
        for (int j = 0; j < stacks; j++) {
            giveItemMailbox(player, stack);
        }
        giveItemMailbox(player, i);
    }

    public static Inventory setRow(Inventory inv, int row, ItemStack[] rowItems) {
        Inventory ret = Bukkit.createInventory(inv.getHolder(), inv.getSize(), inv.getTitle());
        ret.setContents(inv.getContents());
        for (int i = 0; i < rowItems.length; i++) {
            ret.setItem(9 * row + i, rowItems[i]);
        }
        return ret;
    }

    public static int getCount(ItemStack item, Inventory inv) {
        int count = 0;
        for (ItemStack i : inv.getContents()) {
            if (i != null && i.isSimilar(item)) {
                count += i.getAmount();
            }
        }
        return count;
    }

    public static boolean decrementHand(Player player) {
        if (player.getInventory().getItemInMainHand() == null) {
            return false;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);
        return true;
    }
}
