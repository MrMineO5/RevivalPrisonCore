/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.mines;

import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.rankup.RankupManager;
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

public class Mines_PrestigeGUI implements GUI {
    private static ItemStack getMineItem(String mine) {
        int i = RankupManager.getIdOf(mine);
        Mine m = MineManager.getMine(mine);
        if (m == null) {
            return GUIUtils.error;
        }
        List<String> lore = new ArrayList<>();
        lore.add("§7");
        lore.add("§eRequires: §7[" + RankupManager.getRankDisplayName(i) + "§7]");
        lore.add("§7");
        lore.add("§eResetting in §7" + DateUtils.convertTimeM(m.timeUntilReset()));
        lore.add("§7");
        lore.add("§bBlocks");
        lore.addAll(m.getGUIComposition());
        lore.add("§7");
        lore.add("§b» §nClick to teleport!");
        String[][] script = {{"mine:teleport", mine}};
        return new ItemFactory(Material.GOLD_BLOCK).setName("§f§l" + mine.toUpperCase()).setLore(lore)
                .setClickEvent(script).create();
    }

    @Override
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lPrestige");

        inv.setItem(9, getMineItem("P1"));
        inv.setItem(10, getMineItem("P5"));
        inv.setItem(11, getMineItem("P10"));
        inv.setItem(12, getMineItem("P15"));
        inv.setItem(13, getMineItem("P20"));
        inv.setItem(14, getMineItem("P25"));
        inv.setItem(15, getMineItem("P30"));
        inv.setItem(16, getMineItem("P40"));
        inv.setItem(17, getMineItem("P50"));

        inv = InvUtils.fillEmpty(inv, GUIUtils.filler);
        return inv;
    }
}