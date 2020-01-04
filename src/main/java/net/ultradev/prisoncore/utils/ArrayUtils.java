/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import java.util.List;

public class ArrayUtils {
    @SafeVarargs
    public static <T> T[] mergeAll(T[]... arrays) {
        T[] fin = null;
        for (T[] arr : arrays) {
            fin = org.apache.commons.lang3.ArrayUtils.addAll(fin, arr);
        }
        return fin;
    }

    public static <T> void toArray(T[] arr, List<T> str) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = str.get(i);
        }
    }
}
