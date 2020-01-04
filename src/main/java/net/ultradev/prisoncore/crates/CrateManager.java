/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.crates;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.TextLine;
import net.ultradev.prisoncore.ConfigManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.RewardParser;
import net.ultradev.prisoncore.utils.file.FileUtils;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.plugins.HoloUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;


public class CrateManager {
    public static LinkedHashMap<String, Crate> normalcrates = new LinkedHashMap<>();
    private static HashMap<Location, PlacedCrate> placedCrates = new HashMap<>();

    @NotNull
    public static LinkedHashMap<Reward, Double> loadRewards(File f) {
        String str = FileUtils.loadFile(f);
        if (str == null || str.length() == 0) {
            return new LinkedHashMap<>();
        }
        String[] strs = str.split("\n");
        LinkedHashMap<Reward, Double> rewards = new LinkedHashMap<>();
        for (String s : strs) {
            String[] ss = s.split("#");
            rewards.put(RewardParser.getReward(ss[0]), Double.parseDouble(ss[1]));
        }
        return rewards;
    }

    public static void init() {
        loadCrates();
        loadPlacedCrates();
    }

    @Contract("null -> false")
    public static boolean isKey(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return NBTUtils.getString(item, "type").equals("cratekey");
    }

    public static boolean isAdminCrate(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return NBTUtils.getString(item, "type").equals("admincrate");
    }

    @Nullable
    public static Crate getType(ItemStack item) {
        if (!isKey(item) && !isAdminCrate(item)) {
            return null;
        }
        return normalcrates.get(NBTUtils.getString(item, "crate"));
    }

    private static void loadCrates() {
        String[] lore = {"§7Use this key at §e/warp crates§7!"};

        Crate minecrate = new Crate("mine", "§bMine", "§b§lMine Crate", lore, "§7Common drop from mining!", false, true);
        Crate rarecrate = new Crate("rare", "§dRare", "§d§lRare Crate", lore, "§7Rare drop from mining!", false, true);
        Crate legendarycrate = new Crate("legendary", "§cLegendary", "§c§lLegendary Crate", lore, "§7Rare drop from mining!", false, true);
        Crate mythicalcrate = new Crate("mythical", "§3Mythical", "animated_text(50):§c§lMythical Crate||§6§lMythical Crate||§e§lMythical Crate||§a§lMythical Crate||§b§lMythical Crate||§d§lMythical Crate", lore, "§7Type §e/buy§7 to get one!", false, false);
        Crate votecrate = new Crate("vote", "§aVote", "§a§lVote Crate", lore, "§7Gain by voting! §e/vote", false, false);

        registerCrate(minecrate);
        registerCrate(rarecrate);
        registerCrate(legendarycrate);
        registerCrate(mythicalcrate);
        registerCrate(votecrate);
    }

    private static void registerCrate(Crate crate) {
        normalcrates.put(crate.name, crate);
    }


    public static Crate getCrate(String name) {
        if (normalcrates.containsKey(name.toLowerCase())) {
            return normalcrates.get(name.toLowerCase());
        }
        return null;
    }


    public static int giveKeysSafe(Player player, Crate type, int amount) {
        ItemStack item = type.getKey(1);
        int space = InvUtils.getSpace(player, item);
        int give = space;
        if (space > amount) {
            give = amount;
        }
        InvUtils.giveItems(player, type.getKey(1), give);
        return amount - give;
    }

    public static long giveKeysSafe(Player player, Crate type, long amount) {
        long tempAmount = amount;
        if (type.keyVaultSupported) {
            ItemStack[] items = player.getInventory().getContents();
            Debugger.log("Checking for keyvaults", "crateManager");
            for (int i = 0; i < items.length; i++) {
                Debugger.log("Item #" + i, "crateManager");
                if (tempAmount <= 0L) {
                    Debugger.log("No remaining keys, stopping.", "crateManager");
                    break;
                }
                if (KeyVaultManager.isKeyvault(items[i])) {
                    Debugger.log("Item is a keyvault", "crateManager");
                    int cap = KeyVaultManager.getRemainingCapacity(items[i], type.name);
                    Debugger.log("Remaining Capacity: " + cap, "crateManager");
                    items[i] = KeyVaultManager.addKeys(items[i], type.name, (int) Math.min(tempAmount, cap));
                    Debugger.log("Adding keys...", "crateManager");
                    tempAmount -= Math.min(tempAmount, cap);
                    Debugger.log("Remaining keys: " + tempAmount, "crateManager");
                }
            }
            Debugger.log("Applying changes...", "crateManager");
            player.getInventory().setContents(items);
        }
        ItemStack item = type.getKey(1);
        long space = InvUtils.getSpace(player, item);
        long give = space;
        if (space > tempAmount) {
            give = tempAmount;
        }
        InvUtils.giveItems(player, type.getKey(1), (int) give);
        return tempAmount - give;
    }


