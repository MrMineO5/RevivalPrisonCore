/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis;

import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.gui.guis.autominer.AutominerGUI;
import net.ultradev.prisoncore.utils.gui.guis.autominer.Autominer_EarningsGUI;
import net.ultradev.prisoncore.utils.gui.guis.autominer.Autominer_SkinsGUI;
import net.ultradev.prisoncore.utils.gui.guis.autominer.Autominer_UpgradesGUI;
import net.ultradev.prisoncore.utils.gui.guis.crate.CratePreviewGUI;
import net.ultradev.prisoncore.utils.gui.guis.dustbank.DustBankGUI;
import net.ultradev.prisoncore.utils.gui.guis.faq.FaqGUI;
import net.ultradev.prisoncore.utils.gui.guis.keyvault.KeyVaultGUI;
import net.ultradev.prisoncore.utils.gui.guis.keyvault.KeyvaultCraftGUI;
import net.ultradev.prisoncore.utils.gui.guis.kits.KitsGUI;
import net.ultradev.prisoncore.utils.gui.guis.leaderboards.LeaderBoardGUI;
import net.ultradev.prisoncore.utils.gui.guis.mailbox.MailboxGUI;
import net.ultradev.prisoncore.utils.gui.guis.mines.MinesGUI;
import net.ultradev.prisoncore.utils.gui.guis.mines.Mines_DonatorGUI;
import net.ultradev.prisoncore.utils.gui.guis.mines.Mines_PrestigeGUI;
import net.ultradev.prisoncore.utils.gui.guis.pets.ExchangerPetGUI;
import net.ultradev.prisoncore.utils.gui.guis.pickaxe.PickaxeGUI;
import net.ultradev.prisoncore.utils.gui.guis.pickaxe.Pickaxe_SocketsGUI;
import net.ultradev.prisoncore.utils.gui.guis.settings.SettingsGUI;
import net.ultradev.prisoncore.utils.gui.guis.shadytrader.ShadyTraderGUI;
import net.ultradev.prisoncore.utils.gui.guis.thunt.THuntGUI;
import net.ultradev.prisoncore.utils.gui.guis.tinker.TinkerGUI;
import net.ultradev.prisoncore.utils.gui.guis.tinker.Tinker_CraftingGUI;
import net.ultradev.prisoncore.utils.gui.guis.tinker.Tinker_DismantleGUI;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GUIManager {
    private static HashMap<String, GUI> guis = new HashMap<>();

    static {
        guis.put("mines", new MinesGUI());
        guis.put("mines_prestige", new Mines_PrestigeGUI());
        guis.put("mines_donator", new Mines_DonatorGUI());

        guis.put("pickaxe", new PickaxeGUI());
        guis.put("pickaxe_sockets", new Pickaxe_SocketsGUI());

        guis.put("crate_preview", new CratePreviewGUI());

        guis.put("mailbox", new MailboxGUI());

        guis.put("thunt", new THuntGUI());

        guis.put("shadytrader", new ShadyTraderGUI());

        guis.put("autominer", new AutominerGUI());
        guis.put("autominer_earnings", new Autominer_EarningsGUI());
        guis.put("autominer_skins", new Autominer_SkinsGUI());
        guis.put("autominer_upgrades", new Autominer_UpgradesGUI());

        guis.put("tinker", new TinkerGUI());
        guis.put("tinker_crafting", new Tinker_CraftingGUI());
        guis.put("tinker_dismantle", new Tinker_DismantleGUI());

        guis.put("settings", new SettingsGUI());

        guis.put("keyvault", new KeyVaultGUI());
        guis.put("kvcraft", new KeyvaultCraftGUI());

        guis.put("leaderboard", new LeaderBoardGUI());

        guis.put("faq", new FaqGUI());

        guis.put("kit", new KitsGUI());

        guis.put("pet_exchanger", new ExchangerPetGUI());

        guis.put("dustbank", new DustBankGUI());
    }

    public static void openGUISync(Player player, String name, String... args) {
        player.openInventory(guis.get(name).generateGUI(player, args));
    }

    public static void openGUI(Player player, String name, String... args) {
        Synchronizer.desynchronize(() -> {
            Inventory inv = guis.get(name).generateGUI(player, args);
            Synchronizer.synchronize(() -> player.openInventory(inv));
        });
    }

    public static void registerGUI(String name, GUI gui) {
        guis.put(name, gui);
    }

    public static void updateAllGUIs() {
        Synchronizer.desynchronize(() -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTopInventory() != null) {
                    if (HiddenStringUtils.hasHiddenString(player.getOpenInventory().getTopInventory().getTitle())) {
                        String decoded = HiddenStringUtils
                                .extractHiddenString(player.getOpenInventory().getTopInventory().getTitle());
                        String[] funcs = decoded.split("\\|");
                        if (funcs[0].equals("gui:update")) {
                            String inv = funcs[1];
                            String[] args = {};
                            if (funcs.length > 2) {
                                args = new String[funcs.length - 2];
                                System.arraycopy(funcs, 2, args, 0, funcs.length - 2);
                            }
                            openGUI(player, inv, args);
                        }
                    }
                }
            }
        });
    }

    public static void startLoop() {
        Scheduler.scheduleSyncRepeatingTask(GUIManager::updateAllGUIs, 10, 3);
    }
}
