/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta.commandtypes;

import net.ultradev.prisoncore.commands.beta.Command;
import net.ultradev.prisoncore.commands.beta.RequiredRank;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageCommand extends Command {

    public MessageCommand(String name, String message, RequiredRank req, JavaPlugin plugin) {
        super(name, "", true, req, plugin, (sender, args) -> {
            sender.sendMessage(message);
            return true;
        });
    }
}
