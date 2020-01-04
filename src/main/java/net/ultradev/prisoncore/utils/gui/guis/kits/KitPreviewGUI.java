/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.kits;

import net.ultradev.prisoncore.kits.Kit;
import net.ultradev.prisoncore.kits.KitManager;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class KitPreviewGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§2§lInventory");
        if (args.length != 1) {
            return inv;
        }
        String k = args[0];

        Kit kit = KitManager.getKit(k);

        assert kit != null;
        List<ItemStack> rew = kit.getRewards().stream()
                .map(it -> GUIUtils.addClickEvent(it, GUIUtils.noop))
                .collect(Collectors.toList());
        for (int i = 0; i < rew.size(); i++) {
            inv.setItem(i, rew.get(i));
        }

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
