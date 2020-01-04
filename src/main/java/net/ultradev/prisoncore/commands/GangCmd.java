/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.gangs.Gang;
import net.ultradev.prisoncore.gangs.GangManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.PlayerMap;
import net.ultradev.prisoncore.utils.text.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangCmd implements CommandExecutor {
    private boolean enabled = false;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!enabled) {
            sender.sendMessage("§cThis command is currently disabled!");
            return false;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendUsage(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                sender.sendMessage("§cUsage: /gang create <Name>");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.NO_CONSOLE.get());
                return true;
            }
            Player player = (Player) sender;
            if (PlayerData.isInGang(player)) {
                player.sendMessage("§cYou are already in a gang, please leave it first.");
                return true;
            }
            if (!StringUtils.isAlphanumeric(args[1])) {
                player.sendMessage("§cGang name may not contain special characters!");
                return true;
            }
            if (args[1].length() > 10) {
                player.sendMessage("§cGang name may not be more than 10 characters long!");
                return true;
            }
            if (GangManager.createGang(player.getUniqueId(), args[1])) {
                player.sendMessage("§aGang created");
                Bukkit.broadcastMessage("§6Gang » §e" + player.getName() + "§6 created Gang §e" + args[1] + "§6!");
            } else {
                player.sendMessage("§cA gang with that name already exists.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("invite")) {
            if (args.length != 3) {
                sender.sendMessage("§cUsage: /gang invite <Add/Remove> <Player>");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.NO_CONSOLE.get());
                return true;
            }
            Player player = (Player) sender;
            String g = PlayerData.getGang(player);
            if (g == null) {
                player.sendMessage("§cYou must be in a gang to invite people.");
                return false;
            }
            Gang gang = GangManager.getGang(g);
            OfflinePlayer pl = PlayerMap.getPlayer(args[2]);
            if (pl == null) {
                player.sendMessage("§cThat player has never joined the server.");
                return false;
            }
            if (args[1].equalsIgnoreCase("add")) {
                GangManager.invite(gang, pl.getUniqueId());
                player.sendMessage("§7Successfully invited §e" + pl.getName() + "§7 to the gang!");
            } else if (args[1].equalsIgnoreCase("remove")) {
                GangManager.uninvite(gang, pl.getUniqueId());
                player.sendMessage("§7Successfully removed invite of §e" + pl.getName() + "§7 to the gang!");
            } else {
                sender.sendMessage("§cUsage: /gang invite <Add/Remove> <Player>");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            if (args.length != 2) {
                sender.sendMessage("§cUsage: /gang join <Gang>");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.NO_CONSOLE.get());
                return true;
            }
            Player player = (Player) sender;
            if (PlayerData.isInGang(player)) {
                player.sendMessage("§cYou are already in a gang.");
                return false;
            }
            if (GangManager.join(player, args[1])) {
                player.sendMessage("§aSuccessfully joined the gang!");
            } else {
                player.sendMessage("§cThat gang does not exist or you are not invited to it.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length != 1 && args.length != 2) {
                sender.sendMessage("§cUsage: /gang info [Gang]");
                return true;
            }
            Gang g;
            if (args.length == 2) {
                if (args[1].startsWith("p-")) {
                    OfflinePlayer p = PlayerMap.getPlayer(args[1].substring(2));
                    if (p == null) {
                        sender.sendMessage("§cThat player has never joined the server.");
                        return false;
                    }
                    if (!PlayerData.isInGang(p)) {
                        sender.sendMessage("§cThat player is not in a gang.");
                        return false;
                    }
                    g = GangManager.getGang(PlayerData.getGang(p));
                } else {
                    g = GangManager.getGang(args[1]);
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cUsage: /gang info <Gang>");
                    return false;
                }
                Player player = (Player) sender;
                g = GangManager.getGang(PlayerData.getGang(player));
            }
            if (g == null) {
                sender.sendMessage("§cEither you are not in a gang, or the gang you entered is invalid.");
                return false;
            }
            sender.sendMessage("§b" + g.getDisplayname() + " Info:");
            sender.sendMessage("§7Owner: §e" + Bukkit.getOfflinePlayer(g.getOwner()).getName());
            sender.sendMessage("§7Members:");
            g.getMembers().forEach((id, rank) -> sender.sendMessage("§7- §e" + Bukkit.getOfflinePlayer(id).getName() + "§7: " + rank.getName()));
        }
        return true;
    }

    public void sendUsage(CommandSender sender) {
        if (!enabled) {
            return;
        }
        sender.sendMessage("§cUsage:");
        sender.sendMessage("§7/gang create <Name>");
        sender.sendMessage("§7/gang invite add <Player>");
        sender.sendMessage("§7/gang invite remove <Player>");
        sender.sendMessage("§7/gang join <Gang>");
        sender.sendMessage("§7/gang info [Gang]");
    }
}
