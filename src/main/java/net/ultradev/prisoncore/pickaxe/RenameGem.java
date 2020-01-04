/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class RenameGem {

    private static ArrayList<Player> players = new ArrayList<>();

    public static ItemStack getRenameGem() {
        return new ItemFactory(Material.EMERALD)
                .setName("§2§lRename Gem")
                .setLore(
                        "§7Right click with the item in your hand to rename your pickaxe!"
                )
                .addNBT("type", "renameGem")
                .create();
    }

    public static boolean isRenameToken(ItemStack item) {
        return ItemUtils.isType(item, "renameGem");
    }

    public static void giveGemsBack() {
        for (Player p : players) {
            p.getInventory().addItem(getRenameGem());
        }
    }

    public static boolean canRenameItem(Player p) {
        return players.contains(p);
    }

    public static void useRenameToken(Player p, String itemname) {
        if (canRenameItem(p)) {
            players.remove(p);
            ItemStack item = PickaxeUtils.getPickaxe(p);
            ItemMeta m = item.getItemMeta();
            m.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemname));
            item.setItemMeta(m);

            p.getInventory().setItem(0, item);
        }
    }

    public static void activateToken(Player p) {
        players.add(p);
    }
}