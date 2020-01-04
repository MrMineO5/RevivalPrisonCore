/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.dustbank;

import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.utils.gui.GUIClickType;
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

import java.math.BigInteger;

public class DustBankGUI implements GUI {
    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 9, "§2§lDust bank");

        BigInteger balance = Economy.dust.getBalance(player);

        String[][] script = {{"inv:open", "dustbank"}};
        String[][] leftScript = {{"dustbank:withdraw", "1"}};
        String[][] rightScript = {{"dustbank:withdraw", "16"}};
        String[][] middleScript = {{"dustbank:withdraw", "64"}};
        String[][] shiftScript = {{"dustbank:insert"}};
        ItemStack item = new ItemFactory(Material.SULPHUR)
                .setName("§dDust")
                .setLore(
                        "§7Balance: §e" + NumberUtils.formatFull(balance) + " dust",
                        "",
                        "§eLeft Click: Withdraw 1",
                        "§eRight Click: Withdraw 16",
                        "§eMiddle Click: Withdraw 64",
                        "",
                        "§eShift Click: Insert all"
                )
                .setClickEvent(script)
                .setClickEvent(GUIClickType.LEFTCLICK, leftScript)
                .setClickEvent(GUIClickType.RIGHTCLICK, rightScript)
                .setClickEvent(GUIClickType.MIDDLECLICK, middleScript)
                .setClickEvent(GUIClickType.SHIFTCLICK, shiftScript)
                .addEnchantment(balance.compareTo(BigInteger.ZERO) > 0 ? Enchantment.DIG_SPEED : null)
                .hideFlags()
                .create();

        inv.setItem(4, item);

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
