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
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinesGUI implements GUI {
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
        if (!MineManager.mineExists(mine)) {
            lore.add("§c§lWIP");
        } else {
            lore.addAll(Objects.requireNonNull(MineManager.getMine(mine)).getGUIComposition());
        }
        lore.add("§7");
        lore.add("§b» §nClick to teleport!");
        String[][] script = {{"mine:teleport", mine}};
        return new ItemFactory(Material.EMERALD_BLOCK)
                .setName("§f§l" + mine.toUpperCase())
                .setLore(lore)
                .setClickEvent(script).create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 54, "§a§lMines"
                + HiddenStringUtils.encodeString("gui:update|mines"));

        inv.setItem(9, getMineItem("a"));
        inv.setItem(10, getMineItem("b"));
        inv.setItem(11, getMineItem("c"));
        inv.setItem(12, getMineItem("d"));
        inv.setItem(13, getMineItem("e"));
        inv.setItem(14, getMineItem("f"));
        inv.setItem(15, getMineItem("g"));
        inv.setItem(16, getMineItem("h"));
        inv.setItem(17, getMineItem("i"));

        inv.setItem(18, getMineItem("j"));
        inv.setItem(19, getMineItem("k"));
        inv.setItem(20, getMineItem("l"));
        inv.setItem(21, getMineItem("m"));
        inv.setItem(22, getMineItem("n"));
        inv.setItem(23, getMineItem("o"));
        inv.setItem(24, getMineItem("p"));
        inv.setItem(25, getMineItem("q"));
        inv.setItem(26, getMineItem("r"));

        inv.setItem(27, getMineItem("s"));
        inv.setItem(28, getMineItem("t"));
        inv.setItem(29, getMineItem("u"));
        inv.setItem(30, getMineItem("v"));
        inv.setItem(31, getMineItem("w"));
        inv.setItem(32, getMineItem("x"));
        inv.setItem(33, getMineItem("y"));
        inv.setItem(34, getMineItem("z"));
        inv.setItem(35, getMineItem("star"));

        String[][] prestigeScript = {{"inv:open", "mines_prestige"}};
        ItemStack prestige = new ItemFactory(Material.GOLD_BLOCK)
                .setName("§dPrestige Mines")
                .setLore("", "§7Click to view!", "")
                .setClickEvent(prestigeScript)
                .create();

        String[][] donatorScript = {{"inv:open", "mines_donator"}};
        ItemStack donator = new ItemFactory(Material.DIAMOND_BLOCK)
                .setName("§dDonator Mines")
                .setLore("", "§7Click to view!", "")
                .setClickEvent(donatorScript)
                .create();

        inv.setItem(39, prestige);
        inv.setItem(41, donator);

        inv = InvUtils.fillEmpty(inv, GUIUtils.filler);
        return inv;
    }
}
