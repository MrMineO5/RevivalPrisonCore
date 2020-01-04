/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.shadytrader;

import lombok.Data;
import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShadyTraderGUI implements GUI {
    public static Map<String, ShadyTrade> trades = new HashMap<>();

    static {
        trades.put("p7bomb", new ShadyTrade(Bombs.generateBombItem(7, 3), 50));
        trades.put("cata", new ShadyTrade(ScrollUtils.createScroll(CustomEnchant.CATACLYSM), 1000));
        trades.put("myth", new ShadyTrade(CrateManager.getCrate("mythical").getKey(1), 100));
        trades.put("leg", new ShadyTrade(CrateManager.getCrate("legendary").getKey(5), 100));
    }

    private ItemStack generateItem(String name) {
        ShadyTrade trade = trades.get(name);
        String[][] script = {{"shadytrader:buy", name}};
        ItemStack i = ItemUtils.addToLore(trade.getItem(), "");
        i = ItemUtils.addToLore(i, "§7Price: §b§l" + NumberUtils.formatFull(trade.getPrice()) + " Mysterious Crystals");
        i = GUIUtils.addClickEvent(i, script);
        return i;
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lShady Trader");

        inv.setItem(11, generateItem("p7bomb"));
        inv.setItem(12, generateItem("cata"));
        inv.setItem(14, generateItem("leg"));
        inv.setItem(15, generateItem("myth"));

        inv = InvUtils.fillEmpty(inv, GUIUtils.filler);
        return inv;
    }

    @Data
    public static class ShadyTrade {
        private ItemStack item;
        private int price;

        public ShadyTrade(ItemStack item, int price) {
            this.item = item;
            this.price = price;
        }
    }
}
