/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.autominer.AutoMinerChip;
import net.ultradev.prisoncore.autominer.AutoMinerSkipper;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.multipliers.MultiplierManager;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pickaxe.RenameToken;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemEssence;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemGenerator;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.rankupgrade.RankUpgrade;
import net.ultradev.prisoncore.selling.AutoSell;
import net.ultradev.prisoncore.selling.AutoSellEssence;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.text.Messages;
import net.ultradev.prisoncore.utils.time.DateUtils;
import net.ultradev.prisoncore.withdraw.TokenItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;

public class RightClickEvents implements Listener {
    public Plugin plugin = null;

    @EventHandler
    public void onRightClick(@NotNull PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            return;
        }
        Debugger.log("Checking right click events", "right_click");
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (AutoSellEssence.isAutoSellEssence(item)) {
            int amount = 1;
            if (player.isSneaking())
                amount = item.getAmount();
            BigInteger time = Objects.requireNonNull(NBTUtils.getBigInteger(item, "time")).multiply(BigInteger.valueOf(amount));
            AutoSell.addAutoSellTime(player, time);
            item.setAmount(item.getAmount() - amount);
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Messages.AUTOSELL_CONSUME
                    .get(DateUtils.convertTime(time.divide(BigInteger.valueOf(1000)))));
        }
        ScrollUtils.applyScroll(event.getPlayer());
        if (RankUpgrade.isRankUpgrade(item)) {
            if (RankUpgrade.isHighestRank(player)) {
                player.sendMessage("§cYou are already at the highest rank.");
                return;
            }
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
            RankUpgrade.applyRankUpgrade(player);
            Bukkit.broadcastMessage("§c§lANNOUNCEMENT §7> §6" + player.getName() + "§c used a rank upgrade!");
        }
        if (Multiplier.isMultiplier(item)) {
            Debugger.log("Item is multiplier", "right_click");
            Multiplier multi = Multiplier.parseFromItem(item);
            Debugger.log("Parsed multiplier", "right_click");
            boolean success = MultiplierManager.activateMulti(player, multi);
            if (success) {
                Debugger.log("Sending multiplier activation message", "right_click");
                player.sendMessage(multi.getActivationMessage());
            }
            InvUtils.decrementHand(player);
        }
        if (AutoMinerChip.isAutoMinerChip(item)) {
            int amount = 1;
            if (player.isSneaking())
                amount = item.getAmount();
            BigInteger time = AutoMinerChip.getTime(item).multiply(BigInteger.valueOf(amount));
            AutoMinerUtils.getModel(player).addAutominerTime(time);
            item.setAmount(item.getAmount() - amount);
            player.sendMessage(Messages.AUTOSELL_CONSUME
                    .get(DateUtils.convertTimeM(time)));
        }
        if (TokenItem.isTokenItem(item)) {
            if (player.isSneaking()) {
                Economy.tokens.addBalance(player, TokenItem.getTokens(item) * item.getAmount());
                player.getInventory().setItemInMainHand(null);
            } else {
                Economy.tokens.addBalance(player, TokenItem.getTokens(item));
                InvUtils.decrementHand(player);
            }
        }
        if (RenameToken.isRenameToken(item)) {
            InvUtils.decrementHand(player);
            Main.getSignGui().open(player, (player1, lines) -> {
                String name = ChatColor.translateAlternateColorCodes('&', lines[0]);
                Synchronizer.synchronize(() -> {
                    ItemStack pick = player1.getInventory().getItem(0);
                    ItemMeta meta = pick.getItemMeta();
                    meta.setDisplayName(name);
                    pick.setItemMeta(meta);
                    player1.getInventory().setItem(0, pick);
                });
            });
        }
        if (SocketGemEssence.isSocketGemEssence(item)) {
            SocketGemTier tier = SocketGemEssence.getTier(item);
            InvUtils.decrementHand(player);
            player.getInventory().addItem(SocketGemGenerator.generateSocketGem(tier).getItem());
        }
        if (AutoMinerSkipper.isAutoMinerSkipper(item)) {
            if (!AutoMinerUtils.getModel(player).run) {
                player.sendMessage("§cYour autominer must be running in order to use that!");
                return;
            }
            int time;
            if (player.isSneaking()) {
                time = (int) (AutoMinerSkipper.getTime(item) / 1000L * item.getAmount());
                player.getInventory().setItemInMainHand(null);
            } else {
                time = (int) (AutoMinerSkipper.getTime(item) / 1000L);
                InvUtils.decrementHand(player);
            }
            AutoMinerUtils.getModel(player).skip(time);
        }
        if (PetManager.isPet(item)) {
            PetType type = PetManager.getType(item);
            if (!type.hasGUI()) {
                return;
            }
            GUIManager.openGUI(player, "pet_" + type.name().toLowerCase());
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (PetManager.isPet(e.getItemInHand())) {
            e.setCancelled(true);
        }
    }
}
