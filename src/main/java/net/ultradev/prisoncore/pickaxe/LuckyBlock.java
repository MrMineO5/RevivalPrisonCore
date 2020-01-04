/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.RewardApplicator;
import net.ultradev.prisoncore.rewards.rewards.KeyReward;
import net.ultradev.prisoncore.rewards.rewards.PlatinumTokenReward;
import net.ultradev.prisoncore.rewards.rewards.SocketGemDustReward;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.entity.Player;

import java.util.Random;

public class LuckyBlock {
    public static RewardApplicator getLuckBlockReward(int favoredLevel, boolean message, double favoredSocket) {
        boolean more = MathUtils.isRandom(favoredSocket, 100.0);
        double i = MathUtils.random(0.0, 100.0);
        Random rand = new Random();
        if (i < 60.0) {
            long tokenCount = (favoredLevel + 1) * 5;
            if (more) {
                tokenCount = (long) Math.floor(tokenCount * MathUtils.random(1.5, 2.0));
            }
            return new TokenReward(tokenCount).getApplicator(message ? (name, amount) -> "§eLucky Block » §7You found §e" + amount + " Tokens§7!" : null);
        }
        if (i < 95.0) {
            String key = "mine";
            int amount = Math.floorDiv(favoredLevel, 200) + 1;
            double r = MathUtils.random(0.0, 100.0);
            if (favoredLevel >= 500) {
                key = "legendary";
            }
            if (r <= 85) {
                if (favoredLevel >= 100) {
                    key = "rare";
                }
                amount += 2;
            }
            if (r <= 65.0) {
                key = "mine";
                amount += 1;
            }
            if (more) {
                amount = (int) Math.floor(amount * MathUtils.random(1.3, 1.7));
            }
            return new KeyReward(key, amount).getApplicator(message ? (name, am) -> "§eLucky Block » §7You found " + am + "x §e" + name + "§7!" : null);
        }
        int dust = Math.floorDiv(favoredLevel, 200) + 2;
        if (more) {
            dust = (int) Math.floor(dust * MathUtils.random(1.3, 1.7));
        }
        return new SocketGemDustReward(dust).getApplicator(message ? (name, am) -> "§eLucky Block» §7You found " + am + " Socket Gem Dust" : null);
    }

    public static void runLuckBlockReward(Player player, int favoredLevel, double favoredSocket) {
        getLuckBlockReward(favoredLevel, PlayerData.getSetting(player, "lb_message"), favoredSocket).applyReward(player);
    }

    public static RewardApplicator getSuperLuckBlockReward(int favoredLevel, boolean message, double favoredSocket) {
        boolean more = MathUtils.isRandom(favoredSocket, 100.0);
        double i = MathUtils.random(0.0, 100.0);
        Random rand = new Random();
        if (i < 60.0) {
            long tokenCount = favoredLevel * 5;
            if (more) {
                tokenCount = (long) Math.floor(tokenCount * MathUtils.random(1.5, 2.0));
            }
            return new TokenReward(tokenCount).getApplicator(message ? (name, amount) -> "§cSuper Lucky Block » §7You found §e" + amount + " Tokens§7!" : null);
        }
        if (i < 90.0) {
            String key = "mine";
            int amount = Math.floorDiv(favoredLevel, 200) - 1;
            double r = MathUtils.random(0.0, 100.0);
            if (favoredLevel >= 500) {
                key = "legendary";
            }
            if (r <= 85) {
                if (favoredLevel >= 100) {
                    key = "rare";
                }
                amount += 2;
            }
            if (r <= 65.0) {
                key = "mine";
                amount += 1;
            }
            if (more) {
                amount = (int) Math.floor(amount * MathUtils.random(1.3, 1.7));
            }
            return new KeyReward(key, amount).getApplicator(message ? (name, am) -> "§cSuper Lucky Block » §7You found " + am + "x §e" + name + "§7!" : null);
        }
        if (i < 95.0) {
            int dust = Math.floorDiv(favoredLevel, 100) + 5;
            if (more) {
                dust = (int) Math.floor(dust * MathUtils.random(1.3, 1.7));
            }
            return new SocketGemDustReward(dust).getApplicator(message ? (name, am) -> "§cSuper Lucky Block» §7You found " + am + " Socket Gem Dust" : null);
        }
        int platinumTokens = Math.floorDiv(favoredLevel, 50) + 10;
        if (more) {
            platinumTokens = (int) Math.floor(platinumTokens * MathUtils.random(1.5, 2.0));
        }
        return new PlatinumTokenReward(platinumTokens).getApplicator(message ? (name, amount) -> "§cSuper Lucky Block » §7You found §b" + amount + " Platinum Tokens§7!" : null);
    }

    public static void runSuperLuckBlockReward(Player player, int favoredLevel, double luckySocket) {
        getSuperLuckBlockReward(favoredLevel, PlayerData.getSetting(player, "lb_message"), luckySocket).applyReward(player);
    }
}
