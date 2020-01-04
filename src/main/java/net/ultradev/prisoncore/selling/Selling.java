/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.selling;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.equipment.Equipment;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.multipliers.MultiplierManager;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pets.PetXp;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.ArrayUtils;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Selling {
    public static void sellall(Player player) {
        sellall(player, false);
    }
    public static void sellall(Player player, boolean force) {
        if (AutoSell.isAutosell(player) && !force) {
            return;
        }
        if (!CooldownUtils.isCooldown(player, "sell") && !force) {
            player.sendMessage("§cPlease wait before selling again.");
            return;
        }
        Inventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();

        int itemcount = 0;
        long total = 0;

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) {
                continue;
            }
            Integer price = Main.getSellPrice(item.getType());
            if (price != null) {
                itemcount += item.getAmount();
                total += price * item.getAmount();
                contents[i] = null;
            }
        }

        if (total == 0.0) {
            player.sendMessage("§cYou do not have enough items to sell.");
            return;
        }

        MultiplierInfo info = new MultiplierInfo(player);

        double multi = info.multiplier;

        total = MathUtils.roundRand(total * multi);
        if (MathUtils.isRandom(PickaxeUtils.getPercent(player, SocketGemType.MERCHANT), 100.0)) {
            total *= 2;
        }

        // Add balance
        Economy.tokens.addBalance(player, total);

        // Modify pet xp
        PetXp.addXp(player, PetXp.XpType.BLOCKS_SOLD, itemcount);
        PetXp.addXp(player, PetXp.XpType.TOKENS_EARNED, (int) total);

        // Modify inventory
        player.getInventory().setContents(contents);

        // Send message
        TextComponent component = new TextComponent("§7Sold inventory for §e" + NumberUtils.formatFull(total)
                + " Tokens§7! (§bHover for multiplier details§7)");
        component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, info.multiplierHover));
        player.spigot().sendMessage(component);
        if (!player.hasPermission("ultraprison.autosell")) {
            player.sendMessage("§cPurchase §e§lAuto Sell§c at §bstore.revivalprison.com");
        }

        // Add cooldown
        CooldownUtils.setCooldown(player, "sell", 5);
    }

    static AutoSellOutput getPrice(List<ItemStack> list) {
        long total = 0;
        int itemcount = 0;
        if (list == null || list.isEmpty())
            return new AutoSellOutput(itemcount, total);

        for (int i = 0; i < list.size(); i++) {
            ItemStack item = list.get(i);
            if (item == null) {
                continue;
            }
            Integer price = Main.getSellPrice(item.getType());
            if (price != null) {
                total += price * item.getAmount();
                itemcount += item.getAmount();
            }
        }
        return new AutoSellOutput(itemcount, total);
    }

    static class AutoSellOutput {
        int itemcount;
        long price;

        AutoSellOutput(int itemcount, long price) {
            this.itemcount = itemcount;
            this.price = price;
        }
    }

    static class MultiplierInfo {
        private static BaseComponent[] newLine = new ComponentBuilder("\n").create();

        double multiplier;
        BaseComponent[] multiplierHover;

        MultiplierInfo(Player player) {
            double serverRankMulti = 0.01 * PlayerData.getRank(player);
            double storeRankMulti = PlayerData.getStoreRank(player).getMultiplier();
            double sellMulti = MultiplierManager.getMultiplier(player, Multiplier.MultiplierType.MONEY);
            double pickaxeMulti = PickaxeUtils.getMultiplier(player);
            double equipmentMulti = Equipment.getMultiplier(player);
            double petMulti = 0.1 * PetManager.getLevel(player, PetType.SELL);

            double totalMulti = serverRankMulti + storeRankMulti + sellMulti + pickaxeMulti + equipmentMulti + petMulti;

            double merchantPetChance = PetManager.getLevel(player, PetType.MERCHANT);
            if (MathUtils.isRandom(merchantPetChance, 100.0)) {
                petMulti += totalMulti;
                totalMulti *= 2.0;
            }

            BaseComponent[] title = new ComponentBuilder("§dMultiplier Details").create();

            BaseComponent[] serverRankComponent = new ComponentBuilder("§7- §eServer Rank §7(" + RankupManager.getRankDisplayName(PlayerData.getRank(player)) + "§7): §b" + serverRankMulti + "x")
                    .create();
            BaseComponent[] storeRankComponent = new ComponentBuilder("§7- §eStore Rank §7(§a" + PlayerData.getStoreRank(player) + "§7): §b" + storeRankMulti + "x")
                    .create();
            BaseComponent[] sellMultiComponent = new ComponentBuilder("§7- §eSell Multiplier: §b" + sellMulti + "x")
                    .create();
            BaseComponent[] pickaxeComponent = new ComponentBuilder("§7- §ePickaxe Upgrades: §b" + pickaxeMulti + "x")
                    .create();
            BaseComponent[] equipmentComponent = new ComponentBuilder("§7- §eEquipment Bonus: §b" + equipmentMulti + "x")
                    .create();
            BaseComponent[] petComponent = new ComponentBuilder("§7- §ePet Bonus: §b" + petMulti + "x")
                    .create();

            this.multiplierHover = ArrayUtils.mergeAll(title, newLine, newLine, serverRankComponent, newLine,
                    storeRankComponent, newLine, sellMultiComponent, newLine, pickaxeComponent, newLine,
                    equipmentComponent, newLine, petComponent);
            this.multiplier = totalMulti + 1.0;
        }
    }
}
