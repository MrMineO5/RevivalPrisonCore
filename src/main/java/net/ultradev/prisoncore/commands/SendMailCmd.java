/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMailCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NO_CONSOLE.get());
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("ultraprison.admin")) {
            player.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§4§lAdmin§7]"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("§cUsage: §7/sendmail <Player>");
            return true;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
        PlayerData.addMailbox(op, player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(null);
        player.sendMessage("§7Sent item to " + op.getName());
        return true;
    }
}
