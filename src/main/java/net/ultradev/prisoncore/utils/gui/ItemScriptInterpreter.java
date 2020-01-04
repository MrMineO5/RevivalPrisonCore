/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

class ItemScriptInterpreter {
    static void interpretItemScript(String script, Player player, InventoryClickEvent event, Inventory inv) {
        Debugger.log("Starting up interpreter...", "itemscript_interpreter");
        Debugger.log("Decoding functions...", "itemscript_interpreter");
        Function[] funcs = ItemScriptEncoder.decodeInstructions(script);
        Debugger.log("Running functions...", "itemscript_interpreter");
        for (int i = 0; i < funcs.length; i++) {
            Debugger.log("Running function " + i, "itemscript_interpreter");
            funcs[i].run(player, event, inv);
        }
    }
}
