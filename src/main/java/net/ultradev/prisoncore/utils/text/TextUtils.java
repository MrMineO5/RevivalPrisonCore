/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;


public class TextUtils {
    public static String generateBar(char onColor, char offColor, int length, int curr, int max) {
        return generateBar(onColor, offColor, length, BigInteger.valueOf(curr), BigInteger.valueOf(max));
    }


    public static String generateBar(char onColor, char offColor, int length, BigInteger curr, BigInteger max) {
        return generateBar(onColor, offColor, ':', length, curr, max);
    }


    public static String generateBar(char onColor, char offColor, char ch, int length, int curr, int max) {
        return generateBar(onColor, offColor, ch, length, BigInteger.valueOf(curr), BigInteger.valueOf(max));
    }

    public static String generateBar(char onColor, char offColor, char ch, int length, BigInteger curr, BigInteger max) {
        int achieved = curr.multiply(BigInteger.valueOf(100L)).divide(max).multiply(BigInteger.valueOf(length)).divide(BigInteger.valueOf(100L)).intValue();
        if (curr.compareTo(max) >= 0) {
            achieved = length;
        }
        if (curr.compareTo(BigInteger.ZERO) <= 0) {
            achieved = 0;
        }
        StringBuilder str = new StringBuilder("§" + onColor);
        for (int i = 0; i < achieved; i++) {
            str.append(ch);
        }
        str.append("§").append(offColor);
        for (int i = achieved; i < length; i++) {
            str.append(ch);
        }
        return str.toString();
    }


    public static String toText(Location loc) {
        return "(" + loc.getWorld() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")";
    }

    public static String toText(ItemStack item) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", item);
        return config.saveToString();
    }


    public static boolean isCommand(String str, String command) {
        return (str.equalsIgnoreCase(command) || str.toLowerCase().startsWith(command.toLowerCase() + " "));
    }


    public static BaseComponent createVoteLink(String url) {
        TextComponent comp = new TextComponent("§7- §e" + url);
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("me!")));
        return comp;
    }

    public static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return sb.toString();
    }
}
