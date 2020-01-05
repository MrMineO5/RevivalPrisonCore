package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PickaxeLore {
    public static double getPercent(final BigInteger xp, final BigInteger maxxp) {
        return xp.multiply(BigInteger.valueOf(1000L)).divide(maxxp).doubleValue() / 10.0;
    }

    public static List<String> generateLevelLines(final BigInteger xp, final int level) {
        final BigInteger maxxp = getXpRequired(level);
        final List<String> lines = new ArrayList<String>();
        lines.add("§b§lLevel " + level);
        lines.add("§8[" + TextUtils.generateBar('a', '7', 50, xp, maxxp) + "§8] §f" + getPercent(xp, maxxp) + "§7%");
        lines.add("§7(§f" + NumberUtils.formatFull(xp) + "§7 / " + NumberUtils.formatFull(maxxp) + "§7)");
        return lines;
    }

    public static BigInteger getXpRequired(final int level) {
        return BigInteger.valueOf(1000L).multiply(BigDecimal.valueOf(1.3).pow(level).toBigInteger());
    }

    static List<String> generateSocketLines(final List<SocketGem> gems) {
        final List<String> lines = new ArrayList<>();
        for (final SocketGem sg : gems) {
            if (sg == null) {
                lines.add("§a<Empty Slot>");
            } else {
                lines.add(sg.getItem().getItemMeta().getDisplayName() + " (" + sg.getPercent() + ")");
            }
        }
        return lines;
    }
}