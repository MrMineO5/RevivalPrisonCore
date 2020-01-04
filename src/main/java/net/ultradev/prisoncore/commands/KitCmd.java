/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.kits.KitManager;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            GUIManager.openGUI(player, "kit");
            return true;
        }
        if (args.length == 1) {
            KitManager.useKit(player, args[0]);
            return true;
        }
        player.sendMessage("§cUsage: §7/kit [Kit]");
        return false;
    }
}
