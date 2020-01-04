/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.ultradev.prisoncore.playerdata.StaffRank;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class NBTCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("ultraprison.admin")) {
            player.sendMessage(Messages.NO_PERMISSION_COMMAND.get(StaffRank.ADMIN.getPrefix()));
            return false;
        }
        if (args.length < 1) {
            player.sendMessage("§cUsages:");
            player.sendMessage("§7- /NBT info: Show NBT information about your held item");
            return true;
        }
        if (args[0].equalsIgnoreCase("info")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null) {
                player.sendMessage("§cYou must be holding an item to do that.");
                return false;
            }
            NBTTagCompound compound = NBTUtils.getNBTCompound(item);
            Set<String> elements = compound.c();
            elements.forEach(element -> player.sendMessage("§7- " + element + ": " + compound.get(element).toString()));
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 4) {
                player.sendMessage("§cUsage: /NBT set <Name> <Type> <Value>");
                return false;
            }
        }
        return true;
    }
}
