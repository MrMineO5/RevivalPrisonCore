/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.keyvault;

import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class KeyvaultCraftGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        return Bukkit.createInventory(null,
                InventoryType.DROPPER,
                "§2§lKVCraft" + HiddenStringUtils.encodeString("kvcraft")
        );
    }
}
