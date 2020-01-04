/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.commands.BuildCmd;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.pickaxe.LuckyBlock;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.selling.Selling;
import net.ultradev.prisoncore.treasurehunt.LootGenerator;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PickaxeEvents implements Listener {
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }
        Inventory inv = player.getInventory();
        ItemStack item = inv.getItem(0);
        if (item == null) {
            player.getInventory().setItem(0, PickaxeUtils.createDefaultPickaxe());
            return;
        }
        if (!Objects.equals(NBTUtils.getString(item, "type"), "pickaxe")) {
            player.getInventory().setItem(0, PickaxeUtils.createDefaultPickaxe());
        } else {
            player.getInventory().setItem(0, PickaxeUtils.updatePickaxe(player.getInventory().getItem(0)));
        }
    }

    @EventHandler
    public void onInvClick(@NotNull InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            event.setCancelled(true);
        }
        if (PickaxeUtils.isPickaxe(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwapHand(@NotNull PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(@NotNull PlayerDropItemEvent event) {
        if (PickaxeUtils.isPickaxe(event.getItemDrop().getItemStack())) {
            ItemStack item = event.getItemDrop().getItemStack().clone();
            event.getItemDrop().setItemStack(null);
            event.getPlayer().getInventory().setItem(0, item);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("ultraprison.build")) {
            return;
        }
        if (!MineManager.isInMine(event.getBlock())) {
            if (!event.isCancelled()) {
                if (!BuildCmd.buildOn.contains(player.getUniqueId())) {
                    if (CooldownUtils.isCooldown(player, "message_build")) {
                        player.sendMessage("§7Your build mode is disabled, enable it using /build");
                        CooldownUtils.setCooldown(player, "message_build", 5);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onMine(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        Mine mine = MineManager.getMineAt(event.getBlock().getLocation());
        if (mine == null) {
            if (!event.isCancelled()) {
                if (player.hasPermission("ultraprison.build")) {
                    if (!BuildCmd.buildOn.contains(player.getUniqueId())) {
                        if (CooldownUtils.isCooldown(player, "message_build")) {
                            player.sendMessage("§7Your build mode is disabled, enable it using /build");
                            CooldownUtils.setCooldown(player, "message_build", 5);
                        }
                        event.setCancelled(true);
                    }
                }
            }
            return;
        }
        ItemStack pickaxe = player.getInventory().getItemInMainHand();
        if (!PickaxeUtils.isPickaxe(pickaxe)) {
            if (event.getBlock().getType().equals(Material.SPONGE)) {
                event.setDropItems(false);
                event.setExpToDrop(0);
                LuckyBlock.runLuckBlockReward(event.getPlayer(), 0, PickaxeUtils.getPercent(player, SocketGemType.FAVORED));
                return;
            }
            return;
        }
        if (player.getInventory().getItemInMainHand().getDurability() >= 1560) {
            player.getInventory().getItemInMainHand().setDurability((short) 0);
            player.sendMessage("§cYour pickaxe broke.");
            return;
        }
        if (player.getInventory().getItemInMainHand().getDurability() >= 1540) {
            player.sendTitle(
                    "§cYour pickaxe is about to break!",
                    "§eRight click to repair it",
                    0, 10, 0
            );
            return;
        }
        event.setDropItems(false);
        event.setExpToDrop(0);
        if (PickaxeUtils.breakBlock(player, event.getBlock(), true)) {
            event.setCancelled(true);
            return;
        }
        mine.blocksBroken(1);
        if (mine.getName().equalsIgnoreCase("thunt")) {
            if (MathUtils.isRandom(0.45, 100.0)) {
                event.getBlock().setType(Material.CHEST);
                Chest chest = (Chest) event.getBlock().getState();
                chest.getInventory().setContents(LootGenerator.generateLootChest());
                event.setCancelled(true);
                player.sendMessage("§aYou found a Treasure Chest!");
            }
        }
    }

    /*
        @EventHandler
        public void onChangeGamemode(PlayerGameModeChangeEvent event) {
            if (!event.getNewGameMode().equals(GameMode.CREATIVE)) {
                Player player = event.getPlayer();
                ItemStack[] cont = player.getInventory().getContents();
                for (int i = 1; i < cont.length; i++) {
                    if (PickaxeUtils.isPickaxe(cont[i])) {
                        cont[0] = cont[i];
                        cont[i] = null;
                    }
                }
                player.getInventory().setContents(cont);
            }
        }
     */
    @EventHandler
    public void onClick(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.SURVIVAL) && !player.getGameMode().equals(GameMode.ADVENTURE)) {
            return;
        }
        if (!PickaxeUtils.isPickaxe(player.getInventory().getItemInMainHand())) {
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (player.isSneaking()) {
                Selling.sellall(player);
                return;
            }
        }
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.CHEST)) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            GUIManager.openGUI(player, "pickaxe");
        }
    }

    private static Map<Enchantment, PotionEffectType> potEnchants = new HashMap<>();
    static {
        potEnchants.put(CustomEnchant.NIGHTVISION, PotionEffectType.NIGHT_VISION);
        potEnchants.put(CustomEnchant.JUMPBOOST, PotionEffectType.JUMP);
        potEnchants.put(CustomEnchant.SPEED, PotionEffectType.SPEED);
        potEnchants.put(CustomEnchant.HASTE, PotionEffectType.FAST_DIGGING);
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if (e.getNewSlot() == 0) {
            Pickaxe pick = new Pickaxe(p);
            potEnchants.forEach((ench, pot) -> {
                if (pick.hasEnchantment(ench)) {
                    if (p.hasPotionEffect(pot)) {
                        return;
                    }
                    p.addPotionEffect(new PotionEffect(pot, 100000, pick.getEnchantmentLevel(ench) - 1, false, false));
                }
            });
        } else {
            potEnchants.values().stream()
                    .filter(p::hasPotionEffect)
                    .forEach(p::removePotionEffect);
        }
    }

    @EventHandler
    public void onGameMode(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            PlayerData.setPickaxe(player, player.getInventory().getItem(0));
            player.getInventory().setItem(0, null);
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            BuildCmd.buildOn.remove(player.getUniqueId());
            ItemStack firstSlot = player.getInventory().getItem(0);
            ItemStack pickaxe = PlayerData.getPickaxe(player);
            player.getInventory().setItem(0, pickaxe);
            InvUtils.giveItemMailbox(player, firstSlot);
        }
    }
}
