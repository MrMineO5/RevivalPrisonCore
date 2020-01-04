/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.tinker;

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

public class TinkerGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lTinkerer");

        String[][] craftingScript = {{"inv:open", "tinker_crafting"}};
        ItemStack crafting = new ItemFactory(Material.CLAY_BALL)
                .setName("§6Crafting")
                .setLore(
                        "§7Click to craft your Dust",
                        "§7into Socket Gem Essence!"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(craftingScript)
                .create();

        String[][] dismantleScript = {{"inv:open", "tinker_dismantle"}};
        ItemStack dismantle = new ItemFactory(Material.SULPHUR)
                .setName("§6Dismantle")
                .setLore(
                        "§7Click to dismantle your unwanted",
                        "§7Socket Gems into Dust!"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(dismantleScript)
                .create();

        inv.setItem(12, crafting);
        inv.setItem(14, dismantle);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(3));
        return inv;
    }
}
