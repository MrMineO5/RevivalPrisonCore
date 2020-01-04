/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockBreaker {
    AutoMinerAI model;

    public BlockBreaker(AutoMinerAI model) {
        this.model = model;
    }

    abstract List<Block> narrowScan();

    public List<Block> wideScan() {
        return scan(model.getWideScanRadius(), model.getArmReach());
    }

    public List<Block> scan(int radiusXZ, int radiusY) {
        Location npc = model.getNpc().getEntity().getLocation();
        Mine m = MineManager.getMineAt(npc);
        List<Block> result = new ArrayList<>();
        for (int i = -radiusXZ; i <= radiusXZ; i++) {
            if (m != null) {
                int x = npc.getBlockX() + i;
                if (x < m.getMinX() || x > m.getMaxX()) {
                    continue;
                }
            }
            for (int j = -radiusY; j <= radiusY; j++) {
                if (m != null) {
                    int y = npc.getBlockY() + j;
                    if (y < m.getMinY() || y > m.getMaxY()) {
                        continue;
                    }
                }
                for (int k = -radiusXZ; k <= radiusXZ; k++) {
                    if (m != null) {
                        int z = npc.getBlockZ() + k;
                        if (z < m.getMinZ() || z > m.getMaxZ()) {
                            continue;
                        }
                    }
                    assert model.getNpc() != null;
                    Block block = npc.getBlock().getRelative(i, j, k);
                    if (block.getType().equals(Material.AIR))
                        continue;
                    if (m == null) {
                        m = MineManager.getMineAt(block.getLocation());
                        if (m != null) {
                            result.add(block);
                        }
                        continue;
                    }
                    result.add(block);
                }
            }
        }
        return result;
    }
}
