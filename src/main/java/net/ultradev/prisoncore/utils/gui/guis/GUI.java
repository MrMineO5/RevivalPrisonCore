/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GUI {
    Inventory generateGUI(Player player, String... args);
}
