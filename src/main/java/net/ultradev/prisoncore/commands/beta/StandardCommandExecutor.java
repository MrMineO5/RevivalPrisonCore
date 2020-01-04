/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface StandardCommandExecutor {
    boolean execute(CommandSender sender, Command cmd, String label, String... args);
}
