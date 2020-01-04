/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.faq;

import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FaqGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§2§lFAQ");

        ItemStack gettingStarted = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aGetting started")
                .setLore(
                        "§7Welcome to §c§lRevival§f§lPrison",
                        "§7To go to any mine, use /mine, if",
                        "§7you want to go to a specific mine",
                        "§7you can use /mine <Name>. Right",
                        "§7click with your pickaxe to upgrade",
                        "§7it and Shift+Left click to sell your",
                        "§7mined blocks. Remember to check the",
                        "§7sidebar for your rankup progress and",
                        "§7rank up when you can. Higher mines",
                        "§7have better blocks!",
                        "§7Have fun!"
                )
                .setClickEvent(GUIUtils.noop)
                .create();
        ItemStack autoMiner = new ItemFactory(Material.SKULL_ITEM, 1, (short) 3)
                .setSkullOwner(UUID.fromString("2bc924c3-3999-4c02-92de-c4328514c5a1"))
                .setName("§e§lAuto Miner")
                .setLore(
                        "§7Every player has their own Auto Miner",
                        "§7get time for it by collecting Auto",
                        "§7Miner Shards from crates. Go to a mine",
                        "§7and use /am to summon your Auto Miner,",
                        "§7it will mine for you and get you tokens,",
                        "§7keys, and dust. Upgrade your Auto Miner",
                        "§7to get more rewards!"
                )
                .setClickEvent(GUIUtils.noop)
                .create();

        inv.setItem(9, gettingStarted);
        inv.setItem(10, autoMiner);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
