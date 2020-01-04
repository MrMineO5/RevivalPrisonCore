/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import org.bukkit.block.Block;

import java.util.List;

public class DefaultBlockBreaker extends BlockBreaker {
    public DefaultBlockBreaker(AutoMinerAI model) {
        super(model);
    }

    @Override
    List<Block> narrowScan() {
        return scan(model.getArmReach(), model.getArmReach());
    }
}
