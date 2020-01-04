/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.crate;

import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.InventoryTitleBuilder;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CratePreviewGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Crate cr = CrateManager.getCrate(args[0]);
        assert cr != null;
        int rows = (int) Math.ceil(cr.drops.size() / 9.0);
        Inventory inv = Bukkit.createInventory(null, rows * 9, new InventoryTitleBuilder(cr.displayName + " rewards").noPut().create());
        ItemStack[] items = new ItemStack[rows * 9];
        int i = 0;
        for (Reward re : cr.drops.keySet()) {
            items[i] = GUIUtils.addClickEvent(re.getGUIItem(), GUIUtils.noop);
            i++;
        }
        inv.setContents(items);
        return inv;
    }
}
