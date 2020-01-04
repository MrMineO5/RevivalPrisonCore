/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.autominer;

import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Autominer_SkinsGUI implements GUI {
    private ItemStack generateItem(String displayName, String name) {
        String[][] script = {{"autominer:reskin", name}, {"inv:close"}};
        ItemStack item = new ItemFactory(Material.SKULL_ITEM, 1, (short) 3)
                .setName(displayName)
                .setLore(
                        "",
                        "§b» Click to apply this skin!",
                        ""
                )
                .setClickEvent(script)
                .create();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        item.setItemMeta(meta);
        return item;
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lAutominer");

        String[][] backScript = {{"inv:open", "autominer"}};
        ItemStack back = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aBack")
                .setLore("§7Return to Auto Miner menu!")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(backScript)
                .create();

        inv.setItem(22, back);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
