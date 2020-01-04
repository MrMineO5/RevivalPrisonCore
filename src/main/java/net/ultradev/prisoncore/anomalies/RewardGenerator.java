/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.anomalies;

import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.rewards.KeyReward;
import net.ultradev.prisoncore.rewards.rewards.SocketGemDustReward;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RewardGenerator {
    public static List<Reward> generateRewards(AnomalyType type, double damage) {
        int count = (int) Math.floor((Math.random() + 0.3) * damage / 150);
        List<Reward> ret = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ret.add(generateReward(damage));
        }
        return ret;
    }

    @Nullable
    public static Reward generateReward(double damage) {
        if (damage < 50) {
            return null;
        }
        double r0 = Math.random() * 100.0;
        if (r0 < damage / 25) {
            int am = (int) Math.floor(damage * 100 / Math.ceil(Math.random() * 6));
            return new TokenReward(am);
        }
        if (r0 < 3 * (damage / 50)) {
            double kt = Math.random() * damage / 1300;
            String t = "mythical";
            if (kt < 0.9) {
                t = "legendary";
            }
            if (kt < 0.65) {
                t = "rare";
            }
            int amount = (int) Math.ceil((1 - kt) * damage / 400);
            return new KeyReward(t, amount);
        }
        int dust = (int) Math.floor(damage / 50);
        return new SocketGemDustReward(dust);
    }
}
