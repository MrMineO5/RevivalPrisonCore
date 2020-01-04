/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import net.ultradev.prisoncore.utils.collectors.UltraCollectors;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MapSerializers {
    public static String serialize(Map<UUID, Double> map) {
        return map.entrySet().stream()
                .map((en) -> en.getKey().toString() + "|" + en.getValue())
                .collect(Collectors.joining("#"));
    }

    public static Map<UUID, Double> deserialize(String in) {
        return Arrays.stream(in.split("#"))
                .map(str -> {
                    String[] sp = str.split("\\|");
                    return new AbstractMap.SimpleEntry<UUID, Double>(UUID.fromString(sp[0]), Double.parseDouble(sp[1]));
                })
                .collect(UltraCollectors.toMap());
    }
}
