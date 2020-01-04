/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import lombok.Getter;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Command {
    @Getter
    private String name;
    private RequiredRank rank;
    private Class<?>[] arguments;
    private CommandExecutor executor;

    public Command(String name, String usage, boolean console, RequiredRank rank, JavaPlugin plugin, CommandExecutor executor, Class<?>... args) {
        this.name = name;
        this.rank = rank;
        this.executor = executor;
        this.arguments = args;
        plugin.getCommand(name).setExecutor((commandSender, command, label, strings) -> {
            if (!rank.hasRank(commandSender)) {
                commandSender.sendMessage(Messages.NO_PERMISSION_COMMAND.get(rank.getPrefix()));
                return true;
            }
            if (!validate(strings)) {
                commandSender.sendMessage(getError(strings));
                commandSender.sendMessage("§cUsage: /" + name.toLowerCase() + " " + usage);
                return true;
            }
            if (!console) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Messages.NO_CONSOLE.get());
                    return true;
                }
            }
            return executor.execute(commandSender, Validator.getValues(strings, arguments));
        });
    }

    public boolean validate(String... in) {
        return Validator.validate(in, arguments);
    }

    public String getError(String... in) {
        return Validator.getError(in, arguments);
    }
}
