/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.pickaxe;

import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
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

public class Pickaxe_SocketsGUI implements GUI {

    private ItemStack generateSocketGem(Player player, int number) {
        if (PickaxeUtils.getSocketGem(player, number) != null) {
            String[][] script = {{"socketgem:remove", Integer.toString(number)}, {"inv:open", "pickaxe_sockets"}};
            ItemStack item = PickaxeUtils.getSocketGem(player, number).getItem();
            return GUIUtils.addClickEvent(item, script);
        }
        if (PickaxeUtils.hasUnlockedSocketGem(player, number)) {
            String[][] script = {{"socketgem:put", Integer.toString(number)}, {"inv:open", "pickaxe_sockets"}};
            return new ItemFactory(Material.STAINED_GLASS_PANE, 1, (short) 5)
                    .setName("§aEmpty Slot")
                    .setLore(
                            "§7Place a Socket Gem here",
                            "§7to add it to your pickaxe!"
                    )
                    .setClickEvent(script)
                    .create();
        } else {
            return new ItemFactory(Material.STAINED_GLASS_PANE, 1, (short) 14)
                    .setName("§cLocked")
                    .setLore(
                            "§7This Socket Gem slot requires",
                            "§7Pickaxe Level §e" + PickaxeUtils.socketRequiredLevels[number] + "§7!"
                    )
                    .setClickEvent(GUIUtils.noop)
                    .create();
        }
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 54, "§7        §4§lPickaxe Upgrades");

        ItemStack info = new ItemFactory(Material.BOOK)
                .setName("§eInformation")
                .setLore(
                        "§7Socket Gems can be applied to empty",
                        "§7slots to give your pickaxe unique",
                        "§7effects and bonuses!",
                        "§7",
                        "§7Drag and drop a Socket Gem into an empty",
                        "§7slot to apply it. Note that once a Socket",
                        "§7Gem has been added to your pickaxe, it",
                        "§7cannot be removed without destroying it.",
                        "§7",
                        "§eClick to learn more about Socket Gems",
                        "§eand where you can find them!"
                )
                .setClickEvent(GUIUtils.noop)
                .hideFlags()
                .create();

        String[][] backScript = {{"inv:open", "pickaxe"}};
        ItemStack back = new ItemFactory(Material.DIAMOND_PICKAXE)
                .setName("§aReturn")
                .setLore(
                        "§7Click to return to",
                        "§7Pickaxe Upgrades!"
                )
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .hideFlags()
                .setClickEvent(backScript)
                .create();

        inv.setItem(11, generateSocketGem(player, 0));
        inv.setItem(12, generateSocketGem(player, 1));
        inv.setItem(13, generateSocketGem(player, 2));
        inv.setItem(14, generateSocketGem(player, 3));
        inv.setItem(15, generateSocketGem(player, 4));

        inv.setItem(21, generateSocketGem(player, 5));
        inv.setItem(22, generateSocketGem(player, 6));
        inv.setItem(23, generateSocketGem(player, 7));

        inv.setItem(39, back);
        inv.setItem(41, info);
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller((short) 3));
        return inv;
    }
}
