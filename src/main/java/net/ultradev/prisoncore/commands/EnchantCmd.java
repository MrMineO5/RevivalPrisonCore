/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.enchant")) {
            sender.sendMessage("§7Right click with your pickaxe to open the enchantment GUI");
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendUsage(sender);
            return true;
        }
        if (args[0].equals("add")) {
            if (args.length < 4 || args.length > 5) {
                sender.sendMessage("§cUsage: §7/enchant add <Player> <Enchant> <Amount> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            Enchantment ench = CustomEnchant.getByName(args[2]);
            int amount = 0;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.INVALID_INTEGER.get(args[3]));
                return true;
            }
            boolean silent = false;
            if (args.length == 5) {
                silent = (args[4].equals("-s"));
            }
            Pickaxe pick = new Pickaxe(player);
            pick.addEnchantmentLevel(ench, amount);
            sender.sendMessage("§7Given §e" + player.getName() + " " + amount + "§7 levels of "
                    + EnchantInfo.getEnchantName(ench) + "§7, new level: §e" + pick.getEnchantmentLevel(ench));
            if (!silent) {
                player.sendMessage("§e" + amount + "§7 levels of §e" + EnchantInfo.getEnchantName(ench)
                        + "§7 have been added to your pickaxe.");
            }
            pick.applyPickaxe();
            return true;
        }
        if (args[0].equals("remove")) {
            if (args.length < 4 || args.length > 5) {
                sender.sendMessage("§cUsage: §7/enchant remove <Player> <Enchant> <Amount> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            Enchantment ench = CustomEnchant.getByName(args[2]);
            int amount = 0;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.INVALID_INTEGER.get(args[3]));
                return true;
            }
            boolean silent = false;
            if (args.length == 5) {
                silent = (args[4].equals("-s"));
            }
            Pickaxe pick = new Pickaxe(player);
            pick.addEnchantmentLevel(ench, -amount);
            sender.sendMessage("§7Removed §e" + amount + "§7 levels of " + EnchantInfo.getEnchantName(ench)
                    + "§7 from §e" + player.getName() + "§7, new level: §e" + pick.getEnchantmentLevel(ench));
            if (!silent) {
                player.sendMessage("§e" + amount + "§7 levels of " + EnchantInfo.getEnchantName(ench)
                        + "§7 have been removed from your pickaxe.");
            }
            pick.applyPickaxe();
            return true;
        }
        if (args[0].equals("set")) {
            if (args.length < 4 || args.length > 5) {
                sender.sendMessage("§cUsage: §7/enchant set <Player> <Enchant> <Amount> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            Enchantment ench = CustomEnchant.getByName(args[2]);
            int amount = 0;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.INVALID_INTEGER.get(args[3]));
                return true;
            }
            boolean silent = false;
            if (args.length == 5) {
                silent = (args[4].equals("-s"));
            }
            Debugger.log("Getting pickaxe...", "enchant_command");
            Pickaxe pick = new Pickaxe(player);
            Debugger.log("Success.", "enchant command");
            pick.setEnchantmentLevel(ench, amount);
            Debugger.log("Setting enchantment...", "enchant_command");
            sender.sendMessage("§7Set §e" + player.getName() + "'s " + EnchantInfo.getEnchantName(ench)
                    + "§7 level to §e" + amount + "§7.");
            if (!silent) {
                player.sendMessage(
                        "§7Your " + EnchantInfo.getEnchantName(ench) + "§7 level has been set to §e" + amount + "§7.");
            }
            Debugger.log("Applying pickaxe...", "enchant_command");
            pick.applyPickaxe();
            Debugger.log("Pickaxe applied", "enchant_command");
            return true;
        }
        /*
         * if (args[0].equals("givescroll")) { if (!(args.length == 4 || args.length ==
         * 5)) { sender.
         * sendMessage("§8{§6§lExcelsiorMC§8} §cUsage: §7/customenchant givescroll <Player> <Enchant> <Amount> [-s]"
         * ); return true; } Player player = Bukkit.getPlayer(args[1]); if (player ==
         * null) { sender.sendMessage("§8{§6§lExcelsiorMC§8} §cPlayer not found.");
         * return true; } Enchantment ench = CustomEnchant.getByName(args[2]); if (ench
         * == null) {
         * sender.sendMessage("§8{§6§lExcelsiorMC§8} §cEnchantment not found: §7" +
         * args[2]); } int amount = 0; try { amount = Integer.parseInt(args[3]); }
         * catch(NumberFormatException e) {
         * sender.sendMessage("§8{§6§lExcelsiorMC§8} §cInvalid integer: §7" + args[3]);
         * return true; } boolean silent = false; if (args.length == 5) { silent =
         * (args[4].equals("-s")); } InvUtils.giveItems(player,
         * ScrollUtils.createScroll(ench), amount);
         * sender.sendMessage("§8{§6§lExcelsiorMC§8} §7Given §6" + amount + " " +
         * EnchantInfo.getEnchantName(ench) + "§7 scrolls to §6" + player.getName() +
         * "§7."); if (!silent) {
         * player.sendMessage("§8{§6§lExcelsiorMC§8} §7You have received §6" + amount +
         * " " + EnchantInfo.getEnchantName(ench) + "§7 scrolls."); } return true; }
         */
        if (args[0].equals("reset")) {
            if (!(args.length == 2 || args.length == 3)) {
                sender.sendMessage("§cUsage: §7/enchant reset <Player> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("§8{§6§lExcelsiorMC§8} §cPlayer not found.");
                return true;
            }
            boolean silent = false;
            if (args.length == 3) {
                silent = (args[2].equals("-s"));
            }
            player.getInventory().setItem(0, PickaxeUtils.createDefaultPickaxe());
            sender.sendMessage("§7Resetting §6" + player.getName() + "'s§7 pickaxe.");
            if (!silent) {
                player.sendMessage("§7Your pickaxe has been reset.§7.");
            }
            return true;
        }
        if (args[0].equals("setlevel")) {
            if (args.length < 3 || args.length > 4) {
                sender.sendMessage("§cUsage: §7/enchant setlevel <Player> <Level> [-s]");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
                return true;
            }
            int level = 0;
            try {
                level = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Messages.INVALID_INTEGER.get(args[2]));
                return true;
            }
            boolean silent = false;
            if (args.length == 4) {
                silent = (args[3].equals("-s"));
            }
            Debugger.log("Getting pickaxe...", "enchant_command");
            ItemStack pick = PickaxeUtils.getPickaxe(player);
            Debugger.log("Success.", "enchant command");
            pick = NBTUtils.setInt(pick, "level", level);
            Debugger.log("Setting level...", "enchant_command");
            sender.sendMessage("§7Set §e" + player.getName() + "'s§7 pickaxe level to §e" + level + "§7.");
            if (!silent) {
                player.sendMessage(
                        "§7Your pickaxe level has been set to §e" + level + "§7.");
            }
            Debugger.log("Applying pickaxe...", "enchant_command");
            player.getInventory().setItem(0, pick);
            Debugger.log("Pickaxe applied", "enchant_command");
            return true;
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§cUsage:");
        sender.sendMessage("§7/enchant add <Player> <Enchant> <Amount> [-s]");
        sender.sendMessage("§7/enchant remove <Player> <Enchant> <Amount> [-s]");
        sender.sendMessage("§7/enchant set <Player> <Enchant> <Amount> [-s]");
        sender.sendMessage("§7/enchant setlevel <Player> <Level> [-s]");
        // sender.sendMessage("§7/enchant givescroll <Player> <Enchant> <Amount> [-s]");
        sender.sendMessage("§7/enchant reset <Player> [-s]");
    }
}
