/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.multipliers;

import net.ultradev.prisoncore.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiplierManager {
    private static Map<UUID, MultiplierData> datas = new HashMap<>();

    public static void startLoop() {
        Scheduler.scheduleSyncRepeatingTask(() -> {
            if (datas.isEmpty()) {
                return;
            }
            for (UUID id : datas.keySet()) {
                Player player = Bukkit.getPlayer(id);
                assert player != null;
                datas.get(id).update(player);
            }
        }, 1, 1);
    }

    @NotNull
    public static MultiplierData getData(@NotNull Player player) {
        UUID id = player.getUniqueId();
        if (!datas.containsKey(id)) {
            MultiplierData data = new MultiplierData();
            datas.put(id, data);
        }
        return datas.get(id);
    }

    public static boolean activateMulti(Player player, Multiplier multi) {
        return getData(player).activateMulti(multi);
    }

    public static double getMultiplier(Player player, Multiplier.MultiplierType type) {
        return getData(player).getMultiplier(type);
    }

    public static Multiplier getMultiplierReal(Player player, Multiplier.MultiplierType type) {
        return getData(player).getMultiplierReal(type);
    }

    public static void leave(Player player) {
        getData(player).leave(player);
        datas.remove(player.getUniqueId());
    }
}