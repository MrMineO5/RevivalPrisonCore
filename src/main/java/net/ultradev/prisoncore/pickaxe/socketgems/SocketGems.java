/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

public class SocketGems {
    public static int getSalvageReturns(SocketGemTier type) {
        switch (type) {
            case COMMON:
                return 0;
            case RARE:
                return 3;
            case LEGENDARY:
                return 8;
            case MYTHICAL:
                return 25;
            case EPIC:
                return 75;
        }
        return 0;
    }

    public static int getDismantleReturns(SocketGemTier type) {
        switch (type) {
            case COMMON:
                return 1;
            case RARE:
                return 5;
            case LEGENDARY:
                return 20;
            case MYTHICAL:
                return 60;
            case EPIC:
                return 300;
        }
        return 0;
    }
}
