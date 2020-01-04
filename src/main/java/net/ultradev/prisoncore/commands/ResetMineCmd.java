/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetMineCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.NO_CONSOLE.get());
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 0) {
            sender.sendMessage("§cUsage: §7/mailbox");
            return true;
        }
        GUIManager.openGUI(player, "mailbox");
        return true;
    }
}
