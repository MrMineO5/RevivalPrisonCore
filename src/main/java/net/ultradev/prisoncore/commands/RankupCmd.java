/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console can't rank up!");
            return false;
        }
        Player player = (Player) sender;
        if (PlayerData.canAffordRankup(player)) {
            PlayerData.rankup(player);
            Bukkit.broadcastMessage(
                    "§a» §e" + player.getName() + " ranked up to §7[" + PlayerData.getRankDisplayName(player) + "§7]");
        } else {
            player.sendMessage("§cInsufficient funds! You need §e"
                    + NumberUtils.formatFull(RankupManager.getRankupCost(player)) + " §cto rank up!");
        }
        return true;
    }
}
