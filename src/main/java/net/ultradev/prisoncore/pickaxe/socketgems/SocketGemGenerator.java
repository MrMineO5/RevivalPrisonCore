/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import net.ultradev.prisoncore.utils.math.MathUtils;

public class SocketGemGenerator {
    public static SocketGem generateSocketGem(SocketGemTier rarity) {
        SocketGemType type = SocketGemType.random(rarity.equals(SocketGemTier.EPIC));

        return new SocketGem(type, rarity, randomPercent(type, rarity));
    }

    private static double randomPercent(SocketGemType type, SocketGemTier tier) {
        double pow = 0.0;
        double max = 100.0;
        switch (tier) {
            case EPIC:
                return Math.round(10 * MathUtils.random(type.getMinPercent(), type.getMaxPercent())) / 10.0;
            case COMMON:
                pow = 8.0;
                max = 25.0;
                break;
            case RARE:
                pow = 5.0;
                max = 50.0;
                break;
            case LEGENDARY:
                pow = 4.0;
                max = 75.0;
                break;
            case MYTHICAL:
                pow = 3.0;
                max = 99.0;
                break;
        }
        double rco = Math.pow(Math.random(), pow);
        double min = type.getMinPercent();
        max = type.getMaxPercent() * max / 100.0;
        return Math.round(10 * ((max - min) * rco + min)) / 10.0;
    }
}
