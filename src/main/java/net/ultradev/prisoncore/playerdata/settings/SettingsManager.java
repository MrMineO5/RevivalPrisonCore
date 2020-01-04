/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata.settings;

import net.ultradev.prisoncore.commands.beta.RequiredRank;

import java.util.LinkedList;

public class SettingsManager {
    // TODO: Add categories
    // private static HashMap<SettingsCategory, LinkedList<Setting>> settings = new HashMap<SettingsCategory, LinkedList<Setting>>();
    public static LinkedList<Setting> settings = new LinkedList<>();

    static {
        settings.add(new Setting(
                "crate_message",
                "Crate Messages",
                true,
                RequiredRank.NONE,
                "§7Toggle messages for rewards",
                "§7when opening crates"
        ));
        settings.add(new Setting(
                "key_message",
                "Key Messages",
                true,
                RequiredRank.NONE,
                "§7Toggle messages for keys",
                "§7found while mining"
        ));
        settings.add(new Setting(
                "lb_message",
                "Lucky Block Messages",
                true,
                RequiredRank.NONE,
                "§7Toggle messages for rewards",
                "§7from mining lucky blocks"
        ));
        settings.add(new Setting(
                "scroll_message",
                "Scroll Messages",
                true,
                RequiredRank.NONE,
                "§7Toggle messages from adding",
                "§7scrolls to your pickaxe"
        ));
        settings.add(new Setting(
                "auto_exchange",
                "Auto Exchanger",
                false,
                RequiredRank.OBSIDIAN,
                "§7Automatically exchange bad rewards",
                "§7from crates for tokens"
        ));

        settings.add(new Setting(
                "fly_on_join",
                "Join Fly",
                false,
                RequiredRank.EMERALD,
                "§7Automatically enables flight",
                "§7when you join the server"
        ));
        settings.add(new Setting(
                "as_on_join",
                "Join AutoSell",
                false,
                RequiredRank.GOLD,
                "§7Automatically enables AutoSell",
                "§7when you join the server"
        ));
    }

    public static Setting getSetting(String name) {
        for (Setting setting : settings) {
            if (setting.name.equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }
}
