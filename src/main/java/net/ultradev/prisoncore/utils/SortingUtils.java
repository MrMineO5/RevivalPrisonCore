/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SortingUtils {
    public static <T> LinkedHashMap<T, BigInteger> sort(Map<T, BigInteger> map) {
        return map.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
}
