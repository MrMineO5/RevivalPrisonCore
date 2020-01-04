/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.kits;

import net.ultradev.prisoncore.kits.Kit;
import net.ultradev.prisoncore.kits.KitManager;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitsGUI implements GUI {
    private static ItemStack generateItem(Player player, String k) {
        Kit kit = KitManager.getKit(k);
        assert kit != null;
        String[][] script = {{"kit:use", kit.getName()}, {"inv:open", "kit"}};
        boolean canUse = kit.isCooldown(player);
        boolean perm = kit.hasPermission(player);
        String lore = "§7Click to use!";
        if (!canUse) {
            lore = "§cCooldown: " + DateUtils.convertTime(kit.getCooldown(player) / 1000L);
        }
        if (!perm) {
            lore = "§cThat kit requires " + kit.getRank() + "§c or above";
        }
        return new ItemFactory(kit.getDisplayitem())
                .setName(kit.getDisplayname())
                .setLore(lore)
                .addEnchantment((canUse && perm) ? Enchantment.DIG_SPEED : null)
                .hideFlags()
                .setClickEvent((canUse && perm) ? script : GUIUtils.noop)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 9, "§2§lInventory");
        inv.setItem(0, generateItem(player, "tools"));
        inv.setItem(1, generateItem(player, "pvp"));
        inv.setItem(2, generateItem(player, "coal"));
        inv.setItem(3, generateItem(player, "iron"));
        inv.setItem(4, generateItem(player, "gold"));
        inv.setItem(5, generateItem(player, "diamond"));
        inv.setItem(6, generateItem(player, "emerald"));
        inv.setItem(7, generateItem(player, "obsidian"));
        inv.setItem(8, generateItem(player, "ultra"));
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
