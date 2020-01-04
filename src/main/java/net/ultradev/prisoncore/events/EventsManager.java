/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class EventsManager {
    public static void registerEvents(Plugin pl) {
        System.out.println("[EventManager] Registering Join/Leave events");
        Bukkit.getPluginManager().registerEvents(new JoinLeaveEvents(), pl);
        System.out.println("[EventManager] Registering Chat events");
        Bukkit.getPluginManager().registerEvents(new ChatEvents(), pl);
        System.out.println("[EventManager] Registering Crate events");
        Bukkit.getPluginManager().registerEvents(new CrateEvents(), pl);
        System.out.println("[EventManager] Registering Mine Wand events");
        Bukkit.getPluginManager().registerEvents(new MineWandEvents(), pl);
        System.out.println("[EventManager] Registering Pickaxe events");
        Bukkit.getPluginManager().registerEvents(new PickaxeEvents(), pl);
        System.out.println("[EventManager] Registering ItemScript events");
        Bukkit.getPluginManager().registerEvents(new GUIClickEvent(), pl);
        System.out.println("[EventManager] Registering Bomb events");
        Bukkit.getPluginManager().registerEvents(new BombEvents(), pl);
        System.out.println("[EventManager] Registering Right Click events");
        Bukkit.getPluginManager().registerEvents(new RightClickEvents(), pl);
        Debugger.log("Registering Riddle events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new RiddleEvents(), pl);
        Debugger.log("Registering Portal events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new PortalEvents(), pl);
        Debugger.log("Registering TreasureHunt events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new TreasureHuntEvents(), pl);
        Debugger.log("Registering Anomaly events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new AnomalyEvents(), pl);
        Debugger.log("Registering KeyVault events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new KeyVaultEvents(), pl);
        Debugger.log("Registering Tinkerer events", "EventManager");
        Bukkit.getPluginManager().registerEvents(new TinkererEvents(), pl);
    }
}
