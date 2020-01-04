/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.mines;

import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.playerdata.StoreRank;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Mines_DonatorGUI implements GUI {
    private static ItemStack getMineItem(String mine) {
        Mine m = MineManager.getMine(mine);
        if (m == null) {
            return GUIUtils.error;
        }
        StoreRank rank = StoreRank.valueOf(mine.toUpperCase());
        List<String> lore = new ArrayList<>();
        lore.add("§7");
        lore.add("§eRequires: " + rank.getPrefix());
        lore.add("§7");
        lore.add("§eResetting in §7" + DateUtils.convertTimeM(m.timeUntilReset()));
        lore.add("§7");
        lore.add("§bBlocks");
        lore.addAll(m.getGUIComposition());
        lore.add("§7");
        lore.add("§b» §nClick to teleport!");
        String[][] script = {{"mine:teleport", mine}};
        return new ItemFactory(Material.EMERALD_BLOCK)
                .setName(rank.getDisplayName())
                .setLore(lore)
                .setClickEvent(script).create();
    }

    @Override
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lDonators");

        inv.setItem(10, getMineItem("coal"));
        inv.setItem(11, getMineItem("iron"));
        inv.setItem(12, getMineItem("gold"));
        inv.setItem(13, getMineItem("diamond"));
        inv.setItem(14, getMineItem("emerald"));
        inv.setItem(15, getMineItem("obsidian"));
        inv.setItem(16, getMineItem("ultra"));

        inv = InvUtils.fillEmpty(inv, GUIUtils.filler);
        return inv;
    }
}