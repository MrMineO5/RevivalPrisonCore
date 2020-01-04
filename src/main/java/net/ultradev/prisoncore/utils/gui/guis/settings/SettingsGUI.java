/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.settings;

import net.ultradev.prisoncore.commands.beta.RequiredRank;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.playerdata.settings.Setting;
import net.ultradev.prisoncore.playerdata.settings.SettingsManager;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsGUI implements GUI {
    private ItemStack generateItem(String setting, Player player) {
        boolean val = PlayerData.getSetting(player, setting);
        Setting set = SettingsManager.getSetting(setting);
        if (set == null) {
            set = new Setting("Error", "Error", false, RequiredRank.ADMIN, "Error: Tell UltraDev the following:", "dat: " + setting);
        }
        boolean can = set.requiredRank.hasRank(player);
        String[][] itemScript = {{"setting:toggle", setting}, {"inv:open", "settings"}};
        ArrayList<String> lore = new ArrayList<>(Arrays.asList(set.description));
        if (!can) {
            lore.add("");
            lore.add("§cRequires " + set.requiredRank.getPrefix() + "§c or above.");
        }
        return new ItemFactory(can ? Material.INK_SACK : Material.BARRIER, 1, (short) (val ? 10 : 8))
                .setName((val ? "§a" : "§7") + set.displayName)
                .setLore(lore)
                .setClickEvent(can ? itemScript : GUIUtils.noop)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§2§lInventory");

        inv.setItem(9, generateItem("crate_message", player));
        inv.setItem(10, generateItem("key_message", player));
        inv.setItem(11, generateItem("lb_message", player));
        inv.setItem(12, generateItem("scroll_message", player));
        inv.setItem(13, generateItem("auto_exchange", player));
        inv.setItem(14, generateItem("fly_on_join", player));
        inv.setItem(15, generateItem("as_on_join", player));

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
