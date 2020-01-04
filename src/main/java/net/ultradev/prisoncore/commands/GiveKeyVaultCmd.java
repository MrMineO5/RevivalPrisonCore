/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultType;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveKeyVaultCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.givekeyvault")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§c§lAdmin§7]"));
            return true;
        }
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage("§cUsage: /givekeyvault <Player> <Type> [-s]");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
            return true;
        }
        KeyVaultType type;
        try {
            type = KeyVaultType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage("§cInvalid KeyVaultType: " + args[1]);
            return true;
        }
        boolean silent = false;
        if (args.length == 3) {
            silent = (args[2].equals("-s"));
        }
        InvUtils.giveItemMailbox(player, KeyVaultManager.generateKeyVault(type));
        sender.sendMessage("§7Given §e" + player.getName() + "§7 a key vault!");
        if (!silent) {
            player.sendMessage("§7You have been given a key vault!");
        }
        return true;
    }
}
