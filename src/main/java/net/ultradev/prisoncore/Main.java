/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore;

import lombok.Getter;
import net.ultradev.prisoncore.announcer.Announcer;
import net.ultradev.prisoncore.anomalies.AnomalyManager;
import net.ultradev.prisoncore.autominer.AutominerCore;
import net.ultradev.prisoncore.blockers.PluginHider;
import net.ultradev.prisoncore.commands.*;
import net.ultradev.prisoncore.commands.beta.CommandManager;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.crates.hopper.CrateHoppers;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.events.EventsManager;
import net.ultradev.prisoncore.events.JoinLeaveEvents;
import net.ultradev.prisoncore.gangs.GangManager;
import net.ultradev.prisoncore.leaderboards.LeaderBoards;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.multipliers.MultiplierManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.selling.AutoSell;
import net.ultradev.prisoncore.utils.AntiPiracy;
import net.ultradev.prisoncore.utils.PlayerMap;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.betterspigot.BetterSpigot;
import net.ultradev.prisoncore.utils.display.MessageQueue;
import net.ultradev.prisoncore.utils.display.ScoreboardUtils;
import net.ultradev.prisoncore.utils.display.SignGUI;
import net.ultradev.prisoncore.utils.display.TabUtils;
import net.ultradev.prisoncore.utils.gui.ItemScriptFunction;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.plugins.HoloUtils;
import net.ultradev.prisoncore.utils.plugins.VaultUtils;
import net.ultradev.prisoncore.utils.protocol.Protocol;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Main extends JavaPlugin {
    public static boolean auth = true;
    @Getter
    private static SignGUI signGui;
    private static PluginHider hider;

    private static Main instance;

    // public static HashMap<UUID, NPC> npcs;

    private static FileConfiguration sellpriceConfig = null;

    @Contract(pure = true)
    public static Main getInstance() {
        return instance;
    }

	/*
	private void loadEnchantsConfig() {
		FileConfiguration conf = getConfig();
		// Lightning
		EnchantUtils.lightning_chancePerLevel = conf.getDouble("enchants.lightning.chancePerLevel");
		EnchantUtils.lightning_radius = conf.getInt("enchants.lightning.radius");

		// Detonation
		EnchantUtils.detonation_chancePerLevel = conf.getInt("enchants.detonation.chancePerLevel");
		EnchantUtils.detonation_radiusMin = conf.getInt("enchants.detonation.radiusMin");
		EnchantUtils.detonation_radiusMax = conf.getInt("enchants.detonation.radiusMax");

		// Saturated
		EnchantUtils.saturated_chancePerLevel = conf.getInt("enchants.saturated.chancePerLevel");

		// Explosive
		EnchantUtils.explosive_initialChance = conf.getInt("enchants.explosive.initialChance");
		EnchantUtils.explosive_chancePerLevel = conf.getInt("enchants.explosive.chancePerLevel");

		// Charity
		EnchantUtils.charity_chancePerLevel = conf.getInt("enchants.charity.chancePerLevel");
		EnchantUtils.charity_amountPerLevelMin = conf.getInt("enchants.charity.amountPerLevelMin");
		EnchantUtils.charity_amountPerLevelMax = conf.getInt("enchants.charity.amountPerLevelMax");

		// Eruption
		EnchantUtils.eruption_chancePerLevel = conf.getInt("enchants.eruption.chancePerLevel");
		EnchantUtils.eruption_minPerLevel = conf.getInt("enchants.eruption.minPerLevel");
		EnchantUtils.eruption_maxPerLevel = conf.getInt("enchants.eruption.maxPerLevel");

		// Token Greed
		EnchantUtils.tokengreed_baseChance = conf.getInt("enchants.tokengreed.baseChance");
		EnchantUtils.tokengreed_chancePerLevel = conf.getInt("enchants.tokengreed.chancePerLevel");
		EnchantUtils.tokengreed_baseMin = conf.getInt("enchants.tokengreed.baseMin");
		EnchantUtils.tokengreed_minPerLevel = conf.getInt("enchants.tokengreed.minPerLevel");
		EnchantUtils.tokengreed_baseMax = conf.getInt("enchants.tokengreed.baseMax");
		EnchantUtils.tokengreed_maxPerLevel = conf.getInt("enchants.tokengreed.maxPerLevel");
	}
	 */

    public static Integer getSellPrice(Material mat) {
        if (!sellpriceConfig.contains(mat.toString().toLowerCase())) {
            return null;
        }
        return sellpriceConfig.getInt(mat.toString().toLowerCase());
    }

    public void onEnable() {
        instance = this;
        if (!AntiPiracy.verify()) {
            return;
        }
        System.out.println("Loading PrisonCore v" + instance.getDescription().getVersion() + " by UltraDev");
        System.out.println("Initializing enchantments...");
        CustomEnchant.init();
        System.out.println("Injecting enchantments...");
        CustomEnchant.register();
        System.out.println("Initializing dependancy: Vault ...");
        VaultUtils.init();
        System.out.println("Initializing dependancy: Holograms ...");
        HoloUtils.init();
        System.out.println("Loading config...");
        loadConfig();
        System.out.println("Initializing ConfigManager...");
        ConfigManager.init();
        System.out.println("Registering events...");
        EventsManager.registerEvents(instance);
        System.out.println("Loading sell prices...");
        getInstance().loadSellpriceConfig();
        System.out.println("Initializing CrateManager...");
        CrateManager.init();
        System.out.println("Loading mines...");
        MineManager.loadMines();
        System.out.println("Initializing ItemScript Functions...");
        ItemScriptFunction.init();
        System.out.println("Loading custom ItemScript Functions...");
        ItemScriptFunction.initExt();
        System.out.println("Registering commands...");
        getInstance().registerCommands();
        CommandManager.registerAll(this);
        System.out.println("Scheduling tasks...");
        getInstance().scheduleTasks();
        System.out.println("Activating multiplier loop...");
        MultiplierManager.startLoop();
        System.out.println("Loading enchantment price functions...");
        EnchantInfo.loadEnchants();
        // loadEnchantsConfig();
        System.out.println("Starting anomaly timer...");
        AnomalyManager.startTimer(300);
        System.out.println("Generating AutoMiner Core");
        new AutominerCore(this);
        System.out.println("Loading cooldowns");
        CooldownUtils.loadCooldowns();
        System.out.println("Enabling message queue");
        MessageQueue.init();
        System.out.println("Enabling Gang Manager");
        GangManager.init();
        System.out.println("Initializing player map");
        PlayerMap.init(this);
        System.out.println("Initializing player data");
        PlayerData.init();
        System.out.println("Initializing custom spigot features");
        BetterSpigot.init(this);
        System.out.println("Starting GUI loop");
        GUIManager.startLoop();
        signGui = new SignGUI(this);
        System.out.println("Initializing Protocol");
        Protocol.init();
        hider = new PluginHider(this);
        hider.init();
        System.out.println("Initializing sorter");
        LeaderBoards.init();
        System.out.println("Creating announcer");
        new Announcer();
        System.out.println("Loading crate hoppers");
        CrateHoppers.loadHoppers();
        CrateHoppers.startTimer();
    }

    private void register(String str, CommandExecutor exec) {
        getCommand(str).setExecutor(exec);
    }

    private void registerCommands() {
        register("show", new ShowCmd());
        register("rankup", new RankupCmd());
        register("crate", new CrateCmd());
        register("mine", new MineCmd());
        register("enchant", new EnchantCmd());

        register("givescroll", new GiveScrollCmd());
        register("eco", (CommandExecutor) new EcoCmd());
        new ConsumableCmd(this);

        register("nbt", new NBTCmd());

        getCommand("autosell").setExecutor(new AutoSellCmd());

        getCommand("setarmormulti").setExecutor(new SetArmorMultiCmd());

        getCommand("build").setExecutor(new BuildCmd());
        getCommand("debug").setExecutor(new DebugCmd());

        getCommand("treasurehunt").setExecutor(new TreasureHuntCmd());

        getCommand("mailbox").setExecutor(new MailBoxCmd());
        getCommand("sendmail").setExecutor(new SendMailCmd());

        getCommand("gang").setExecutor(new GangCmd());

        getCommand("kit").setExecutor(new KitCmd());
    }

    private void scheduleTasks() {
        Scheduler.scheduleSyncRepeatingTask(AutoSell::autosellUpdate, 100, 20);
        MineManager.startTimer();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, TabUtils::updateAll, 20, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ScoreboardUtils::updateAll, 20, 20);
    }

    public void onDisable() {
        if (!auth) {
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled(this)) {
            return;
        }
        JoinLeaveEvents.onDisable();
        CrateHoppers.saveHoppers();
        CooldownUtils.saveCooldowns();
        MessageQueue.deinit();
        GangManager.deinit();
        PlayerMap.deinit();
        AutominerCore.deInit();
        ConfigManager.deInit();
        CustomEnchant.unregister();
        CrateManager.savePlacedCrates();
        signGui.destroy();
        VaultUtils.deinit();
        PlayerData.saveAll();
        AnomalyManager.deinit();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    // SellPrice Config
    private void loadSellpriceConfig() {
        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdir()) {
                Debugger.log("Config folder created", "main");
            }
        }
        File file = new File(getDataFolder(), "sellprices.yml");
        if (!file.exists()) {
            try (InputStream in = getResource("sellprices.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sellpriceConfig = YamlConfiguration.loadConfiguration(file);
    }
}
