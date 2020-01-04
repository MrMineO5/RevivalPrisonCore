/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.treasurehunt.TreasureHunt;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TreasureHuntCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.treasurehunt")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§4§lAdmin§7]"));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage("§7/thunt start [Time]");
            sender.sendMessage("§7/thunt stop");
            sender.sendMessage("§7/thunt extend <Time>");
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (args.length > 2) {
                sender.sendMessage("§cUsage: §7/thunt start [Time]");
                return false;
            }
            int time = 1800;
            if (args.length == 2) {
                time = Integer.parseInt(args[1]);
            }
            TreasureHunt.start(time);
            return true;
        }
        if (args[0].equalsIgnoreCase("stop")) {
            if (args.length != 1) {
                sender.sendMessage("§cUsage: §7/thunt stop");
                return false;
            }
            TreasureHunt.stop();
            sender.sendMessage("§7You force-ended the Treasure Hunt");
            return true;
        }
        if (args[0].equalsIgnoreCase("extend")) {
            if (args.length != 2) {
                sender.sendMessage("§cUsage: §7/thunt extend <Time>");
                return false;
            }
            int time = Integer.parseInt(args[1]);
            TreasureHunt.extend(time);
            Bukkit.broadcastMessage("§d" + sender.getName() + " extended the Treasure Hunt by " + time + " seconds");
            return true;
        }
        return false;
    }
}
