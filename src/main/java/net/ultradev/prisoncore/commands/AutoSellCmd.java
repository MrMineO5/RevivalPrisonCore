/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.selling.AutoSell;
import net.ultradev.prisoncore.selling.AutoSellEssence;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class AutoSellCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.autosell.admin") || args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.NO_CONSOLE.get());
                return false;
            }
            Player player = (Player) sender;
            if (AutoSell.toggleAutosell(player)) {
                player.sendMessage("§7Auto Sell §aenabled§7.");
            } else {
                player.sendMessage("§7Auto Sell §cdisabled§7.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("addtime")) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§cUsage: /autosell addtime <Player> <Time> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            BigInteger amount;
            try {
                amount = new BigInteger(args[2]).multiply(BigInteger.valueOf(1000));
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid integer: " + args[2]);
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            AutoSell.addAutoSellTime(player, amount);
            sender.sendMessage("§7Given §e" + amount + "§7 of autosell time to §e" + player.getName() + "§7.");
            if (!silent) {
                player.sendMessage("§7You have been given §e" + amount + "§7 autosell time.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("settime")) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§cUsage: /autosell settime <Player> <Time> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            BigInteger amount;
            try {
                amount = new BigInteger(args[2]).multiply(BigInteger.valueOf(1000));
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid integer: " + args[2]);
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            AutoSell.setAutoSellTime(player, amount);
            sender.sendMessage("§7Set §6" + player.getName() + "§7's autosell time to §6" + amount + "§7.");
            if (!silent) {
                player.sendMessage("§7Your autosell time was set to §6" + amount + "§7s.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("removetime")) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§cUsage: /autosell removetime <Player> <Time> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            BigInteger amount;
            try {
                amount = new BigInteger(args[2]).multiply(BigInteger.valueOf(1000));
            } catch (NumberFormatException e) {
                sender.sendMessage("§§cInvalid integer: " + args[2]);
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            AutoSell.addAutoSellTime(player, amount.negate());
            sender.sendMessage("§7Removed §6" + amount + "§7 of autosell time from §6" + player.getName() + "§7.");
            if (!silent) {
                player.sendMessage("§7You have lost §6" + amount + "§7 autosell time.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("giveessence")) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§cUsage: /autosell giveessence <Player> <Time> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            BigInteger time;
            try {
                time = new BigInteger(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid integer: " + args[2]);
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            InvUtils.giveItemMailbox(player,
                    AutoSellEssence.getAutoSellEssence(time.multiply(BigInteger.valueOf(1000))));
            sender.sendMessage("§7Given §e" + player.getName() + " §e" + DateUtils.convertTime(time.longValue())
                    + "§7 of autosell essence.");
            if (!silent) {
                player.sendMessage("§7You have been given §e" + DateUtils.convertTime(time.longValue())
                        + "§7 of autosell essence.");
            }
            return true;
        }
        return false;
    }
}
