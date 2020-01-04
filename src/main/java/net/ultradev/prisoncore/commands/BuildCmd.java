/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.playerdata.StaffRank;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildCmd implements CommandExecutor {
    public static List<UUID> buildOn = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be used from console.");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("ultraprison.build")) {
            player.sendMessage(Messages.NO_PERMISSION_COMMAND.get(StaffRank.ADMIN));
            return false;
        }
        if (buildOn.contains(player.getUniqueId())) {
            player.sendMessage("§7Build mode disabled.");
            buildOn.remove(player.getUniqueId());
        } else {
            player.sendMessage("§7Build mode enabled.");
            buildOn.add(player.getUniqueId());
        }
        return true;
    }
}
