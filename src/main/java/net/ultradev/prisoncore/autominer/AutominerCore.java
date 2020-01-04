/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutominerCore implements Listener {
    @Getter
    private static AutominerCore core;
    Map<Player, NPC> npcs = new HashMap<>();

    public AutominerCore(Plugin plugin) {
        core = this;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!npcs.containsKey(player)) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Miner");
                npcs.put(player, npc);
                npc.addTrait(new AutoMinerTrait());
            }
        }
        List<Integer> remove = new ArrayList<>();
        CitizensAPI.getNPCRegistry().forEach((npc) -> {
            if (npc.getName().equalsIgnoreCase("Miner")) {
                if (!npc.hasTrait(AutoMinerTrait.class)) {
                    remove.add(npc.getId());
                }
            }
        });
        for (int i : remove) {
            CitizensAPI.getNPCRegistry().getById(i).destroy();
        }
    }

    public static void deInit() {
        core.deinit();
    }

    private void deinit() {
        Debugger.log("Deinitializing the AutoMiner Core", "autominerCore");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Debugger.log("Saving data for: " + player.getName(), "autominerCore");
            AutoMinerAI model = AutoMinerUtils.getModel(player);
            model.saveConfig();
            model.getNpc().destroy();
            npcs.remove(player);
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!npcs.containsKey(player)) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Miner");
            npcs.put(player, npc);
            npc.addTrait(new AutoMinerTrait());
        }
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent e) {
        AutoMinerAI model = AutoMinerUtils.getModel(e.getPlayer());
        model.saveConfig();
        model.getNpc().destroy();
        npcs.remove(e.getPlayer());
    }
}
