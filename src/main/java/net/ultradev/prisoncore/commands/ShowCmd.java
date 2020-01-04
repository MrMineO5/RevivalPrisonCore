/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShowCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be used from console.");
            return false;
        }
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        net.minecraft.server.v1_12_R1.NBTTagCompound compound = new NBTTagCompound();
        net.minecraft.server.v1_12_R1.ItemStack it = NBTUtils
                .convertToNMS(item);
        assert it != null;
        it.save(compound);
        BaseComponent[] hover = {new TextComponent(compound.toString())};
        int amount = InvUtils.getCount(item, player.getInventory());
        TextComponent component = new TextComponent("§f" + player.getName() + "§7 is showcasing §b[" + it.getName()
                + (amount == 1 ? "§b]" : "§r §8(§7x" + amount + "§8)§b]"));
        component.setHoverEvent(new HoverEvent(Action.SHOW_ITEM, hover));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(component);
        }
        return false;
    }
}