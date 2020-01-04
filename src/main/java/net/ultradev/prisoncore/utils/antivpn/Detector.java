package net.ultradev.prisoncore.utils.antivpn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Detector {
    private static LinkedHashMap<String, ProxyStatus> cache;

    public static String getLocation(String address) {
        if (!cache.containsKey(address)) {
            return null;
        }
        return cache.get(address).getLocation();
    }

    private static int ip2int(@NotNull final String ip) {
        final String[] srgs = ip.split("\\.");
        int out = 0;
        for (int i = 3; i >= 0; --i) {
            out += (int) (Integer.parseInt(srgs[i]) * Math.pow(256.0, 3 - i));
        }
        return out;
    }

    public static boolean detect(final String player, @NotNull final String ip) {
        if (ip.equals("127.0.0.1")) {
            return true;
        }
        System.out.println("IP: " + ip);
        if (Detector.cache.containsKey(ip)) {
            final boolean ret = Detector.cache.get(ip).isClean();
            if (!ret) {
                broadcastOPs("§c" + player + "§7 tried to join on a Proxy/VPN");
            } else {
                broadcastOPs("§e" + player + "§7 is from §e" + Detector.cache.get(ip).getLocation());
            }
            return ret;
        }
        cleanCache();
        try {
            final URL url = new URL("https://proxycheck.io/v2/" + ip + "?vpn=1&asn=1&key=7uacd5-e902b4-n29865-7360t4");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            final StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            final String res = content.toString();
            final JsonElement el = new JsonParser().parse(res);
            final JsonObject obj = el.getAsJsonObject();
            final String status = obj.get("status").getAsString();
            if (status.equalsIgnoreCase("denied") || status.equalsIgnoreCase("error")) {
                Debugger.error("Could not check proxy: " + obj.get("message").getAsString(), "AntiVPN");
                return true;
            }
            if (status.equalsIgnoreCase("warning")) {
                Debugger.error("Warning: " + obj.get("message").getAsString(), "AntiVPN");
            }
            final JsonObject obj2 = obj.get(ip).getAsJsonObject();
            String loc = obj2.get("country").getAsString();
            if (obj2.has("city")) {
                loc = obj2.get("city").getAsString() + ", " + loc;
            }
            if (obj2.get("proxy").getAsString().equalsIgnoreCase("yes")) {
                final String type = obj2.get("type").getAsString();
                System.out.println("IP Blocked for: " + type);
                Detector.cache.put(ip, new ProxyStatus(false, loc));
                broadcastOPs("§c" + player + "§7 tried to join on a Proxy/VPN");
                return false;
            }
            Detector.cache.put(ip, new ProxyStatus(true, loc));
            broadcastOPs("§e" + player + "§7 is from §e" + loc);
            return true;
        } catch (Exception e) {
            System.out.println("IP Verification failed: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    private static void broadcastOPs(final String str) {
        Synchronizer.synchronize(() -> Bukkit.broadcast(str, "ultraprison.admin"));
    }

    public static void cleanCache() {
        if (Detector.cache.size() <= 250) {
            return;
        }
        final AtomicInteger i = new AtomicInteger(Detector.cache.size() - 250);
        final List<String> toRemove = new ArrayList<>();
        Detector.cache.keySet().forEach(key -> {
            if (i.get() > 0) {
                i.getAndDecrement();
                toRemove.add(key);
            }
        });
        toRemove.forEach(key -> Detector.cache.remove(key));
    }

    static {
        Detector.cache = new LinkedHashMap<>();
    }

    public static class ProxyStatus {
        private boolean clean;
        private String location;

        public ProxyStatus(final boolean clean, final String location) {
            this.clean = clean;
            this.location = location;
        }

        public boolean isClean() {
            return this.clean;
        }

        public String getLocation() {
            return this.location;
        }
    }
}
