/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrateCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.crate.admin")) {
            sender.sendMessage("§cThat command requires §8[§4§lAdmin§8]§c or above.");
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendUsage(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("key")) {
            if (args.length != 4 && args.length != 5) {
                sender.sendMessage("§cUsage: /crate key <Player> <Type> <Amount> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }
            Crate crate = CrateManager.getCrate(args[2]);
            if (crate == null) {
                sender.sendMessage("§cThat crate does not exist.");
                return true;
            }
            Integer amount = null;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid integer: " + amount);
                return true;
            }
            boolean silent = false;
            if (args.length == 5) {
                silent = (args[4].equals("-s"));
            }
            CrateManager.giveKeys(player, crate, amount);
            sender.sendMessage(
                    "§7Given §6" + amount + " " + crate.displayName + " keys§7 to §6" + player.getName() + "§7.");
            if (!silent) {
                player.sendMessage("§7You have been given §6" + amount + " " + crate.displayName + " keys§7.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("keyall")) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§cUsage: /crate keyall <Type> <Amount> [-s]");
                return true;
            }
            Crate crate = CrateManager.getCrate(args[1]);
            if (crate == null) {
                sender.sendMessage("§cThat crate does not exist.");
                return true;
            }
            Integer amount = null;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid integer: " + amount);
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            sender.sendMessage("§7Given §6" + amount + " " + crate.displayName + " keys§7 to all players.");
            for (Player player : Bukkit.getOnlinePlayers()) {
                CrateManager.giveKeys(player, crate, amount);
                if (!silent) {
                    player.sendMessage("§7You have been given §6" + amount + " " + crate.displayName + " keys§7.");
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length != 3) {
                sender.sendMessage("§7/crate give <Player> <Type>");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }
            Crate crate = CrateManager.getCrate(args[2]);
            if (crate == null) {
                sender.sendMessage("§cThat crate does not exist.");
                return true;
            }
            ItemStack item = crate.getAdminCrate();
            player.getInventory().addItem(item);
            return true;
        }
        return true;
    }

    private void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage("§cUsage:");
        sender.sendMessage("§7/crate key <Player> <Type> <Amount> [-s]");
        sender.sendMessage("§7/crate keyall <Type> <Amount> [-s]");
        sender.sendMessage("§7/crate give <Player> <Type>");
    }
}
