/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCmd implements CommandExecutor {
    private static void toggleDebug(Player player) {
        if (Debugger.debugged_all.contains(player)) {
            player.sendMessage("§7Debugger disabled.");
            Debugger.debugged_all.remove(player);
        } else {
            player.sendMessage("§7Debugger enabled.");
            Debugger.debugged_all.add(player);
        }
    }

    private static void toggleDebug(Player player, String debug) {
        if (Debugger.debugged.containsKey(debug) && Debugger.debugged.get(debug).contains(player)) {
            Debugger.removePlayer(player, debug);
            player.sendMessage("§7Debugger disabled (" + debug + ")");
        } else {
            player.sendMessage("§7Debugger enabled (" + debug + ")");
            Debugger.addPlayer(player, debug);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                Debugger.debug_console = !Debugger.debug_console;
                return true;
            }
            if (args.length == 1) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage("§cInvalid player");
                    return false;
                }

            }
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("ultraprison.admin")) {
            player.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§4§lAdmin§7]"));
            return false;
        }
        if (args.length == 1) {
            toggleDebug(player, args[0]);
            return true;
        }
        toggleDebug(player);
        return true;
    }
}
