/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.riddle;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.plugins.FaweAPI;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalRiddle {
    private static Map<UUID, String> answers = new HashMap<UUID, String>();

    private static boolean portalOpen = false;

    public static boolean isOpen() {
        return portalOpen;
    }

    public static void makeAttempt(Player p, String a) {
        if (CooldownUtils.isCooldown(p, "riddle_portal")) {
            Debugger.log("Replacing...", "riddle_portal");
            answers.put(p.getUniqueId(), a);
        } else {
            Debugger.log("Adding...", "riddle_portal");
            String str = answers.get(p.getUniqueId());
            Debugger.log("New string: " + str + a, "riddle_portal");
            answers.put(p.getUniqueId(), str + a);
        }
        CooldownUtils.setCooldown(p, "riddle_portal", 5);
        if (portalOpen) {
            Debugger.log("Portal already open.", "riddle_portal");
            return;
        }
        Debugger.log("Checking answer...", "riddle_portal");
        if (answers.get(p.getUniqueId()).equalsIgnoreCase("gbgbgwd")) {
            Debugger.log("Opening portal...", "riddle_portal");
            openPortal();
        }
    }

    private static void openPortal() {
        portalOpen = true;
        Bukkit.broadcastMessage("§dA portal has opened...");
        World w = Bukkit.getWorld("spawn");
        Debugger.log("Setting blocks...", "riddle_portal");
        EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(w.getName())).fastmode(true).build();
        Vector min = new Vector(-543, 165, -355);
        Vector max = new Vector(-541, 168, -355);
        try {
            editSession.setBlocks(new CuboidRegion(min, max), new SingleBlockPattern(new BaseBlock(Material.PORTAL.getId())));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        editSession.flushQueue();
        Scheduler.scheduleSyncDelayedTask(() -> {
            Bukkit.broadcastMessage("§dA portal is closing...");
            portalOpen = false;
            Debugger.log("Setting blocks...", "riddle_portal");
            EditSession editSession2 = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(w.getName())).fastmode(true).build();
            try {
                editSession.setBlocks(new CuboidRegion(min, max), new SingleBlockPattern(new BaseBlock(Material.AIR.getId())));
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
            editSession2.flushQueue();
        }, 10);
    }

    public static Location getDestination() {
        return new Location(Bukkit.getWorld("spawn"), -535, 200, -379);
    }
}
