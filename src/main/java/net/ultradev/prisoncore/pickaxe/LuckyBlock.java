package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.RewardApplicator;
import net.ultradev.prisoncore.rewards.rewards.KeyReward;
import net.ultradev.prisoncore.rewards.rewards.PlatinumTokenReward;
import net.ultradev.prisoncore.rewards.rewards.SocketGemDustReward;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.entity.Player;

public class LuckyBlock {
    public static RewardApplicator getLuckBlockReward(final int favoredLevel, final boolean message, final double favoredSocket) {
        final boolean more = MathUtils.isRandom(favoredSocket, 100.0);
        final double i = MathUtils.random(0.0, 100.0);
        if (i < 60.0) {
            long tokenCount = (favoredLevel + 1) * 5;
            if (more) {
                tokenCount = (long) Math.floor(tokenCount * MathUtils.random(1.5, 2.0));
            }
            return new TokenReward(tokenCount).getApplicator(message ? ((name, amount) -> "§eLucky Block » §7You found §e" + amount + " Tokens§7!") : null);
        }
        if (i < 95.0) {
            String key = "mine";
            int amount2 = Math.floorDiv(favoredLevel, 200) + 1;
            final double r = MathUtils.random(0.0, 100.0);
            if (favoredLevel >= 500) {
                key = "legendary";
            }
            if (r <= 85.0) {
                if (favoredLevel >= 100) {
                    key = "rare";
                }
                amount2 += 2;
            }
            if (r <= 65.0) {
                key = "mine";
                ++amount2;
            }
            if (more) {
                amount2 = (int) Math.floor(amount2 * MathUtils.random(1.3, 1.7));
            }
            return new KeyReward(key, amount2).getApplicator(message ? ((name, am) -> "§eLucky Block » §7You found " + am + "x §e" + name + "§7!") : null);
        }
        int dust = Math.floorDiv(favoredLevel, 200) + 2;
        if (more) {
            dust = (int) Math.floor(dust * MathUtils.random(1.3, 1.7));
        }
        return new SocketGemDustReward(dust).getApplicator(message ? ((name, am) -> "§eLucky Block» §7You found " + am + " Socket Gem Dust") : null);
    }

    public static void runLuckBlockReward(final Player player, final int favoredLevel, final double favoredSocket) {
        getLuckBlockReward(favoredLevel, PlayerData.getSetting(player, "lb_message"), favoredSocket).applyReward(player);
    }

    public static RewardApplicator getSuperLuckBlockReward(final int favoredLevel, final boolean message, final double favoredSocket) {
        final boolean more = MathUtils.isRandom(favoredSocket, 100.0);
        final double i = MathUtils.random(0.0, 100.0);
        if (i < 60.0) {
            long tokenCount = favoredLevel * 5;
            if (more) {
                tokenCount = (long) Math.floor(tokenCount * MathUtils.random(1.5, 2.0));
            }
            return new TokenReward(tokenCount).getApplicator(message ? ((name, amount) -> "§cSuper Lucky Block » §7You found §e" + amount + " Tokens§7!") : null);
        }
        if (i < 90.0) {
            String key = "mine";
            int amount2 = Math.floorDiv(favoredLevel, 200) - 1;
            final double r = MathUtils.random(0.0, 100.0);
            if (favoredLevel >= 500) {
                key = "legendary";
            }
            if (r <= 85.0) {
                if (favoredLevel >= 100) {
                    key = "rare";
                }
                amount2 += 2;
            }
            if (r <= 65.0) {
                key = "mine";
                ++amount2;
            }
            if (more) {
                amount2 = (int) Math.floor(amount2 * MathUtils.random(1.3, 1.7));
            }
            return new KeyReward(key, amount2).getApplicator(message ? ((name, am) -> "§cSuper Lucky Block » §7You found " + am + "x §e" + name + "§7!") : null);
        }
        if (i < 95.0) {
            int dust = Math.floorDiv(favoredLevel, 100) + 5;
            if (more) {
                dust = (int) Math.floor(dust * MathUtils.random(1.3, 1.7));
            }
            return new SocketGemDustReward(dust).getApplicator(message ? ((name, am) -> "§cSuper Lucky Block» §7You found " + am + " Socket Gem Dust") : null);
        }
        int platinumTokens = Math.floorDiv(favoredLevel, 50) + 10;
        if (more) {
            platinumTokens = (int) Math.floor(platinumTokens * MathUtils.random(1.5, 2.0));
        }
        return new PlatinumTokenReward(platinumTokens).getApplicator(message ? ((name, amount) -> "§cSuper Lucky Block » §7You found §b" + amount + " Platinum Tokens§7!") : null);
    }

    public static void runSuperLuckBlockReward(final Player player, final int favoredLevel, final double luckySocket) {
        getSuperLuckBlockReward(favoredLevel, PlayerData.getSetting(player, "lb_message"), luckySocket).applyReward(player);
    }
}