/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.plugins;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EssentialsUtils {
    public static String getNickName(Player player) {
        User user = Essentials.getPlugin(Essentials.class).getUser(player);
        return user.getNickname() == null ? player.getName() : user.getNickname();
    }

    public static Location getWarpLocation(String warp) {
        try {
            return Essentials.getPlugin(Essentials.class).getWarps().getWarp(warp);
        } catch (Exception e) {
            Debugger.error("Error while getting warp: " + e.getMessage(), "Essentials");
            e.printStackTrace();
            return null;
        }
    }
}
