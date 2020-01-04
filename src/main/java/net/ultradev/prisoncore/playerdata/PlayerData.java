/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.autominer.AutoMinerData;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.gangs.Gang;
import net.ultradev.prisoncore.gangs.GangManager;
import net.ultradev.prisoncore.playerdata.settings.Setting;
import net.ultradev.prisoncore.playerdata.settings.SettingsManager;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.plugins.EssentialsUtils;
import net.ultradev.prisoncore.utils.plugins.VaultUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerData {
    private static HashMap<UUID, FileConfiguration> loadedPlayers = new HashMap<>();

    public static void init() {
        Scheduler.scheduleSyncRepeatingTask(PlayerData::announceMailbox, 100, 300);
        Scheduler.scheduleSyncRepeatingTask(PlayerData::saveAll, 100, 60);
        Scheduler.scheduleSyncRepeatingTask(PlayerData::update, 100, 180);
    }

    public static void update() {
        new ArrayList<>(loadedPlayers.keySet()).stream()
                .filter(i -> !Bukkit.getOfflinePlayer(i).isOnline())
                .forEach(PlayerData::unloadPlayerData);
    }

    public static void saveAll() {
        loadedPlayers.forEach((key, value) -> savePlayerData(key));
    }

    /**
     * Get the current loaded data for the player or load if it doesn't exist
     *
     * @param op Player
     * @return Data of player
     */
    public static FileConfiguration getPlayerData(OfflinePlayer op) {
        return getPlayerData(op.getUniqueId());
    }
    public static FileConfiguration getPlayerData(UUID id) {
        if (loadedPlayers.containsKey(id)) {
            return loadedPlayers.get(id);
        }
        File folder = new File(Main.getInstance().getDataFolder(), "players");
        File file = new File(folder, id + ".yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("WTF just happened?");
                }
            } catch (IOException e) {
                System.out.println("Error while attempting to create player data file for " + id);
                e.printStackTrace();
            }
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        loadedPlayers.put(id, cfg);
        return cfg;
    }

    public static List<UUID> getDatas() {
        List<UUID> ids = new ArrayList<>();
        File folder = new File(Main.getInstance().getDataFolder(), "players");
        String[] fs = folder.list();
        if (!folder.isDirectory() || fs == null) {
            return ids;
        }
        for (String s : fs) {
            ids.add(UUID.fromString(s.replaceAll("\\.yml", "")));
        }
        return ids;
    }

    public static void clean() {
        List<UUID> onlines = Bukkit.getOnlinePlayers().stream()
                .map(Entity::getUniqueId)
                .collect(Collectors.toList());
        loadedPlayers.keySet().stream()
                .filter(it -> !onlines.contains(it))
                .forEach(PlayerData::unloadPlayerData);
    }

    /**
     * Save the data of the player
     *
     * @param op Player
     */
    public static void savePlayerData(@NotNull OfflinePlayer op) {
        savePlayerData(op.getUniqueId());
    }

    /**
     * Save the data of the player
     *
     * @param id UUID
     */
    public static void savePlayerData(@NotNull UUID id) {
        if (!loadedPlayers.containsKey(id) || loadedPlayers.get(id) == null) {
            loadedPlayers.remove(id);
            return;
        }
        File folder = new File(Main.getInstance().getDataFolder(), "players");
        File file = new File(folder, id + ".yml");
        try {
            loadedPlayers.get(id).save(file);
        } catch (IOException e) {
            System.out.println("Error while attempting to save player data file for " + id);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Config Data: " + loadedPlayers.get(id).saveToString());
            e.printStackTrace();
        }
    }

    public static void unloadPlayerData(OfflinePlayer op) {
        unloadPlayerData(op.getUniqueId());
    }

    /**
     * Unload the data of the player
     *
     * @param id UUID of Player
     */
    public static void unloadPlayerData(UUID id) {
        if (id == null) {
            return;
        }
        savePlayerData(id);
        loadedPlayers.remove(id);
    }

    /**
     * Reload the data of the player
     *
     * @param op Player
     */
    public static void reloadPlayerData(OfflinePlayer op) {
        unloadPlayerData(op);
        getPlayerData(op);
    }

    /**
     * Get the index of the player's current prison rank
     *
     * @param op Player
     * @return Index of current prison rank
     */
    public static int getRank(OfflinePlayer op) {
        return getRank(op.getUniqueId());
    }
    public static int getRank(UUID id) {
        FileConfiguration cfg = getPlayerData(id);
        if (!cfg.contains("rank")) {
            cfg.set("rank", 0);
        }
        return cfg.getInt("rank");
    }

    /**
     * Set a player's current prison rank
     *
     * @param op   Player
     * @param rank Index of the prison rank to set to
     */
    public static void setRank(OfflinePlayer op, int rank) {
        FileConfiguration cfg = getPlayerData(op);
        cfg.set("rank", rank);
        savePlayerData(op);
    }

    /**
     * Get the name of a player's current rank
     *
     * @param op Player
     * @return Rank name
     */
    @NotNull
    public static String getRankName(OfflinePlayer op) {
        return RankupManager.getRankName(getRank(op));
    }

    /**
     * Get the display name of the player's current prison rank
     *
     * @param op Player
     * @return Rank display name
     */
    public static String getRankDisplayName(OfflinePlayer op) {
        return RankupManager.getRankDisplayName(getRank(op));
    }

    /**
     * Get the hover text displayed when hovering over a player's name
     *
     * @param p Player
     * @return Hover text
     */
    private static BaseComponent[] getNameHover(Player p) {
        BaseComponent[] total;

        BaseComponent[] nl = new ComponentBuilder("\n").create();

        BaseComponent[] name = new ComponentBuilder("§9Name: §7" + p.getName()).create();
        total = ArrayUtils.addAll(name, nl);
        total = ArrayUtils.addAll(total, nl);
        BaseComponent[] serverRank = new ComponentBuilder("§6Server Rank: §7[" + getRankDisplayName(p) + "§7]").create();
        total = ArrayUtils.addAll(total, serverRank);
        total = ArrayUtils.addAll(total, nl);
        BaseComponent[] storeRank = new ComponentBuilder("§6Store Rank: " + getStoreRankPrefix(p)).create();
        total = ArrayUtils.addAll(total, storeRank);
        total = ArrayUtils.addAll(total, nl);
        BaseComponent[] staffRank = new ComponentBuilder("§6Staff Rank: " + getStaffRankPrefix(p)).create();
        total = ArrayUtils.addAll(total, staffRank);
        total = ArrayUtils.addAll(total, nl);
        total = ArrayUtils.addAll(total, nl);
        BaseComponent[] tokens = new ComponentBuilder("§eTokens: §7" + NumberUtils.formatFull(Economy.tokens.getBalance(p))).create();
        total = ArrayUtils.addAll(total, tokens);
        total = ArrayUtils.addAll(total, nl);
        BigInteger autoMinerTime = AutoMinerUtils.getModel(p).getAutominerTime();
        BaseComponent[] amTime = new ComponentBuilder("§bAM Time: §7" + DateUtils.convertTimeM(autoMinerTime)).create();
        total = ArrayUtils.addAll(total, amTime);

        if (isInGang(p) && GangManager.getGang(getGang(p)) != null) {
            total = ArrayUtils.addAll(total, nl);
            total = ArrayUtils.addAll(total, nl);
            Gang g = GangManager.getGang(Objects.requireNonNull(getGang(p)));
            assert g != null;
            BaseComponent[] gang = new ComponentBuilder("§cGang: §7" + g.getName()).create();
            total = ArrayUtils.addAll(total, gang);
            total = ArrayUtils.addAll(total, nl);
            BaseComponent[] rank = new ComponentBuilder("§cRank: §7" + g.getRank(p.getUniqueId()).getName()).create();
            total = ArrayUtils.addAll(total, rank);
        }

        return total;
    }

    /**
     * Get the format of chat message for a player
     *
     * @param p       Player
     * @param message Chat message
     * @return Format of chat message
     */
    public static BaseComponent[] getChatFormat(Player p, String message) {
        String rankName = RankupManager.getRankDisplayName(getRank(p));
        String name = ChatColor.translateAlternateColorCodes('&', VaultUtils.getPrefix(p)) + "§7[" + rankName + "§7] " + EssentialsUtils.getNickName(p);

        BaseComponent[] nameComponent = new ComponentBuilder(name)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getNameHover(p)))
                .create();

        String msg = "§r §7» §f" + message;
        BaseComponent[] msgComponent = TextComponent.fromLegacyText(msg);

        return ArrayUtils.addAll(nameComponent, msgComponent);
    }

    /**
     * Check if a player has sufficient balance to rank up
     *
     * @param op Player
     * @return <code>true</code> if the player can afford to rank up
     */
    public static boolean canAffordRankup(OfflinePlayer op) {
        return (Economy.tokens.hasBalance(op, RankupManager.getRankupCost(op)));
    }

    /**
     * Increment the rank of a player if they have sufficient balance
     *
     * @param op Player to increment the rank of
     */
    public static void rankup(OfflinePlayer op) {
        if (canAffordRankup(op)) {
            Economy.tokens.removeBalance(op, RankupManager.getRankupCost(op));
            setRank(op, getRank(op) + 1);
        }
    }

    /**
     * Increment the rank of a player as long as they have sufficient balance
     *
     * @param op Player
     */
    public static void rankupMax(OfflinePlayer op) {
        while (canAffordRankup(op)) {
            rankup(op);
        }
    }

    /**
     * Get the AutoSell time of a player
     *
     * @param op Player
     * @return AutoSell time of the player
     */
    public static BigInteger getAutoSellTime(OfflinePlayer op) {
        FileConfiguration cfg = getPlayerData(op);
        if (!cfg.contains("autoSellTime")) {
            cfg.set("autoSellTime", "0");
        }
        return new BigInteger(cfg.getString("autoSellTime"));
    }

    /**
     * Set the AutoSell time of a player
     *
     * @param op     Player
     * @param amount Amount to set AutoSell time to
     */
    public static void setAutoSellTime(OfflinePlayer op, BigInteger amount) {
        FileConfiguration cfg = getPlayerData(op);
        cfg.set("autoSellTime", amount.toString());
    }

    /**
     * Add AutoSell time to a player
     *
     * @param op     Player
     * @param amount Amount of AutoSell time to add
     */
    public static void addAutoSellTime(OfflinePlayer op, BigInteger amount) {
        setAutoSellTime(op, getAutoSellTime(op).add(amount));
    }

    /**
     * Get the contents of a player's mailbox
     *
     * @param op Player
     * @return Contents
     */
    public static List<ItemStack> getMailbox(OfflinePlayer op) {
        FileConfiguration cfg = getPlayerData(op);
        List<ItemStack> mailbox = new ArrayList<>();
        if (!cfg.contains("mailbox")) {
            return mailbox;
        }
        ConfigurationSection section = cfg.getConfigurationSection("mailbox");
        for (String str : section.getKeys(false)) {
            mailbox.add(section.getItemStack(str));
        }
        return mailbox;
    }

    /**
     * Set the contents of a player's mailbox
     *
     * @param op    Player
     * @param items Contents
     */
    private static void setMailbox(OfflinePlayer op, @NotNull List<ItemStack> items) {
        FileConfiguration cfg = getPlayerData(op);
        cfg.set("mailbox", null);
        for (int i = 0; i < items.size(); i++) {
            cfg.set("mailbox." + i, items.get(i));
        }
        savePlayerData(op);
    }

    /**
     * Add an item to a player's mailbox
     *
     * @param op   Player
     * @param item Item to add
     */
    public static void addMailbox(OfflinePlayer op, ItemStack item) {
        List<ItemStack> mailbox = getMailbox(op);
        mailbox.add(item);
        setMailbox(op, mailbox);
    }

    /**
     * Remove an item from a player's mailbox
     *
     * @param op   Player
     * @param item Index of the item to remove
     */
    public static void removeMailbox(OfflinePlayer op, int item) {
        List<ItemStack> mailbox = getMailbox(op);
        mailbox.remove(item);
        setMailbox(op, mailbox);
    }

    /**
     * Announce to all players that have items in their mailbox to check it
     */
    public static void announceMailbox() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getMailbox(player).isEmpty()) {
                player.sendMessage("§bYou have items in your Mailbox! Use /mailbox to claim them!");
            }
        }
    }

    /**
     * Get a player's store rank
     *
     * @param p Player
     * @return Store rank
     */
    public static StoreRank getStoreRank(Player p) {
        return StoreRank.getRank(p);
    }

    /**
     * Get a player's store rank display name
     *
     * @param p Player
     * @return Store rank display name
     */
    public static String getStoreRankDisplay(Player p) {
        return getStoreRank(p).getDisplayName();
    }

    /**
     * Get a player's store rank prefix
     *
     * @param p Player
     * @return Store rank prefix
     */
    @NotNull
    public static String getStoreRankPrefix(Player p) {
        return getStoreRank(p).getPrefix();
    }

    /**
     * Get a player's staff rank
     *
     * @param p Player
     * @return Staff rank
     */
    public static StaffRank getStaffRank(Player p) {
        return StaffRank.getRank(p);
    }

    /**
     * Get a player's staff rank display name
     *
     * @param p Player
     * @return Staff rank display name
     */
    public static String getStaffRankDisplay(Player p) {
        return getStaffRank(p).getDisplayName();
    }

    /**
     * Get a player's staff rank prefix
     *
     * @param p Player
     * @return Staff rank prefix
     */
    @NotNull
    public static String getStaffRankPrefix(Player p) {
        return getStaffRank(p).getPrefix();
    }

    /**
     * Set a player's saved pickaxe
     *
     * @param op   Player
     * @param item Pickaxe
     */
    public static void setPickaxe(OfflinePlayer op, ItemStack item) {
        FileConfiguration cfg = getPlayerData(op);
        cfg.set("pickaxe", item);
        savePlayerData(op);
    }

    /**
     * Get a player's saved pickaxe
     *
     * @param op Player
     * @return Pickaxe
     */
    public static ItemStack getPickaxe(OfflinePlayer op) {
        FileConfiguration cfg = getPlayerData(op);
        return cfg.getItemStack("pickaxe");
    }

    /**
     * Get a player's autominer data
     *
     * @param op Player
     * @return Autominer Data
     */
    @NotNull
    public static AutoMinerData getAutominerData(OfflinePlayer op) {
        FileConfiguration data = getPlayerData(op);
        if (!data.contains("autominerData")) {
            AutoMinerData.Companion.blank().serialize(data.createSection("autominerData"));
            savePlayerData(op);
        }
        return AutoMinerData.Companion.deserialize(getPlayerData(op).getConfigurationSection("autominerData"));
    }

    /**
     * Set a player's autominer data
     *
     * @param op   Player
     * @param data Autominer Data
     */
    public static void setAutominerData(OfflinePlayer op, @NotNull AutoMinerData data) {
        Debugger.log("Saving AutoMiner data", "playerData");
        FileConfiguration config = getPlayerData(op);
        ConfigurationSection sec;
        if (!config.contains("autominerData")) {
            sec = config.createSection("autominerData");
        } else {
            sec = config.getConfigurationSection("autominerData");
        }
        data.serialize(sec);
    }

    /**
     * Set the gang a player is in
     *
     * @param op   Player
     * @param gang Gang
     */
    public static void setGang(OfflinePlayer op, String gang) {
        FileConfiguration config = getPlayerData(op);
        config.set("gang", gang);
        savePlayerData(op);
    }

    /**
     * Get the gang a player is in
     *
     * @param op Player
     * @return Gang
     */
    @Nullable
    public static String getGang(OfflinePlayer op) {
        FileConfiguration config = getPlayerData(op);
        if (!config.contains("gang")) {
            return null;
        }
        return config.getString("gang");
    }

    /**
     * Check is the player is in a gang
     *
     * @param op Player
     * @return <code>true</code> if the player is in a gang
     */
    public static boolean isInGang(OfflinePlayer op) {
        return getGang(op) != null;
    }

    /**
     * Get the settings map for a player
     *
     * @param op Player
     * @return Settings map
     */
    @NotNull
    public static Map<String, Boolean> getSettings(OfflinePlayer op) {
        FileConfiguration config = getPlayerData(op);
        Map<String, Boolean> ret = new HashMap<>();
        if (!config.contains("settings")) {
            return ret;
        }
        ConfigurationSection sec = config.getConfigurationSection("settings");
        for (String str : sec.getKeys(false)) {
            ret.put(str, sec.getBoolean(str));
        }
        return ret;
    }

    /**
     * Set the settings map for a player
     *
     * @param op       Player
     * @param settings Settings map
     */
    public static void setSettings(OfflinePlayer op, @NotNull Map<String, Boolean> settings) {
        FileConfiguration config = getPlayerData(op);
        settings.forEach((key, value) -> {
            config.set("settings." + key, value);
        });
    }

    /**
     * Get the value of a setting for a player
     *
     * @param op      Player
     * @param setting Setting
     * @return Value of the setting
     */
    public static boolean getSetting(OfflinePlayer op, String setting) {
        Setting set = SettingsManager.getSetting(setting);
        boolean def = set != null && set.def;
        return getSettings(op).getOrDefault(setting, def);
    }

    /**
     * Set the value of a setting for a player
     *
     * @param op      Player
     * @param setting Setting
     * @param value   Value
     */
    public static void setSetting(OfflinePlayer op, String setting, boolean value) {
        Map<String, Boolean> settings = getSettings(op);
        settings.put(setting, value);
        setSettings(op, settings);
    }

    /**
     * Get the current economy balance of the player
     *
     * @param op      Player
     * @param economy Economy
     * @return
     */
    public static BigInteger getEconomy(OfflinePlayer op, @NotNull String economy) {
        return getEconomy(op.getUniqueId(), economy);
    }
    public static BigInteger getEconomy(UUID id, @NotNull String economy) {
        FileConfiguration config = getPlayerData(id);
        if (config.contains("economy." + economy.toLowerCase())) {
            return new BigInteger(config.getString("economy." + economy.toLowerCase()));
        }
        return BigInteger.ZERO;
    }

    /**
     * Set the current economy balance of the player
     *
     * @param op      Player
     * @param economy Economy
     * @param amount  Amount
     */
    @NotNull
    @Contract("_, _, _ -> param3")
    public static BigInteger setEconomy(OfflinePlayer op, @NotNull String economy, @NotNull BigInteger amount) {
        FileConfiguration config = getPlayerData(op);
        config.set("economy." + economy.toLowerCase(), amount.toString());
        return amount;
    }

    /**
     * Add <code>amount</code> to the player's economy balance
     *
     * @param op      Player
     * @param economy Economy
     * @param amount  Amount
     */
    @NotNull
    public static BigInteger addEconomy(OfflinePlayer op, String economy, BigInteger amount) {
        return setEconomy(op, economy, getEconomy(op, economy).add(amount));
    }

    /**
     * Remove <code>amount</code> from the player's economy balance
     *
     * @param op      Player
     * @param economy Economy
     * @param amount  Amount
     */
    @NotNull
    public static BigInteger removeEconomy(OfflinePlayer op, String economy, @NotNull BigInteger amount) {
        return addEconomy(op, economy, amount.negate());
    }

    public static String getVariableString(OfflinePlayer op, String loc) {
        FileConfiguration config = getPlayerData(op);
        return config.getString("vars." + loc);
    }

    public static long getVariableLong(OfflinePlayer op, String loc) {
        FileConfiguration config = getPlayerData(op);
        return config.getLong("vars." + loc);
    }

    public static void setVariable(OfflinePlayer op, String loc, String value) {
        FileConfiguration config = getPlayerData(op);
        config.set("vars." + loc, value);
    }

    public static void setVariable(OfflinePlayer op, String loc, long value) {
        FileConfiguration config = getPlayerData(op);
        config.set("vars." + loc, value);
    }

    public static boolean hasVariable(OfflinePlayer op, String loc) {
        FileConfiguration config = getPlayerData(op);
        return config.contains("vars." + loc);
    }

    public static long getCooldown(OfflinePlayer op, String cd) {
        FileConfiguration config = getPlayerData(op);
        if (!config.contains("cooldowns." + cd)) {
            return 0;
        }
        return config.getLong("cooldowns." + cd);
    }

    public static void setCooldown(OfflinePlayer op, String cd, long val) {
        FileConfiguration config = getPlayerData(op);
        config.set("cooldowns." + cd, val);
        savePlayerData(op);
    }

    public static BigInteger getBlocksMined(OfflinePlayer op) {
        FileConfiguration config = getPlayerData(op);
        if (!config.contains("blocksMined")) {
            config.set("blocksMined", BigInteger.ZERO.toString());
            savePlayerData(op);
        }
        return new BigInteger(config.getString("blocksMined"));
    }

    public static void setBlocksMined(OfflinePlayer op, BigInteger amount) {
        FileConfiguration config = getPlayerData(op);
        config.set("blocksMined", amount);
    }

    public static void addBlocksMined(OfflinePlayer op, BigInteger amount) {
        setBlocksMined(op, getBlocksMined(op).add(amount));
    }


    public static void setPickaxeLevel(OfflinePlayer op, int level) {
        FileConfiguration config = getPlayerData(op);
        config.set("pickaxeLevel", level);
        savePlayerData(op);
    }
    public static int getPickaxeLevel(OfflinePlayer op) {
        FileConfiguration config = getPlayerData(op);
        if (!config.contains("pickaxeLevel")) {
            int level = 0;
            if (op.isOnline()) {
                level = NBTUtils.getInt(op.getPlayer().getInventory().getItem(0), "level");
            }
            config.set("pickaxeLevel", level);
            savePlayerData(op);
        }
        return config.getInt("pickaxeLevel");
    }
    public static int getPickaxeLevel(UUID id) {
        FileConfiguration config = getPlayerData(id);
        if (!config.contains("pickaxeLevel")) {
            int level = 0;
            config.set("pickaxeLevel", level);
            savePlayerData(id);
        }
        return config.getInt("pickaxeLevel");
    }

    public static void setPlayTime(OfflinePlayer op, BigInteger playtime) {
        setPlayTime(op.getUniqueId(), playtime);
    }
    public static void setPlayTime(UUID id, BigInteger playtime) {
        FileConfiguration config = getPlayerData(id);
        config.set("playtime", playtime.toString());
        savePlayerData(id);
    }
    public static BigInteger getPlayTime(OfflinePlayer op) {
        return getPlayTime(op.getUniqueId());
    }
    public static BigInteger getPlayTime(UUID id) {
        FileConfiguration config = getPlayerData(id);
        if (!config.contains("playtime")) {
            config.set("playtime", BigInteger.ZERO.toString());
            savePlayerData(id);
        }
        return new BigInteger(config.getString("playtime"));
    }
    public static void addPlayTime(OfflinePlayer op, BigInteger playtime) {
        addPlayTime(op.getUniqueId(), playtime);
    }
    public static void addPlayTime(UUID id, BigInteger playtime) {
        setPlayTime(id, getPlayTime(id).add(playtime));
    }
}
