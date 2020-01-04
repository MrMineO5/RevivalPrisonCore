/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.keyvault;

import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.utils.gui.GUIClickType;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KeyVaultGUI implements GUI {
    private static ItemStack generateWithdrawItem(String type, Player player) {
        int amount = KeyVaultManager.getKeys(player.getInventory().getItemInMainHand(), type);
        Crate cr = CrateManager.getCrate(type);
        if (cr == null) {
            Debugger.error("Could net get crate for: " + type, "keyVaultGUI");
            return new ItemFactory(Material.BARRIER)
                    .setName("§cError")
                    .setClickEvent(GUIUtils.noop)
                    .create();
        }
        String[][] leftScript = {{"keyvault:withdraw", type, "1"}, {"inv:open", "keyvault"}};
        String[][] rightScript = {{"keyvault:withdraw", type, "16"}, {"inv:open", "keyvault"}};
        String[][] middleScript = {{"keyvault:withdraw", type, "64"}, {"inv:open", "keyvault"}};
        String[][] shiftScript = {{"keyvault:insert", type}, {"inv:open", "keyvault"}};
        return new ItemFactory(Material.TRIPWIRE_HOOK)
                .setName(cr.displayName + " Keys")
                .setLore(
                        "§7Amount: §e" + NumberUtils.formatFull(amount),
                        "",
                        "§eLeft Click: Withdraw 1",
                        "§eRight Click: Withdraw 16",
                        "§eMiddle Click: Withdraw 64",
                        "",
                        "§eShift Click: Insert all"
                )
                .setClickEvent(GUIUtils.noop)
                .setClickEvent(GUIClickType.LEFTCLICK, leftScript)
                .setClickEvent(GUIClickType.RIGHTCLICK, rightScript)
                .setClickEvent(GUIClickType.MIDDLECLICK, middleScript)
                .setClickEvent(GUIClickType.SHIFTCLICK, shiftScript)
                .addEnchantment(amount > 0 ? Enchantment.DIG_SPEED : null)
                .hideFlags()
                .create();
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 9, "§e§lKey Vault");

        inv.setItem(1, generateWithdrawItem("mine", player));
        inv.setItem(3, generateWithdrawItem("rare", player));
        inv.setItem(5, generateWithdrawItem("legendary", player));

        boolean mergeMode = KeyVaultManager.isMergeMode(player.getInventory().getItemInMainHand());

        String[][] script = {{"keyvault:togglemerge"}, {"inv:open", "keyvault"}};
        ItemStack toggleMerge = new ItemFactory(mergeMode ? Material.PISTON_STICKY_BASE : Material.PISTON_BASE)
                .setName((mergeMode ? "§a" : "§c") + "Merge Mode")
                .setLore(
                        "§7Merge mode merges bad keys",
                        "§7into better keys",
                        "",
                        "§764 Mine -> 1 Rare",
                        "§764 Rare -> 1 Legendary"
                )
                .setClickEvent(script)
                .addEnchantment(mergeMode ? Enchantment.ARROW_DAMAGE : null)
                .hideFlags()
                .create();

        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }
}
