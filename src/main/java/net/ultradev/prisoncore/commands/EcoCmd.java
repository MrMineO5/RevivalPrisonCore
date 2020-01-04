/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.commands.beta.RequiredRank;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.math.BigInteger;


public class EcoCmd
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        BigInteger am;
        if (!RequiredRank.ADMIN.hasRank(sender)) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get(RequiredRank.ADMIN.getPrefix()));
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage("§cUsage: /eco <Add/Remove/Set> <Amount>");
            return true;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
        if (op == null) {
            sender.sendMessage("§cInvalid player: " + args[1]);
            return true;
        }

        try {
            am = new BigInteger(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number: " + args[2]);
            return true;
        }
        String arg = args[0].toLowerCase();
        boolean success = false;
        switch (arg) {
            case "add":
                Economy.tokens.addBalance(op, am);
                sender.sendMessage("§aSuccess!");
                return true;
            case "remove":
                Economy.tokens.removeBalance(op, am);
                sender.sendMessage("§aSuccess!");
                return true;
            case "set":
                Economy.tokens.setBalance(op, am);
                sender.sendMessage("§aSuccess");
                return true;
        }
        sender.sendMessage("§cInvalid mode: " + args[0]);
        return true;
    }
}
