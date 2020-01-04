/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.autominer;

import net.ultradev.prisoncore.autominer.AutoMinerData;
import net.ultradev.prisoncore.autominer.AutoMinerUpgrade;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Autominer_UpgradesGUI implements GUI {
    private ItemStack generateUpgradeItem(AutoMinerData data, AutoMinerUpgrade upgrade) {
        int level = data.getUpgradeLevel(upgrade);
        int max = upgrade.getMaxLevel();
        List<String> lore = upgrade.getDescription();
        lore.add("");
        lore.add("§eCurrent Level: §f" + level);
        lore.add("§eMax Level: §f" + max);
        lore.add("");
        if (level < max) {
            lore.add("§6Cost: §e" + NumberUtils.formatFull(upgrade.getPrice(data.getUpgradeLevel(upgrade))));
        } else {
            lore.add("§aMaxed!");
        }
        String[][] script = {{"autominer:upgrade", upgrade.name()}, {"inv:open", "autominer_upgrades"}};
        return new ItemFactory(Material.BOOK)
                .setName("§e" + upgrade.getName())
                .setLore(lore)
                .setClickEvent((level < max) ? script : GUIUtils.noop)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 45, "§4§lAutominer");

        AutoMinerData data = AutoMinerUtils.getModel(player).data;

        String[][] backScript = {{"inv:open", "autominer"}};
        ItemStack back = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aBack")
                .setLore("§7Return to Auto Miner menu!")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(backScript)
                .create();

        inv.setItem(11, generateUpgradeItem(data, AutoMinerUpgrade.MORE_KEYS));
        inv.setItem(12, generateUpgradeItem(data, AutoMinerUpgrade.BETTER_KEYS));
        inv.setItem(13, generateUpgradeItem(data, AutoMinerUpgrade.MORE_DUST));
        inv.setItem(14, generateUpgradeItem(data, AutoMinerUpgrade.MORE_TOKENS));
        //inv.setItem(15, generateUpgradeItem(data, AutoMinerUpgrade.LESS_TIME));

        inv.setItem(31, back);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
