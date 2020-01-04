/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.crates.hopper;

import net.ultradev.prisoncore.ConfigManager;
import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.rewards.KeyReward;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import net.ultradev.prisoncore.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateHoppers {
    private static Map<UUID, CrateHopper> hoppers = new HashMap<>();

    public static void createHopper(Location loc, UUID player) {
        CrateHopper hopper = new CrateHopper(loc, player);
        hoppers.put(hopper.getHopperId(), hopper);
    }

    public static boolean removeHopper(Location loc) {
        CrateHopper hopper = getHopper(loc);
        if (hopper == null) {
            return false;
        }
        hoppers.remove(hopper.getHopperId());
        return true;
    }

    public static boolean isHopper(Location loc) {
        return (getHopper(loc) != null);
    }

    @Nullable
    public static CrateHopper getHopper(Location loc) {
        for (CrateHopper value : hoppers.values()) {
            if (value.getLoc().distance(loc) == 0.0D) {
                return value;
            }
        }
        return null;
    }

    public static void saveHoppers() {
        FileConfiguration config = ConfigManager.getConfig("cratehoppers");
        hoppers.forEach((key, value) -> {
            config.set(key + ".world", value.getLoc().getWorld().toString());
            config.set(key + ".location", value.getLoc().toVector());
            config.set(key + ".owner", value.getOwner().toString());
            value.getKeys().forEach((k, val) -> config.set(key + ".keys." + k, val));
        });
        ConfigManager.setConfig(config, "cratehoppers");
    }

    public static void loadHoppers() {
        FileConfiguration config = ConfigManager.getConfig("cratehoppers");
        for (String str : config.getKeys(false)) {
            UUID hopperId = UUID.fromString(str);
            World world = Bukkit.getWorld(config.getString(str + ".world"));
            Location loc = config.getVector(str + ".location").toLocation(world);
            UUID owner = UUID.fromString(config.getString(str + ".owner"));
            Map<String, Long> keys = new HashMap<>();
            if (config.contains("keys")) {
                for (String crate : config.getConfigurationSection("keys").getKeys(false)) {
                    keys.put(crate, config.getLong(str + ".keys." + crate));
                }
            }
            hoppers.put(hopperId, new CrateHopper(hopperId, loc, owner, keys));
        }
    }

    public static void startTimer() {
        Scheduler.scheduleSyncRepeatingTaskT(() -> hoppers.values().forEach(CrateHopper::update), 40, 5);
    }

    public static class CrateHopper {
        private UUID hopperId;
        private Location loc;
        private UUID owner;
        private Map<String, Long> keys;

        public CrateHopper(Location loc, UUID owner) {
            this(UUID.randomUUID(), loc, owner, new HashMap<>());
        }

        public CrateHopper(UUID hopperId, Location loc, UUID owner, Map<String, Long> keys) {
            this.hopperId = hopperId;
            this.loc = loc;
            this.owner = owner;
            this.keys = keys;
        }

        public UUID getHopperId() {
            return this.hopperId;
        }

        public Location getLoc() {
            return this.loc;
        }

        public UUID getOwner() {
            return this.owner;
        }

        public Map<String, Long> getKeys() {
            return this.keys;
        }

        public void addKeys(String type, long amount) {
            this.keys.put(type, this.keys.getOrDefault(type, 0L) + amount);
        }


        private String getCrateToOpen() {
            if (this.keys.isEmpty()) {
                return null;
            }
            for (Map.Entry<String, Long> stringLongEntry : this.keys.entrySet()) {
                if (stringLongEntry.getValue() < 1L) {
                    continue;
                }
                return stringLongEntry.getKey();
            }
            return null;
        }

        public void decrement(String str) {
            addKeys(str, -1L);
        }

        public void update() {
            if (getCrateToOpen() == null) {
                return;
            }
            Block block = this.loc.getBlock();
            Hopper hopper = (Hopper) block.getState();
            Inventory inv = hopper.getInventory();
            for (int i = 0; i < 5; i++) {
                if (inv.getItem(i) == null) {
                    String str = getCrateToOpen();
                    if (str == null) {
                        break;
                    }
                    Crate cr = CrateManager.getCrate(str);
                    assert cr != null;
                    Reward rew = cr.getReward();
                    decrement(cr.name);
                    if (rew instanceof TokenReward) {
                        Economy.tokens.addBalance(Bukkit.getOfflinePlayer(this.owner), ((TokenReward) rew).getAmount());
                    }
                    if (rew instanceof KeyReward) {
                        KeyReward kr = (KeyReward) rew;
                        String crate = kr.getCrate().toLowerCase();
                        int amount = kr.getCount();
                        addKeys(crate, amount);
                    }
                    if (rew instanceof ItemReward) {
                        ItemReward ir = (ItemReward) rew;
                        ItemStack item = ir.getItem();
                        inv.setItem(i, item);
                    }
                }
            }
        }
    }
}
