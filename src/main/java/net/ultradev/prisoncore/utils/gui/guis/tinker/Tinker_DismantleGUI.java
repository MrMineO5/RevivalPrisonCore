/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.tinker;

import net.ultradev.prisoncore.utils.gui.guis.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Tinker_DismantleGUI implements GUI {
    public Inventory generateGUI(Player player, @NotNull String... args) {
        return Bukkit.createInventory(null, 27, "§4§lDismantle");
    }
}
