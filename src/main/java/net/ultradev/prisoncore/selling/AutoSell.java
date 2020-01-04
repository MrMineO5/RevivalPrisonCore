/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.selling;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pets.PetXp;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.selling.Selling.MultiplierInfo;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

public class AutoSell {
    private static Map<UUID, Boolean> autosellEnabled = new HashMap<>();
    private static HashMap<UUID, AutoSellInfo> autosell = new HashMap<>();
    private static HashMap<UUID, List<ItemStack>> toSell = new HashMap<>();

    public static boolean isAutosell(@NotNull Player player) {
        return autosellEnabled.containsKey(player.getUniqueId());
    }

    public static boolean toggleAutosell(Player player) {
        if (isAutosell(player)) {
            disableAutosell(player);
        } else {
            enableAutosell(player);
        }
        return isAutosell(player);
    }

    public static void enableAutosell(Player player) {
        Selling.sellall(player, true);
        autosellEnabled.put(player.getUniqueId(), player.hasPermission("ultraprison.autosell"));
        AutoSellInfo info = new AutoSellInfo(player);
        info.lastAutosell = DateUtils.getMilliTimeStamp();
        info.lastAutosellUpdate = DateUtils.getMilliTimeStamp();
        autosell.put(player.getUniqueId(), info);
        toSell.put(player.getUniqueId(), new ArrayList<>());
    }

    public static void disableAutosell(@NotNull Player player) {
        autosellNow(player);
        autosellEnabled.remove(player.getUniqueId());
        autosell.get(player.getUniqueId()).save(player);
        autosell.remove(player.getUniqueId());
    }

    public static void addItem(@NotNull Player player, ItemStack item) {
        List<ItemStack> items = toSell.get(player.getUniqueId());
        items.add(item);
        // toSell.put(player.getUniqueId(), items);
    }

    public static void addItems(@NotNull Player player, Collection<ItemStack> item) {
        List<ItemStack> items = toSell.get(player.getUniqueId());
        items.addAll(item);
        // toSell.put(player.getUniqueId(), items);
    }

    public static void autosellUpdate() {
        for (UUID id : autosellEnabled.keySet()) {
            autosellUpdate(Bukkit.getPlayer(id));
        }
    }

    private static void autosellUpdate(Player player) {
        UUID id = player.getUniqueId();
        if (!autosellEnabled.containsKey(id)) {
            return;
        }
        AutoSellInfo info = autosell.get(id);
        if (!autosellEnabled.get(id) && !PetManager.hasPet(player, PetType.AUTOSELL)) {
            info.autoSellTime = info.autoSellTime.subtract(BigInteger.valueOf(DateUtils.getMilliTimeStamp() - info.lastAutosellUpdate));
            if (info.autoSellTime.compareTo(BigInteger.ZERO) <= 0) {
                info.autoSellTime = BigInteger.ZERO;
                disableAutosell(Bukkit.getPlayer(id));
                return;
            }
        }
        info.lastAutosellUpdate = DateUtils.getMilliTimeStamp();
        if (info.lastAutosell + 15 * 1000 <= DateUtils.getMilliTimeStamp()) {
            autosellNow(player);
            info.lastAutosell = DateUtils.getMilliTimeStamp();
        }
    }

    private static void autosellNow(Player p) {
        if (p == null) {
            return;
        }
        UUID id = p.getUniqueId();
        if (autosellEnabled == null) {
            autosellEnabled = new HashMap<>();
            return;
        }
        final List<ItemStack> items = toSell.get(id);
        Synchronizer.desynchronize(() -> {
            Selling.AutoSellOutput output = Selling.getPrice(items);
            if (output.itemcount == 0) {
                return;
            }
            MultiplierInfo info = new MultiplierInfo(p);
            long sellPrice = Math.round(output.price * info.multiplier);
            if (MathUtils.isRandom(PickaxeUtils.getPercent(p, SocketGemType.MERCHANT), 100.0)) {
                sellPrice *= 2;
            }

            // Pet xp
            PetXp.addXp(p, PetXp.XpType.BLOCKS_SOLD, output.itemcount);
            PetXp.addXp(p, PetXp.XpType.TOKENS_EARNED, (int) output.price);

            // Add tokens
            Economy.tokens.addBalance(p, sellPrice);

            // Send message
            TextComponent component;
            if (autosellEnabled.get(id)) {
                component = new TextComponent("§7[§dAS§7] Sold §e" + NumberUtils.formatFull(output.itemcount)
                        + "§7 items for §e" + NumberUtils.formatFull(Math.round(sellPrice))
                        + " Tokens§7! (§bHover for multiplier details§7)");
            } else {
                component = new TextComponent("§7[§dAS§7] Sold §e" + NumberUtils.formatFull(output.itemcount)
                        + "§7 items for §e" + NumberUtils.formatFull(Math.round(sellPrice)) + " Tokens§7! (§a"
                        + DateUtils.convertTime(autosell.get(id).autoSellTime.divide(BigInteger.valueOf(1000)))
                        + " §7remaining) (§bHover for multiplier details§7)");
            }
            component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, info.multiplierHover));
            p.spigot().sendMessage(component);

            // Clear toSell
            toSell.put(id, new ArrayList<>());
        });
    }

    public static void setAutoSellTime(@NotNull Player player, BigInteger amount) {
        UUID id = player.getUniqueId();
        if (autosell.containsKey(id)) {
            autosell.get(id).autoSellTime = amount;
        }
        PlayerData.setAutoSellTime(player, amount);
    }

    private static BigInteger getAutoSellTime(@NotNull Player player) {
        UUID id = player.getUniqueId();
        if (autosell.containsKey(id)) {
            return autosell.get(id).autoSellTime;
        }
        return PlayerData.getAutoSellTime(player);
    }

    public static void addAutoSellTime(Player player, BigInteger amount) {
        setAutoSellTime(player, getAutoSellTime(player).add(amount));
    }

    private static class AutoSellInfo {
        long lastAutosell;
        long lastAutosellUpdate;
        BigInteger autoSellTime;

        AutoSellInfo(Player p) {
            this.lastAutosell = 0;
            this.lastAutosellUpdate = 0;
            this.autoSellTime = PlayerData.getAutoSellTime(p);
        }

        void save(Player player) {
            PlayerData.setAutoSellTime(player, this.autoSellTime);
        }
    }
}
