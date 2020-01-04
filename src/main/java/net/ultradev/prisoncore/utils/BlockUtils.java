/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.ultradev.prisoncore.utils.plugins.FaweAPI;
import org.bukkit.Material;
import org.bukkit.World;

public class BlockUtils {
    public static void fillArea(World w, Vector loc1, Vector loc2) {
        EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(w.getName())).build();
        Vector[] minMax = getMinMax(loc1, loc2);
        Vector min = minMax[0];
        Vector max = minMax[1];
        try {
            editSession.setBlocks(new CuboidRegion(min, max), new SingleBlockPattern(new BaseBlock(Material.PORTAL.getId())));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        editSession.flushQueue();
    }

    private static Vector[] getMinMax(Vector loc1, Vector loc2) {
        int x1 = loc1.getBlockX();
        int x2 = loc2.getBlockX();
        int y1 = loc1.getBlockY();
        int y2 = loc2.getBlockY();
        int z1 = loc1.getBlockZ();
        int z2 = loc2.getBlockZ();

        int maxX = Math.max(x1, x2);
        int minX = Math.min(x1, x2);
        int maxY = Math.max(y1, y2);
        int minY = Math.min(y1, y2);
        int maxZ = Math.max(z1, z2);
        int minZ = Math.min(z1, z2);

        return new Vector[]{
                new Vector(minX, minY, minZ),
                new Vector(maxX, maxY, maxZ)
        };
    }
}
