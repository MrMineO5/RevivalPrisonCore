/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.autominer;

import net.ultradev.prisoncore.autominer.AutoMinerData;
import net.ultradev.prisoncore.autominer.AutoMinerRewards;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Autominer_EarningsGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 9 * 5, "§4§lAutominer" +
                HiddenStringUtils.encodeString("gui:update|autominer_earnings"));

        AutoMinerData data = AutoMinerUtils.getModel(player).data;
        AutoMinerRewards rewards = data.getRewards();

        String[][] tokensScript = {{"autominer:collect", "tokens"}};
        ItemStack tokens = new ItemFactory(Material.DOUBLE_PLANT)
                .setName("§eTokens")
                .setLore(
                        "",
                        "§7Total: §e" + NumberUtils.formatFull(rewards.getTokens()),
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(rewards.getTokens() > 0 ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(tokensScript)
                .create();

        String[][] mineKeysScript = {{"autominer:collect", "keys:mine"}};
        ItemStack mineKeys = new ItemFactory(Material.STAINED_CLAY, 1, (short) 3)
                .setName("§aMine Crate Keys")
                .setLore(
                        "",
                        "§7Total: §e" + NumberUtils.formatFull(rewards.getKeys().getOrDefault("mine", 0L)),
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(rewards.getKeys().getOrDefault("mine", 0L) > 0 ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(mineKeysScript)
                .create();
        String[][] rareKeysScript = {{"autominer:collect", "keys:rare"}};
        ItemStack rareKeys = new ItemFactory(Material.STAINED_CLAY, 1, (short) 2)
                .setName("§dRare Crate Keys")
                .setLore(
                        "",
                        "§7Total: §e" + NumberUtils.formatFull(rewards.getKeys().getOrDefault("rare", 0L)),
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(rewards.getKeys().getOrDefault("rare", 0L) > 0 ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(rareKeysScript)
                .create();
        String[][] legendaryKeysScript = {{"autominer:collect", "keys:legendary"}};
        ItemStack legendaryKeys = new ItemFactory(Material.STAINED_CLAY, 1, (short) 14)
                .setName("§cLegendary Crate Keys")
                .setLore(
                        "",
                        "§7Total: §e" + NumberUtils.formatFull(rewards.getKeys().getOrDefault("legendary", 0L)),
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(rewards.getKeys().getOrDefault("legendary", 0L) > 0 ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(legendaryKeysScript)
                .create();

        String[][] socketGemDustScript = {{"autominer:collect", "dust"}};
        ItemStack socketGemDust = new ItemFactory(Material.SULPHUR)
                .setName("§eSocket Gem Dust")
                .setLore(
                        "",
                        "§7Total: §e" + NumberUtils.formatFull(rewards.getSocketGemDust()),
                        "",
                        "§b» Click to claim!"
                )
                .addEnchantment(rewards.getSocketGemDust() > 0 ? Enchantment.PROTECTION_ENVIRONMENTAL : null)
                .hideFlags()
                .setClickEvent(socketGemDustScript)
                .create();

        String[][] backScript = {{"inv:open", "autominer"}};
        ItemStack back = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aBack")
                .setLore("§7Return to Auto Miner menu!")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(backScript)
                .create();

        inv.setItem(11, tokens);
        inv.setItem(12, mineKeys);
        inv.setItem(13, rareKeys);
        inv.setItem(14, legendaryKeys);
        inv.setItem(15, socketGemDust);

        inv.setItem(31, back);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
