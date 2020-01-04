/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.rankup;

import net.ultradev.prisoncore.playerdata.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.LinkedList;

public class RankupManager {
    private static LinkedList<Rank> ranks = new LinkedList<>();

    static {
        ranks.add(new Rank("a", "§a§lA", 1100));
        ranks.add(new Rank("b", "§a§lB", 1650));
        ranks.add(new Rank("c", "§a§lC", 3300));
        ranks.add(new Rank("d", "§a§lD", 7200));
        ranks.add(new Rank("e", "§a§lE", 14400));
        ranks.add(new Rank("f", "§a§lF", 21600));
        ranks.add(new Rank("g", "§a§lG", 31200));
        ranks.add(new Rank("h", "§a§lH", 39000));
        ranks.add(new Rank("i", "§a§lI", 78000));
        ranks.add(new Rank("j", "§6§lJ", 126000));
        ranks.add(new Rank("k", "§6§lK", 168000));
        ranks.add(new Rank("l", "§6§lL", 210000));
        ranks.add(new Rank("m", "§6§lM", 270000));
        ranks.add(new Rank("n", "§6§lN", 315000));
        ranks.add(new Rank("o", "§6§lO", 360000));
        ranks.add(new Rank("p", "§6§lP", 432000));
        ranks.add(new Rank("q", "§6§lQ", 480000));
        ranks.add(new Rank("r", "§6§lR", 600000));
        ranks.add(new Rank("s", "§c§lS", 765000));
        ranks.add(new Rank("t", "§c§lT", 892000));
        ranks.add(new Rank("u", "§c§lU", 1020000));
        ranks.add(new Rank("v", "§c§lV", 1275000));
        ranks.add(new Rank("w", "§c§lW", 2625000));
        ranks.add(new Rank("x", "§c§lX", 5250000));
        ranks.add(new Rank("y", "§c§lY", 9000000));
        ranks.add(new Rank("z", "§c§lZ", 12600000));
        ranks.add(new Rank("star", "§4§l✫", 20000000));
		/*
		for (int i = 1; i <= 50; i++) {
			ranks.add(new Rank("p" + i, "§4§l" + i + "✫", i * 19000000 + 20000000));
		}*/
    }

    @NotNull
    public static String getRankName(int rank) {
        if (rank > 26) {
            return "p" + (rank - 26);
        }
        return ranks.get(rank).name.toUpperCase();
    }

    @Contract(pure = true)
    public static String getRankDisplayName(int rank) {
        if (rank < 0) {
            return "§c§lError (" + rank + ")";
        }
        if (rank > 26) {
            return "§4§l" + (rank - 26) + "✫";
        }
        return ranks.get(rank).displayname;
    }

    public static int getIdOf(String rank) {
        if (rank.matches("([pP])([0-9])+")) {
            return Integer.parseInt(rank.substring(1)) + 26;
        }
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i).name.equalsIgnoreCase(rank)) {
                return i;
            }
        }
        return -1;
    }

    public static BigInteger getRankupCost(OfflinePlayer player) {
        return getRankupCost(PlayerData.getRank(player));
    }

    @Contract(pure = true)
    public static BigInteger getRankupCost(int rank) {
        if (rank > 26) {
            return BigInteger.valueOf(rank - 26).multiply(BigInteger.valueOf(110000)).multiply(BigInteger.valueOf(122 + rank * 3));
        }
        return ranks.get(rank).cost;
    }

    /**
     * A data class that stores information about a rank
     */
    public static class Rank {
        public String name;
        public String displayname;
        BigInteger cost;

        Rank(String name, String displayName, long cost) {
            this.name = name;
            this.displayname = displayName;
            this.cost = BigInteger.valueOf(cost);
        }
    }
}
