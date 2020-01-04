/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.equipment.Equipment;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArmorMultiCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NO_CONSOLE.get());
            return false;
        }
        if (!sender.hasPermission("ultraprison.enchant")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get("&7[&c&lAdmin&7]"));
            return true;
        }
        if (args.length != 1 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§cUsage: §7/setmulti <Multiplier>");
            return true;
        }
        Player player = (Player) sender;
        Double multi = null;
        try {
            multi = Double.parseDouble(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        player.getInventory().setItemInMainHand(Equipment.setMultiplier(player.getInventory().getItemInMainHand(), multi));
        return true;
    }
}
