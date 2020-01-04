/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.messages;

import net.md_5.bungee.api.ChatColor;
import net.ultradev.prisoncore.Main;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messages {
    private static Map<String, Messages> loadedMessages = new HashMap<>();
    private String locale;
    private Properties prop;
    private String prefix;

    private Messages(String locale) {
        this.locale = locale;
        this.prop = new Properties();
        try {
            this.prop.load(new FileInputStream(new File(getLocaleFolder(), getFileName(locale))));
            this.prefix = this.prop.getProperty("Prefix");
        } catch (IOException e) {
            System.out.println("An error occured while loading locale for " + locale);
            e.printStackTrace();
        }
        this.checkProperties();
    }

    public static void init() {
        if (!getLocaleFolder().mkdirs()) {
            return;
        }
        File f = new File(getLocaleFolder(), "messages.properties");
        if (!f.exists()) {
            try {
                InputStream stream = Messages.class.getResourceAsStream("messages.properties");
                if (!f.createNewFile()) {
                    return;
                }
                byte[] buffer = new byte[stream.available()];
                if (stream.read(buffer) <= 0) {
                    return;
                }
                OutputStream outStream = new FileOutputStream(f);
                outStream.write(buffer);
                outStream.close();
            } catch (Exception e) {
                System.out.println("An error occured while creating the messages.properties file");
                e.printStackTrace();
            }
        }
        getMessages("");
    }

    private static void localeToFile(String locale) {
        File f = new File(getLocaleFolder(), getFileName(locale));
        if (!f.exists()) {
            try {
                InputStream stream = Messages.class.getResourceAsStream(getFileName(locale));
                f.createNewFile();
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream outStream = new FileOutputStream(f);
                outStream.write(buffer);
                outStream.close();
            } catch (Exception e) {
                System.out.println("An error occured while creating the " + getFileName(locale) + " file");
                e.printStackTrace();
            }
        }
    }

    public static void sendMsg(Player player, String loc, String... args) {
        getMessages(player.getLocale()).sendMessage(player, loc, args);
    }

    private static String getFileName(String locale) {
        return locale.equals("") ? "messages.properties" : "messages_" + locale + ".properties";
    }

    public static Messages getMessages(String l) {
        if (l.equals("")) {
            if (loadedMessages.containsKey("default")) {
                return loadedMessages.get("default");
            }
            Messages msgs = new Messages("");
            loadedMessages.put("default", msgs);
            return msgs;
        }
        String locale = l.substring(0, 2);
        if (locale.equals("en")) {
            return getMessages("");
        }
        if (!localeExists(locale)) {
            if (localeExistsInternal(locale)) {
                localeToFile(locale);
                return getMessages(locale);
            }
            return getMessages("");
        }
        if (!loadedMessages.containsKey(locale)) {
            Messages msgs = new Messages(locale);
            loadedMessages.put(locale, msgs);
            return msgs;
        }
        return loadedMessages.get(locale);
    }

    private static File getLocaleFolder() {
        return new File(Main.getInstance().getDataFolder(), "messages");
    }

    public static boolean localeExists(String locale) {
        return (new File(getLocaleFolder(), "messages_" + locale + ".properties").exists());
    }

    private static boolean localeExistsInternal(String locale) {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(getFileName(locale)) == null);
    }

    public void checkProperties() {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("messages.properties");
            Properties def = new Properties();
            def.load(stream);
            if (this.prop.keySet().containsAll(def.keySet())) {
                System.out.println("Validation for " + locale + " complete, not new keys found.");
            } else {
                Set<Object> keys = def.keySet();
                keys.removeAll(this.prop.keySet());
                System.out.println("Validation for " + locale + " complete, adding " + keys.size() + " new keys.");
                for (Object obj : keys) {
                    this.prop.setProperty((String) obj, def.getProperty((String) obj));
                }
                OutputStream os = new FileOutputStream(new File(getLocaleFolder(), getFileName(locale)));
                this.prop.store(os, "");
            }
        } catch (Exception e) {
            System.out.println("An error occured while checking the messages.");
        }
    }

    public void sendMessage(Player player, String loc, String... args) {
        String msg = prop.getProperty(loc);
        for (int i = 0; i < args.length; i++) {
            msg = msg.replaceAll(Pattern.quote("{" + i + "}"), Matcher.quoteReplacement(args[i]));
        }
        msg = msg.replace("%prefix%", this.prefix);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        player.sendMessage(msg);
    }

    public String getMessage(String loc, String... args) {
        String msg = prop.getProperty(loc);
        for (int i = 0; i < args.length; i++) {
            msg = msg.replaceAll(Pattern.quote("{" + i + "}"), Matcher.quoteReplacement(args[i]));
        }
        msg = msg.replace("%prefix%", this.prefix);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }
}
