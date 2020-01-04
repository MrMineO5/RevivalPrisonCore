/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.text;

public enum Messages {
    NO_CONSOLE("§cThat command can only be used by players!"),
    PLAYER_NOT_ONLINE("§cThat player is not online"),
    INVALID_INTEGER("§cInvalid Integer: %s"),
    INVALID_ENCHANT("§cInvalid Enchantment: %s"),
    NO_PERMISSION_COMMAND("§cThat command requires %s§c rank or above."),
    AUTOSELL_CONSUME("§7You redeemed §e%s§7 of Auto Sell time!"),

    AUTOMINER_CONSUME("§7You redeemed §e%s§7 of Auto Miner time!"),
    AUTOMINER_RAN_OUT_OF_TIME("§cYou have run out of autominer time."),

    MINE_RESET("§eThis mine is resetting, so you were teleported to the surface.");

    private String msg;

    Messages(String msg) {
        this.msg = msg;
    }

    public String get(Object... objects) {
        return (String.format(this.msg, objects));
    }
}
