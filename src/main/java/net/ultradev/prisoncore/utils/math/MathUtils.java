/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.math;

import java.util.Random;

public class MathUtils {
    private static Random rand = new Random();

    public static int random(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double random(double min, double max) {
        return rand.nextDouble() * (max - min) + min;
    }

    public static int roundRand(double i) {
        int am = (int) Math.floor(i);
        double ret = i - am;
        if (ret >= random(0.0, 1.0)) {
            am++;
        }
        return am;
    }

    public static boolean isRandom(double i, double max) {
        return (i >= random(0.0, max));
    }

    public static int weightedRandom(int min, int max, int weight) {
        int num = min;
        int nmax = max - min;
        for (int i = 0; i < weight; i++) {
            num += Math.floor(Math.random() * (nmax / weight));
        }
        return num;
    }
}
