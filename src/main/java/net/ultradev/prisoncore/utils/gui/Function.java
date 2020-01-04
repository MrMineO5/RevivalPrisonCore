/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class Function {
    public String[] args;
    private String function;

    Function(String function, String... args) {
        this.function = function;
        this.args = args;
    }

    static Function parse(String[] str) {
        String function = str[0];
        String[] args = new String[str.length - 1];
        System.arraycopy(str, 1, args, 0, str.length - 1);
        return new Function(function, args);
    }

    String encodeInstruction() {
        StringBuilder ret = new StringBuilder(ItemScriptEncoder.START_FUNCTION_BLOCK);
        ret.append(function);
        if (args.length > 0) {
            for (String arg : args) {
                ret.append(ItemScriptEncoder.IN_FUNCTION_SEPERATOR).append(ItemScriptEncoder.START_ARG_BLOCK).append(arg).append(ItemScriptEncoder.END_ARG_BLOCK);
            }
        }
        ret.append(ItemScriptEncoder.END_FUNCTION_BLOCK);
        return ret.toString();
    }

    public void run(Player player, InventoryClickEvent event, Inventory inv) {
        ItemScriptFunction.run(function, player, event, inv, args);
    }
}