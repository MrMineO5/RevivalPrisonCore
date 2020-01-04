/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.crates.hopper.CrateHopperItem;
import net.ultradev.prisoncore.crates.hopper.CrateHoppers;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.keyvaults.KeyVaults;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pets.PetXp;
import net.ultradev.prisoncore.pets.pets.ExchangerPet;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.Exchanger;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rewards.ItemReward;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.rewards.rewards.TokenReward;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class CrateEvents implements Listener {
    public Plugin plugin = null;

    CrateEvents() {

    }

    @EventHandler
    public void onEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (CrateManager.isAdminCrate(item)) {
            CrateManager.placeCrate(event.getBlock(), CrateManager.getType(item));
        }
        if (CrateHopperItem.isCrateHopperItem(item)) {
            CrateHoppers.createHopper(event.getBlock().getLocation(), event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        if (!event.getPlayer().hasPermission("excelsior.crate.break")) {
            return;
        }
        if (CrateManager.isCrate(event.getBlock().getLocation())) {
            if (event.getPlayer().isSneaking()) {
                CrateManager.breakCrate(event.getBlock());
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§7Sneak to break a crate");
            }
        }
        if (CrateHoppers.removeHopper(event.getBlock().getLocation())) {
            event.setDropItems(false);
            loc.getWorld().dropItem(loc, CrateHopperItem.getCrateHopperItem(1));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRightClickCrate(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Debugger.log("Checking for crate at " + TextUtils.toText(e.getClickedBlock().getLocation()), "crate_events");
                if (!CrateManager.isCrate(e.getClickedBlock().getLocation()))
                    return;
                Debugger.log("Crate found. Getting crate type...", "crate_events");
                Crate crate = CrateManager.getCrate(e.getClickedBlock().getLocation());
                assert crate != null;
                GUIManager.openGUI(e.getPlayer(), "crate_preview", crate.name);
            }
            return;
        }
        Location loc = e.getClickedBlock().getLocation();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        CrateHoppers.CrateHopper ch = CrateHoppers.getHopper(loc);
        if (ch != null) {
            if (KeyVaultManager.isKeyvault(item)) {
                Map<String, Integer> keys = KeyVaults.deserialize(Objects.requireNonNull(NBTUtils.getString(item, "kvdata")));
                keys.forEach(ch::addKeys);
                ItemStack newItem = item.clone();
                for (String k : keys.keySet()) {
                    newItem = KeyVaultManager.removeKeys(newItem, k, keys.get(k));
                }
                player.getInventory().setItemInMainHand(newItem);
            } else {
                Map<String, Long> keys = ch.getKeys();
                player.sendMessage("§7========== §c§lHopper§7 ==========");
                keys.forEach((key, value) -> {
                    Crate cr = CrateManager.getCrate(key);
                    assert cr != null;
                    player.sendMessage("§7- " + cr.displayName + "§7: " + NumberUtils.formatFull(value));
                });
                e.setCancelled(true);
            }
        }
        if (!CrateManager.isCrate(e.getClickedBlock().getLocation()))
            return;
        e.setCancelled(true);
        if (CrateManager.isKey(item)) {
            String type = Objects.requireNonNull(CrateManager.getType(item)).name;
            Crate crate = CrateManager.getCrate(e.getClickedBlock().getLocation());
            assert crate != null;
            if (crate.name.equals(type)) {
                if (InvUtils.isFull(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cYou do not have enough space in your inventory.");
                    return;
                }
                int slot = PetManager.getPet(player, PetType.EXCHANGER);
                ItemStack pet = (slot == -1) ? null : player.getInventory().getItem(slot);
                ExchangerPet epet = ExchangerPet.get(pet);

                boolean message = PlayerData.getSetting(player, "crate_message");
                if (player.isSneaking()) {
                    final int amountL = item.getAmount();
                    Synchronizer.desynchronize(() -> {
                        int totalreal = 0;
                        List<ItemReward> autoExchange = new ArrayList<>();
                        List<ItemReward> exchangePet = new ArrayList<>();
                        for (int i = 0; i < amountL; i++) {
                            Reward reward = crate.getReward();
                            totalreal = i + 1;
                            if (epet != null && epet.shouldModify(reward)) {
                                exchangePet.add((ItemReward) reward);
                                return;
                            }
                            if (Exchanger.shouldAutoExchange(reward, player)) {
                                autoExchange.add((ItemReward) reward);
                                continue;
                            }
                            if (reward == null) {
                                continue;
                            }
                            if (!reward.canApply(player)) {
                                player.sendMessage("§7You do not have enough space in your inventory.");
                                totalreal = i;
                                break;
                            }
                            if (reward instanceof TokenReward) {
                                reward.applyReward(player, message ? (name, amount) -> crate.displayName
                                        + " Crate » §7You won §e" + amount + " Tokens§7!" : null);
                            } else {
                                reward.applyReward(player, message ? (name, amount) -> crate.displayName
                                        + " Crate » §7You won " + amount + "x §e" + name + "§7!" : null);
                            }
                            totalreal = i + 1;
                        }
                        Exchanger.AutoExchangerResult res = Exchanger.autoExchange(autoExchange);
                        if (res.itemcount > 0) {
                            Economy.tokens.addBalance(player, res.tokens);
                        }
                        int finalTotalreal = totalreal;
                        Synchronizer.synchronize(() -> {
                            // Exchanger pet
                            if (!exchangePet.isEmpty()) {
                                assert epet != null;
                                epet.run(player, exchangePet);
                            }

                            PetXp.addXp(player, PetXp.XpType.CRATES_OPENED, finalTotalreal);
                            item.setAmount(amountL - finalTotalreal);
                            player.getInventory().setItemInMainHand(item);
                        });
                    });
                } else {
                    Reward reward = crate.getReward();
                    if (epet != null && epet.shouldModify(reward)) {
                        epet.run(player, Collections.singletonList((ItemReward) reward));
                    }
                    if (Exchanger.shouldAutoExchange(reward, player)) {
                        Exchanger.AutoExchangerResult res = Exchanger.autoExchange((ItemReward) reward);
                        if (res.itemcount > 0) {
                            Economy.tokens.addBalance(player, res.tokens);
                        }
                    } else {
                        if (!reward.canApply(player)) {
                            player.sendMessage("§7You do not have enough space in your inventory.");
                            return;
                        }
                        Synchronizer.synchronize(() -> {
                            if (reward instanceof TokenReward) {
                                reward.applyReward(player, message ? (name, amount) -> crate.displayName
                                        + " Crate » §7You won §e" + amount + " Tokens§7!" : null);
                            } else {
                                reward.applyReward(player, message ? (name, amount) -> crate.displayName
                                        + " Crate » §7You won " + amount + "x §e" + name + "§7!" : null);
                            }
                        });
                    }
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItemInMainHand(item);
                    player.updateInventory();
                    PetXp.addXp(player, PetXp.XpType.CRATES_OPENED, 1);
                }
            }
        } else {
            if (KeyVaultManager.isKeyvault(item)) {
                if (InvUtils.isFull(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cYou do not have enough space in your inventory.");
                    return;
                }
                int slot = player.getInventory().getHeldItemSlot();
                Crate crate = CrateManager.getCrate(e.getClickedBlock().getLocation());
                assert crate != null;
                String type = crate.name;
                Player p = e.getPlayer();
                int keys = KeyVaultManager.getKeys(item, crate.name);
                if (keys <= 0) {
                    return;
                }
                int amount = Math.min(keys, 100);
                if (p.isSneaking()) {
                    amount = keys;
                }
                if (amount > 5000) {
                    amount = 5000;
                }
                final int amountL = amount;
                final boolean sendRewardMessage = PlayerData.getSetting(player, "crate_message");

                // Exchanger pet
                int petSlot = PetManager.getPet(player, PetType.EXCHANGER);
                ItemStack ePetItem = (petSlot == -1) ? null : player.getInventory().getItem(petSlot);
                ExchangerPet ePet = ExchangerPet.get(ePetItem);

                List<Reward> applyLater = new ArrayList<>();
                Synchronizer.desynchronize(() -> {
                    int totalreal = 0;
                    List<ItemReward> autoExchange = new ArrayList<>();
                    List<ItemReward> exchangePet = new ArrayList<>();
                    for (int i = 0; i < amountL; i++) {
                        Reward reward = crate.getReward();
                        totalreal = i + 1;
                        if (ePet != null && ePet.shouldModify(reward)) {
                            exchangePet.add((ItemReward) reward);
                            continue;
                        }
                        if (Exchanger.shouldAutoExchange(reward, player)) {
                            autoExchange.add((ItemReward) reward);
                            continue;
                        }
                        if (reward == null) {
                            continue;
                        }
                        if (!reward.canApply(player)) {
                            player.sendMessage("§cYou do not have enough space in your inventory.");
                            totalreal = i;
                            break;
                        }
                        applyLater.add(reward);
                    }

                    // Auto Exchanger
                    Exchanger.AutoExchangerResult res = Exchanger.autoExchange(autoExchange);
                    if (res.itemcount > 0) {
                        Economy.tokens.addBalance(player, res.tokens);
                    }

                    ItemStack item2 = KeyVaultManager.removeKeys(item, type, totalreal);

                    int finalTotalreal = totalreal;
                    Synchronizer.synchronize(() -> {
                        // Exchanger pet
                        if (!exchangePet.isEmpty()) {
                            assert ePet != null;
                            ePet.run(player, exchangePet);
                        }

                        PetXp.addXp(player, PetXp.XpType.CRATES_OPENED, finalTotalreal);

                        player.getInventory().setItem(slot, item2);
                        for (Reward reward : applyLater) {
                            if (reward instanceof TokenReward) {
                                reward.applyReward(player, sendRewardMessage ? (name, am) -> crate.displayName
                                        + " Crate » §7You won §e" + am + " Tokens§7!" : null);
                            } else {
                                reward.applyReward(player, sendRewardMessage ? (name, am) -> crate.displayName
                                        + " Crate » §7You won " + am + "x §e" + name + "§7!" : null);
                            }
                        }
                    });
                });
            }
        }
    }
}