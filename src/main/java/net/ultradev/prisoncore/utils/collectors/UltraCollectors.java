/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.collectors;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class UltraCollectors {
    public static <K, V> Collector<Map.Entry<K, V>, ?, LinkedHashMap<K, V>> toLinkedHashMap() {
        return new Collector<Map.Entry<K, V>, LinkedHashMap<K, V>, LinkedHashMap<K, V>>() {
            @Override
            public Supplier<LinkedHashMap<K, V>> supplier() {
                return LinkedHashMap::new;
            }

            @Override
            public BiConsumer<LinkedHashMap<K, V>, Map.Entry<K, V>> accumulator() {
                return (map, entry) -> map.put(entry.getKey(), entry.getValue());
            }

            @Override
            public BinaryOperator<LinkedHashMap<K, V>> combiner() {
                return (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                };
            }

            @Override
            public Function<LinkedHashMap<K, V>, LinkedHashMap<K, V>> finisher() {
                return map -> map;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>();
            }
        };
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap() {
        return new Collector<Map.Entry<K, V>, HashMap<K, V>, Map<K, V>>() {
            @Override
            public Supplier<HashMap<K, V>> supplier() {
                return HashMap::new;
            }

            @Override
            public BiConsumer<HashMap<K, V>, Map.Entry<K, V>> accumulator() {
                return (map, entry) -> map.put(entry.getKey(), entry.getValue());
            }

            @Override
            public BinaryOperator<HashMap<K, V>> combiner() {
                return (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                };
            }

            @Override
            public Function<HashMap<K, V>, Map<K, V>> finisher() {
                return map -> map;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>();
            }
        };
    }
}
