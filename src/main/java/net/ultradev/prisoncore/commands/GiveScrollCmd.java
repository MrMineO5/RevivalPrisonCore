/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveScrollCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get("§7[§4§lAdmin§7]"));
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage("§cUsage: §7/givescroll <Player> <Enchantment> <Amount>");
            return true;
        }
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE.get());
            return true;
        }
        Enchantment enchant = CustomEnchant.getByName(args[1]);
        if (enchant == null) {
            sender.sendMessage(Messages.INVALID_ENCHANT.get(args[1]));
            return true;
        }
        Integer amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.INVALID_INTEGER.get(args[2]));
            return true;
        }
        ItemStack scroll = ScrollUtils.createScroll(enchant);
        InvUtils.giveItemsMailbox(p, scroll, amount);
        return true;
    }
}
