/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.mailbox;

import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MailboxGUI implements GUI {
    private ItemStack getItem(ItemStack item, int i, int page) {
        String[][] script = {{"mailbox:take", Integer.toString(i)}, {"inv:open", "mailbox", Integer.toString(page)}};
        return GUIUtils.addClickEvent(item, script);
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 54, "§7Mailbox");
        int page = args.length == 1 ? Integer.parseInt(args[0]) : 0;
        List<ItemStack> mailbox = PlayerData.getMailbox(player);
        int requiredPages = Math.floorDiv(mailbox.size(), 45);
        if (requiredPages < page) {
            return generateGUI(player, Integer.toString(requiredPages));
        }
        ItemStack[] contents = inv.getContents();
        for (int i = page * 45; i < 45 * (page + 1); i++) {
            if (mailbox.size() <= i) {
                break;
            }
            contents[i] = getItem(mailbox.get(i), i, page);
        }
        inv.setContents(contents);
        if (requiredPages > page) {
            String[][] nextPageScript = {{"inv:open", "mailbox", Integer.toString(page + 1)}};
            ItemStack nextPage = new ItemFactory(Material.ARROW)
                    .setName("§aNext Page")
                    .setClickEvent(nextPageScript)
                    .create();
            inv.setItem(51, nextPage);
        }
        if (page > 0) {
            String[][] prevPageScript = {{"inv:open", "mailbox", Integer.toString(page - 1)}};
            ItemStack prevPage = new ItemFactory(Material.ARROW)
                    .setName("§aPrevious Page")
                    .setClickEvent(prevPageScript)
                    .create();
            inv.setItem(47, prevPage);
        }
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(8));
        return inv;
    }
}
