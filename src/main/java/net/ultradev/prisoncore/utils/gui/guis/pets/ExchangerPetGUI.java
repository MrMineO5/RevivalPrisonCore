/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.pets;

import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.utils.gui.GUIClickType;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ExchangerPetGUI implements GUI {
    private ItemStack generateItem(ItemStack pet, String setting, int levelReq) {
        int level = PetManager.getLevel(pet);
        boolean canUse = level >= levelReq;
        int val = PetManager.getSetting(pet, setting);
        int data = (val == 0) ? 14 : ((val == 1) ? 4 : 5);
        char color = (val == 0) ? 'c' : ((val == 1) ? 'e' : 'a');
        String[][] script = {{"inv:open", "pet_exchanger"}};
        String[][] leftScript = {{"pet:set", setting, "2"}};
        String[][] middleScript = {{"pet:set", setting, "1"}};
        String[][] rightScript = {{"pet:set", setting, "0"}};
        return (new ItemFactory(Material.WOOL, 1, data))
                .setName("§" + color + setting.replaceAll("_", " ") + (canUse ? "" : " §c(§c§lRequires Level " + levelReq + "§c)"))
                .setLore(
                        "§7Right Click: Disable",
                        "§7Middle Click: Exchange",
                        "§7Left Click: Auto Use (Exchanges if it cannot use)"
                )
                .setClickEvent(script)
                .setClickEvent(GUIClickType.LEFTCLICK, canUse ? leftScript : null)
                .setClickEvent(GUIClickType.MIDDLECLICK, canUse ? middleScript : null)
                .setClickEvent(GUIClickType.RIGHTCLICK, canUse ? rightScript : null)
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 27, "§2§lExchanger Pet");
        ItemStack item = player.getInventory().getItemInMainHand();
        if (PetManager.getType(item) != PetType.EXCHANGER) {
            inv = InvUtils.fillEmpty(inv, GUIUtils.error);
            return inv;
        }
        inv.setItem(10, generateItem(item, "AutoSell_Essence", 1));
        inv.setItem(11, generateItem(item, "AutoMiner_Chips", 2));
        inv.setItem(12, generateItem(item, "Bombs", 3));
        inv.setItem(13, generateItem(item, "Multipliers", 4));
        inv.setItem(14, generateItem(item, "Scrolls", 5));
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