    public static void giveKeys(@NotNull Player player, Crate type, int amount) {
        Debugger.log("Giving keys...", "crateManager");
        int tempAmount = amount;
        if (type.keyVaultSupported) {
            ItemStack[] items = player.getInventory().getContents();
            Debugger.log("Checking for keyvaults", "crateManager");
            for (int i = 0; i < items.length; i++) {
                Debugger.log("Item #" + i, "crateManager");
                if (tempAmount <= 0) {
                    Debugger.log("No remaining keys, stopping.", "crateManager");
                    break;
                }
                if (KeyVaultManager.isKeyvault(items[i])) {
                    Debugger.log("Item is a keyvault", "crateManager");
                    int cap = KeyVaultManager.getRemainingCapacity(items[i], type.name);
                    Debugger.log("Remaining Capacity: " + cap, "crateManager");
                    items[i] = KeyVaultManager.addKeys(items[i], type.name, Math.min(tempAmount, cap));
                    Debugger.log("Adding keys...", "crateManager");
                    tempAmount -= Math.min(tempAmount, cap);
                    Debugger.log("Remaining keys: " + tempAmount, "crateManager");
                }
            }
            Debugger.log("Applying changes...", "crateManager");
            player.getInventory().setContents(items);
        }
        Debugger.log("Giving remaining keys as items", "crateManager");
        InvUtils.giveItems(player, type.getKey(1), tempAmount);
    }


    public static void giveKeys(Player player, String type, int amount) {
        giveKeys(player, Objects.requireNonNull(getCrate(type)), amount);
    }


    @NotNull
    @Contract(pure = true)
    public static Collection<String> getCrateNames() {
        return normalcrates.keySet();
    }


    @NotNull
    @Contract(pure = true)
    public static Collection<Crate> getCrates() {
        return normalcrates.values();
    }


    public static String generateHoloId(Crate crate) {
        String id = null;
        int i = 0;
        while (id == null) {
            if (HoloUtils.holoManager.getHologram(i + crate.name) == null) {
                id = i + crate.name;
            }
            i++;
        }
        return id;
    }

    public static void placeCrate(Block block, Crate crate) {
        if (!block.getType().equals(Material.CHEST)) {
            return;
        }
        Hologram holo = new Hologram(generateHoloId(crate), block.getLocation().add(0.5D, 1.5D, 0.5D));
        holo.addLine(new TextLine(holo, crate.holoName));
        holo.addLine(new TextLine(holo, crate.holoLore));
        holo.addLine(new TextLine(holo, "click to preview!"));

        HoloUtils.holoManager.addActiveHologram(holo);
        HoloUtils.holoManager.saveHologram(holo);
        placedCrates.put(block.getLocation(), new PlacedCrate(holo.getId(), crate.name));
    }

    public static void breakCrate(Block block) {
        if (!isCrate(block.getLocation())) {
            return;
        }
        PlacedCrate crate = placedCrates.get(block.getLocation());
        HoloUtils.holoManager.deleteHologram(HoloUtils.holoManager.getHologram(crate.getHoloId()));
        placedCrates.remove(block.getLocation());
    }


    private static String generatePlacedCrateConfigKey(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getBlockX() + "|" + loc.getBlockY() + "|" + loc.getBlockZ();
    }


    public static boolean isCrate(Location loc) {
        return placedCrates.containsKey(loc);
    }


    public static Crate getCrate(Location loc) {
        if (!isCrate(loc)) {
            return null;
        }
        return getCrate(placedCrates.get(loc).getCrate());
    }

    public static void savePlacedCrates() {
        FileConfiguration config = ConfigManager.getConfig("placedcrates");
        for (Map.Entry<Location, PlacedCrate> entry : placedCrates.entrySet()) {
            Location loc = entry.getKey();
            PlacedCrate crate = entry.getValue();
            crate.serialize(config.createSection(generatePlacedCrateConfigKey(loc)));
        }
        ConfigManager.setConfig(config, "placedcrates");
    }

    public static void loadPlacedCrates() {
        FileConfiguration config = ConfigManager.getConfig("placedcrates");
        if (config == null)
            return;
        for (String str : config.getKeys(false)) {
            String[] locA = str.split("\\|");
            World world = Bukkit.getWorld(locA[0]);
            int x = Integer.parseInt(locA[1]);
            int y = Integer.parseInt(locA[2]);
            int z = Integer.parseInt(locA[3]);
            Location loc = new Location(world, x, y, z);
            placedCrates.put(loc, new PlacedCrate(config.getConfigurationSection(str)));
        }
    }

    public static int keyCount(@NotNull Player player, String crate) {
        int i = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (isKey(item)) {
                Crate cr = getType(item);
                if (cr != null) {

                    if (cr.name.equals(crate))
                        i += item.getAmount();
                }
            }
        }
        System.out.println("KeyCount: " + i);
        return i;
    }

    public static int removeKeys(@NotNull Player player, String crate, int c) {
        int count = c;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (isKey(item) &&
                    Objects.requireNonNull(getType(item)).name.equals(crate)) {
                if (count >= item.getAmount()) {
                    contents[i] = null;
                    count -= item.getAmount();
                } else {
                    item.setAmount(item.getAmount() - count);
                    contents[i] = item;
                    count = 0;
                }
                if (count == 0) {
                    break;
                }
            }
        }

        player.getInventory().setContents(contents);
        return count;
    }

    public static int getInvCap(Player player, String crate) {
        Crate cr = getCrate(crate);
        assert cr != null;
        ItemStack key = cr.getKey(1);
        return InvUtils.getSpace(player, key);
    }
}
