/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.thunt;

import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class THuntGUI implements GUI {
    @Override
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lTreasure Hunt");
        ItemStack info = new ItemFactory(Material.BOOK)
                .setName("§eInformation")
                .setLore(
                        "§7",
                        "§7The Treasure Hunt is an event that",
                        "§7takes place in an enormous mine",
                        "§7made up of highly valuable blocks, as well",
                        "§7as hidden §6Treasure Chests §7scattered randomly",
                        "§7throughout the mine",
                        "§7",
                        "§7Your performance stats will be shown to you",
                        "§7every 60 seconds whilst you're inside the event.",
                        "§7The people who are the top of each category when",
                        "§7the event ends will be given an exclusive prize!",
                        "§7",
                        "§7The Treasure Hunt event starts every §bFriday§7,",
                        "§bSaturday§7, and §bSunday§7 at 8PM (UTC).",
                        "§7",
                        "§7You can also activate it any other time with a",
                        "§7Treasure Hunt Key from §b/buy§7."
                )
                .setClickEvent(GUIUtils.noop)
                .hideFlags()
                .create();

        String[][] script = {{"thunt:usekey"}, {"inv:close"}};
        ItemStack activate = new ItemFactory(Material.GOLD_BLOCK)
                .setName("§eActivate")
                .setLore(
                        "§7",
                        "§7Click here to start the event!",
                        "§7"
                )
                .setClickEvent(script)
                .hideFlags()
                .create();

        inv.setItem(11, info);
        inv.setItem(15, activate);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(8));
        return inv;
    }
}
