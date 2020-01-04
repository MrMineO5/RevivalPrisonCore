/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.leaderboards;

import lombok.Getter;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.SortingUtils;
import net.ultradev.prisoncore.utils.Synchronizer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class LeaderBoards {
    public static class LeaderBoardElement {
        @Getter
        private LinkedHashMap<UUID, BigInteger> sorted;
        private Supplier<Map<UUID, BigInteger>> mapSupplier;

        public LeaderBoardElement(Supplier<Map<UUID, BigInteger>> mapSupplier) {
            this.mapSupplier = mapSupplier;
        }

        public void sort() {
            Map<UUID, BigInteger> map = mapSupplier.get();
            sorted = SortingUtils.sort(map);
        }
    }

    @Getter
    private static Map<String, LeaderBoardElement> elementMap = new HashMap<>();
    static {
        elementMap.put("tokens", new LeaderBoardElement(() -> {
            Map<UUID, BigInteger> m = new HashMap<>();
            for (UUID id : PlayerData.getDatas()) {
                m.put(id, Economy.tokens.getBalance(id));
            }
            return m;
        }));
        elementMap.put("ranks", new LeaderBoardElement(() -> {
            Map<UUID, BigInteger> m = new HashMap<>();
            for (UUID id : PlayerData.getDatas()) {
                m.put(id, BigInteger.valueOf(PlayerData.getRank(id)));
            }
            return m;
        }));
        elementMap.put("pickaxeLevel", new LeaderBoardElement(() -> {
            Map<UUID, BigInteger> m = new HashMap<>();
            for (UUID id : PlayerData.getDatas()) {
                m.put(id, BigInteger.valueOf(PlayerData.getPickaxeLevel(id)));
            }
            return m;
        }));
        elementMap.put("playtime", new LeaderBoardElement(() -> {
            Map<UUID, BigInteger> m = new HashMap<>();
            for (UUID id : PlayerData.getDatas()) {
                m.put(id, PlayerData.getPlayTime(id));
            }
            return m;
        }));
    }

    public static void init() {
        Scheduler.scheduleSyncRepeatingTask(LeaderBoards::sort, 1, 300);
    }

    private static void sort() {
        Synchronizer.desynchronize(() -> elementMap.values().forEach(LeaderBoardElement::sort));
    }
}
