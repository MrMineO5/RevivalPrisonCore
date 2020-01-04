/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.multipliers;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.collectors.UltraCollectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class MultiplierData {
    private Map<Multiplier.MultiplierType, Multiplier> multis = new HashMap<>();

    boolean activateMulti(@NotNull Multiplier multi) {
        Multiplier.MultiplierType type = multi.getType();
        if (this.multis.containsKey(type)) {
            if (type.equals(Multiplier.MultiplierType.AM_KEY) || type.equals(Multiplier.MultiplierType.AM_TOKEN)) {
                if (this.multis.get(type).getMulti() == multi.getMulti()) {
                    this.multis.get(type).addTime(multi.getRemainingTime());
                    return true;
                }
            }
            return false;
        }
        multi.start();
        this.multis.put(multi.getType(), multi);
        return true;
    }

    void update(Player player) {
        if (this.multis.isEmpty())
            return;
        this.multis = this.multis.entrySet().stream()
                .filter((e) -> {
                    if (!e.getValue().isActive()) {
                        e.getValue().expire(player);
                        return false;
                    }
                    e.getValue().render(player);
                    return true;
                })
                .collect(UltraCollectors.toMap());
    }

    double getMultiplier(Multiplier.MultiplierType type) {
        if (!this.multis.containsKey(type))
            return 0.0D;
        return this.multis.get(type).getMulti();
    }

    Multiplier getMultiplierReal(Multiplier.MultiplierType type) {
        if (!this.multis.containsKey(type))
            return null;
        return this.multis.get(type);
    }

    public void leave(Player pl) {
        this.multis.forEach((k, v) -> {
            Multiplier ne = new Multiplier(v.getType(), v.getRemainingTime(), v.getMulti());
            PlayerData.addMailbox(pl, ne.getItem());
        });
        this.multis.clear();
    }
}
