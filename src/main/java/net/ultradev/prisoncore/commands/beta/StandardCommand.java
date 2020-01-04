/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import lombok.Getter;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StandardCommand {
    @Getter
    private String name;
    private RequiredRank rank;
    private StandardCommandExecutor executor;

    public StandardCommand(String name, String usage, boolean console, RequiredRank rank, JavaPlugin plugin, StandardCommandExecutor executor) {
        this.name = name;
        this.rank = rank;
        this.executor = executor;
        plugin.getCommand(name).setExecutor((sender, cmd, label, args) -> {
            if (!rank.hasRank(sender)) {
                sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get(rank.getPrefix()));
                return true;
            }
            if (!console) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.NO_CONSOLE.get());
                    return true;
                }
            }
            return executor.execute(sender, cmd, label, args);
        });
    }
}
