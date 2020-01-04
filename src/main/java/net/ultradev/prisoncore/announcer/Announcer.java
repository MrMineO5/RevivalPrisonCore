/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.announcer;

import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Announcer {
    private List<String> announcements = new ArrayList<>();

    public Announcer() {
        this.announcements.add("Consider supporting the server by purchasing your own rank at store.revivalprison.com!");
        this.announcements.add("Need help? Use /faq for frequently asked questions or ask a staff member!");
        Scheduler.scheduleSyncRepeatingTask(this::randomAnnouncement, 10, 300);
    }

    private void randomAnnouncement() {
        String announcement = this.announcements.get(MathUtils.random(0, this.announcements.size() - 1));
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage("§c§lTIP §7» §6" + announcement);
    }
}
