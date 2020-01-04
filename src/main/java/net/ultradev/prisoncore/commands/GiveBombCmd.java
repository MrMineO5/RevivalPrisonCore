/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.playerdata.StaffRank;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBombCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("bomb.give")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get(StaffRank.ADMIN));
            return true;
        }
        if (args.length != 3 && args.length != 4) {
            sender.sendMessage("§cUsage: /givebomb <Player> <Power> <Amount> [-s]");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
            return true;
        }
        Integer power = null;
        try {
            power = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.INVALID_INTEGER.get(args[1]));
            return true;
        }
        Integer amount = null;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.INVALID_INTEGER.get(args[2]));
            return true;
        }
        boolean silent = false;
        if (args.length == 4) {
            silent = (args[3].equals("-s"));
        }
        InvUtils.giveItemMailbox(player, Bombs.generateBombItem(power, amount));
        sender.sendMessage("§7Given §6" + player.getName() + " §6" + amount + " power §6" + power + " bombs§7!");
        if (!silent) {
            player.sendMessage("§7You have been given §e" + amount + "§7 power §e" + power + "§7 bombs!");
        }
        return true;
    }
}
