package net.ultradev.prisoncore.utils.display;

import me.clip.voteparty.VoteParty;
import me.clip.voteparty.VotePartyAPI;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.UUID;

public class ScoreboardUtils {
    private static HashMap<UUID, Scoreboard> sbs = new HashMap<>();
    private static HashMap<UUID, String[]> sbsl = new HashMap<>();

    public static void setScoreboard(final Player player, final String title, final String... lines) {
        final UUID id = player.getUniqueId();
        if (!ScoreboardUtils.sbs.containsKey(id)) {
            final ScoreboardManager manager = Bukkit.getScoreboardManager();
            final Scoreboard sb = manager.getNewScoreboard();
            ScoreboardUtils.sbs.put(id, sb);
        }
        if (ScoreboardUtils.sbsl.containsKey(id) && compare(lines, ScoreboardUtils.sbsl.get(id))) {
            return;
        }
        final String[] oldLines = ScoreboardUtils.sbsl.get(id);
        final Scoreboard sb = ScoreboardUtils.sbs.get(player.getUniqueId());
        Objective obj = sb.getObjective("obj");
        if (obj == null) {
            obj = sb.registerNewObjective("obj", "dummy");
        }
        obj.setDisplayName(title);
        for (int i = 0; i < lines.length; ++i) {
            if (lines[i].length() > 40) {
                Debugger.error("Too long line: " + lines[i], "scoreboard");
                lines[i] = "§cError";
            }
            if (oldLines != null) {
                if (lines[i].equals(oldLines[i])) {
                    continue;
                }
                sb.resetScores(oldLines[i]);
            }
            obj.getScore(lines[i]).setScore(lines.length - i);
        }
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(sb);
        ScoreboardUtils.sbsl.put(id, lines);
    }

    public static void updateScoreboard(final Player player) {
        String rankupLine;
        if (Economy.tokens.getBalance(player).compareTo(RankupManager.getRankupCost(player)) < 0) {
            rankupLine = "   §8[" + TextUtils.generateBar('a', '8', 20, Economy.tokens.getBalance(player), RankupManager.getRankupCost(player)) + "§8] §7" + Economy.tokens.getBalance(player).multiply(BigInteger.valueOf(100L)).divide(RankupManager.getRankupCost(player)) + "%";
        } else {
            rankupLine = "   §8[§a::::::::::::::::::::§8] §7100%";
        }
        setScoreboard(player,
                "§c§lRevival§f§lPrison",
                "§1",
                "§6§lRANK",
                "§8» §7Current: §7[" + PlayerData.getRankDisplayName(player) + "§7]",
                "§8» §7Progress:", rankupLine,
                "§2",
                "§6§lGANG",
                "§8» §7Gang: §bNone",
                "§8» §7Power: §b0",
                "§3",
                "§6§lSTATS",
                "§8» §7Tokens: §e" + NumberUtils.toReadableNumber(Economy.tokens.getBalance(player)),
                "§8» §7Vote Party: §d" + VotePartyAPI.getVotes() + "/" + VoteParty.getOptions().getVotesNeeded(),
                "§4"
        );
    }

    public static void updateAll() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    private static boolean compare(final String[] str1, final String[] str2) {
        if (str1.length != str2.length) {
            return false;
        }
        for (int i = 0; i < str1.length; ++i) {
            if (!str1[i].equals(str2[i])) {
                return false;
            }
        }
        return true;
    }

    public static void leave(final Player player) {
        ScoreboardUtils.sbsl.remove(player.getUniqueId());
        ScoreboardUtils.sbs.remove(player.getUniqueId());
    }
}