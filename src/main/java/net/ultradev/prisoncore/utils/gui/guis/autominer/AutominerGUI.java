/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.autominer;

import net.ultradev.prisoncore.autominer.AutoMinerAI;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.playerdata.StoreRank;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class AutominerGUI implements GUI {
    private ItemStack getBonusItem(StoreRank rank, Material type, Player p) {
        String[][] bonusItemScript = {{"autominer:collectime", rank.toString()}, {"inv:open", "autominer"}};
        return new ItemFactory(type)
                .setName("§d" + rank.getName() + " Rank Bonus")
                .setLore(
                        "",
                        rank.getPrefix() + "§7 users get an extra",
                        "§7" + Math.floorDiv(rank.getAutoMinerBonus(), 60) + " minutes twice per day!",
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(CooldownUtils.isCooldown(p, "autominerTime_" + rank.name()) ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(bonusItemScript)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 36, "§2§lAuto Miner" +
                HiddenStringUtils.encodeString("gui:update|autominer"));

        AutoMinerAI model = AutoMinerUtils.getModel(player);

        String[][] pickaxeScript = {{"autominer:summon"}, {"inv:close"}};
        ItemStack pickaxe = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§dAuto Miner")
                .setLore(
                        "",
                        "§7Time remaining: §e" + DateUtils.convertTime(model.getAutominerTime().divide(BigInteger.valueOf(1000))),
                        "",
                        "§7Token multiplier: §eNone",
                        "§7Key multiplier: §eNone",
                        "",
                        "§7Rank multiplier: §e1.0x",
                        "",
                        "§b» Click to summon!"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(pickaxeScript)
                .create();

        String[][] earningsScript = {{"inv:open", "autominer_earnings"}};
        ItemStack earnings = new ItemFactory(Material.DOUBLE_PLANT)
                .setName("§dEarnings")
                .setLore(
                        "",
                        "§7View your Auto Miner earnings!",
                        "",
                        "§b» Click to view!"
                )
                .hideFlags()
                .setClickEvent(earningsScript)
                .create();

        String[][] upgradesScript = {{"inv:open", "autominer_upgrades"}};
        ItemStack upgrades = new ItemFactory(Material.REDSTONE)
                .setName("§dUpgrades")
                .setLore(
                        "",
                        "§7View your Auto Miner upgrades!",
                        "",
                        "§b» Click to view!"
                )
                .hideFlags()
                .setClickEvent(upgradesScript)
                .create();

        String[][] skinsScript = {{"inv:open", "autominer_skins"}};
        ItemStack skins = new ItemFactory(Material.SKULL_ITEM, 1, (short) 3)
                .setName("§dSkins")
                .setLore(
                        "",
                        "§7View your Auto Miner skins!",
                        "",
                        "§b» Click to view!"
                )
                .hideFlags()
                .setClickEvent(skinsScript)
                .create();

        inv.setItem(10, pickaxe);
        inv.setItem(12, earnings);
        inv.setItem(14, upgrades);
        inv.setItem(16, skins);


        inv.setItem(20, getBonusItem(StoreRank.COAL, Material.COAL_BLOCK, player));
        inv.setItem(21, getBonusItem(StoreRank.IRON, Material.IRON_BLOCK, player));
        inv.setItem(22, getBonusItem(StoreRank.GOLD, Material.GOLD_BLOCK, player));
        inv.setItem(23, getBonusItem(StoreRank.DIAMOND, Material.DIAMOND_BLOCK, player));
        inv.setItem(24, getBonusItem(StoreRank.EMERALD, Material.EMERALD_BLOCK, player));
        inv.setItem(25, getBonusItem(StoreRank.OBSIDIAN, Material.OBSIDIAN, player));

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
