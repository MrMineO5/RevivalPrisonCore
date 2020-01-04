/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta.commandtypes;

import net.ultradev.prisoncore.commands.beta.Command;
import net.ultradev.prisoncore.commands.beta.RequiredRank;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GUICommand extends Command {

    public GUICommand(String name, JavaPlugin pl) {
        this(name, name, pl);
    }

    public GUICommand(String name, String gui, JavaPlugin pl) {
        this(name, gui, new String[0], pl);
    }

    public GUICommand(String name, String gui, String[] ars, JavaPlugin pl) {
        this(name, gui, ars, RequiredRank.NONE, pl);
    }

    public GUICommand(String name, String gui, String[] ars, RequiredRank req, JavaPlugin plugin) {
        super(name, "", false, req, plugin, (sender, args) -> {
            GUIManager.openGUI((Player) sender, gui, ars);
            return true;
        });
    }
}
