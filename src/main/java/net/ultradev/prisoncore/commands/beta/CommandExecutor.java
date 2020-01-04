/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {
    boolean execute(CommandSender sender, Object... args);
}
