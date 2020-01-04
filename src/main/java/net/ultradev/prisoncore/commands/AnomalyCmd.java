/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnomalyCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§4§lAdmin§7]"));
            return false;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§cs");
        }
        return false;
    }
}
