/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.tinker;

import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
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

public class Tinker_CraftingGUI implements GUI {
    private ItemStack createItem(SocketGemTier tier) {
        String[][] itemscript = {{"tinker:craft", tier.name()}};
        return new ItemFactory(Material.CLAY_BALL)
                .setName(tier.getDisplayName() + "§7 Essence")
                .setLore(
                        "§7Right click to receive a",
                        "§7random " + tier.getName() + " Socket Gem!",
                        "",
                        "§eCost: §b" + tier.getPrice() + " Dust"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .setClickEvent(itemscript)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4§lCraft");

        inv.setItem(10, createItem(SocketGemTier.COMMON));
        inv.setItem(11, createItem(SocketGemTier.RARE));
        inv.setItem(12, createItem(SocketGemTier.LEGENDARY));
        inv.setItem(13, createItem(SocketGemTier.MYTHICAL));
        inv.setItem(14, createItem(SocketGemTier.EPIC));

        ItemStack info = new ItemFactory(Material.BOOK)
                .setName("§7Socket Gems")
                .setLore(
                        "§7Socket gems can be obtained",
                        "§7from Socket Gem Essence,",
                        "§7which is crafted from Socket",
                        "§7Gem Dust. You can also dismantle",
                        "§7unwanted socket gems back into",
                        "§7Socket Gem Dust, however it is",
                        "§7more expensive to craft Socket",
                        "§7Gem Essence than the amount",
                        "§7obtained by dismantling. Socket",
                        "§7Gems amplify the effects of",
                        "§7enchantments on your pickaxe,",
                        "§7there is however a limited",
                        "§7amount of slots for Socket",
                        "§7Gems, increased by your",
                        "§7Pickaxe Level."
                )
                .setClickEvent(GUIUtils.noop)
                .create();

        inv.setItem(16, info);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
