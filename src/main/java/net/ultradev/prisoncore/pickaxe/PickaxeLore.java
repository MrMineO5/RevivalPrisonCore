/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PickaxeLore {
    public static double getPercent(BigInteger xp, BigInteger maxxp) {
        return xp.multiply(BigInteger.valueOf(1000)).divide(maxxp).doubleValue() / 10.0;
    }

    public static List<String> generateLevelLines(BigInteger xp, int level) {
        BigInteger maxxp = getXpRequired(level);
        List<String> lines = new ArrayList<>();
        lines.add("§b§lLevel " + level);
        lines.add("§8[" + TextUtils.generateBar('a', '7', 50, xp, maxxp) + "§8] §f" + getPercent(xp, maxxp) + "§7%");
        lines.add("§7(§f" + NumberUtils.formatFull(xp) + "§7 / " + NumberUtils.formatFull(maxxp) + "§7)");
        return lines;
    }

    public static BigInteger getXpRequired(int level) {
        return BigInteger.valueOf(1000).multiply(BigDecimal.valueOf(1.3).pow(level).toBigInteger());
    }

    static List<String> generateSocketLines(List<SocketGem> gems) {
        List<String> lines = new ArrayList<>();
        for (SocketGem sg : gems) {
            if (sg == null) {
                lines.add("§a<Empty Slot>");
            } else {
                lines.add(sg.getItem().getItemMeta().getDisplayName());
            }
        }
        return lines;
    }
}
