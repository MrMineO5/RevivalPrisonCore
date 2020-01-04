/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui;

import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import net.ultradev.prisoncore.autominer.AutoMinerAI;
import net.ultradev.prisoncore.autominer.AutoMinerRewards;
import net.ultradev.prisoncore.autominer.AutoMinerUpgrade;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.kits.KitManager;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemDust;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemEssence;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.playerdata.StoreRank;
import net.ultradev.prisoncore.shadytrader.MysteriousCrystal;
import net.ultradev.prisoncore.treasurehunt.TreasureHunt;
import net.ultradev.prisoncore.treasurehunt.TreasureHuntKey;
import net.ultradev.prisoncore.utils.ArrayUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.gui.guis.shadytrader.ShadyTraderGUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.HiddenStringUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ItemScriptFunction {
    private static HashMap<String, ItemScriptFunction> functions = new HashMap<>();
    private GUIRunnable run;

    private ItemScriptFunction(GUIRunnable run) {
        this.run = run;
    }

    public static void init() {
        // No-Op
        functions.put("noop", new ItemScriptFunction((player, event, inv, args) -> {
        }));

        // Inventory functions
        functions.put("inv:close", new ItemScriptFunction((player, event, inv, args) -> player.closeInventory()));
        functions.put("inv:open", new ItemScriptFunction((player, event, inv, args) -> {
            String invO = args[0];
            if (args.length > 1) {
                String[] arg = new String[args.length - 1];
                System.arraycopy(args, 1, arg, 0, args.length - 1);
                GUIManager.openGUI(player, invO, arg);
            } else {
                GUIManager.openGUI(player, invO);
            }
        }));

        // Player functions
        functions.put("player:sendmessage", new ItemScriptFunction((player, event, inv, args) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', args[0]))));
        functions.put("player:performcommand", new ItemScriptFunction((player, event, inv, args) -> player.performCommand(args[0])));
        functions.put("player:teleport", new ItemScriptFunction((player, event, inv, args) -> {
            Location loc;
            World world = Bukkit.getWorld(args[0]);
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);
            if (args.length > 4) {
                float yaw = Float.parseFloat(args[4]);
                float pitch = Float.parseFloat(args[5]);
                loc = new Location(world, x, y, z, yaw, pitch);
            } else {
                loc = new Location(world, x, y, z);
            }
            player.teleport(loc);
        }));
    }

    private static void registerFunction(String name, ItemScriptFunction func) {
        functions.put(name, func);
    }

    public static void run(String function, Player player, InventoryClickEvent event, Inventory inv, String... args) {
        functions.get(function).run(player, event, inv, args);
    }

    public static void initExt() {
        ItemScriptFunction.registerFunction("mine:teleport", new ItemScriptFunction((player, event, inv, args) -> {
            if (!MineManager.hasPermission(player, args[0])) {
                return;
            }
            Mine mine = MineManager.getMine(args[0]);
            if (mine != null) {
                player.teleport(mine.getTpPos());
            }
        }));
        ItemScriptFunction.registerFunction("pickaxe:upgrade", new ItemScriptFunction((player, event, inv, args) -> {
            String enchant = args[0];
            int amount = Integer.parseInt(args[1]);
            Enchantment ench = CustomEnchant.getByName(enchant);
            Pickaxe pick = new Pickaxe(player);
            if (pick.getEnchantmentLevel(ench) + amount > EnchantInfo.getMaxLevel(ench, NBTUtils.getInt(PickaxeUtils.getPickaxe(player), "level"))) {
                player.sendMessage("§cEnchantment level too high.");
                return;
            }
            int level = pick.getEnchantmentLevel(ench);
            long price = 0;
            for (int i = 0; i < amount; i++) {
                price += EnchantInfo.getPrice(ench, level + i);
            }
            if (Economy.tokens.hasBalance(player, price)) {
                Economy.tokens.removeBalance(player, price);
                pick.addEnchantmentLevel(ench, amount);
                pick.applyPickaxe();
                player.sendMessage("§7Added " + amount + (amount == 1 ? " level" : " levels") + " of §e" + EnchantInfo.getEnchantName(ench) + "§7.");
            } else {
                player.sendMessage("§cYou need §e" + NumberUtils.formatFull(price - Economy.tokens.getBalance(player).longValue()) + "§c more tokens for that!");
            }
        }));
        ItemScriptFunction.registerFunction("mailbox:take", new ItemScriptFunction((player, event, inv, args) -> {
            if (InvUtils.isFull(player)) {
                player.sendMessage("§cYour inventory is full. Please empty some slots in your inventory.");
                return;
            }
            int i = Integer.parseInt(args[0]);
            ItemStack item = PlayerData.getMailbox(player).get(i);
            InvUtils.giveItem(player, item);
            PlayerData.removeMailbox(player, i);
        }));
        ItemScriptFunction.registerFunction("shadytrader:buy", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            String trade = args[0];
            ShadyTraderGUI.ShadyTrade tra = ShadyTraderGUI.trades.get(trade);
            int count = MysteriousCrystal.countMysteriousCrystals(player.getInventory().getContents());
            if (tra.getPrice() > count) {
                player.sendMessage("§cYou cannot afford that.");
                return;
            }
            ItemStack[] contents = player.getInventory().getContents();
            int req = tra.getPrice();
            for (int i = 0; i < contents.length; i++) {
                if (MysteriousCrystal.isMysteriousCrystal(contents[i])) {
                    if (req <= 0) {
                        break;
                    }
                    ItemStack item = contents[i];
                    int am = item.getAmount();
                    item.setAmount(am - Math.min(req, am));
                    req -= Math.min(req, am);
                    contents[i] = item;
                }
            }
            player.getInventory().setContents(contents);
            InvUtils.giveItemMailbox(player, tra.getItem());
        }));
        ItemScriptFunction.registerFunction("autominer:collect", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            AutoMinerRewards rew = AutoMinerUtils.getModel(player).data.getRewards();
            String[] tt = args[0].split(":");
            if (tt[0].equalsIgnoreCase("tokens")) {
                long tokens = rew.getTokens();
                Economy.tokens.addBalance(player, tokens);
                rew.setTokens(0);
            }
            if (tt[0].equalsIgnoreCase("dust")) {
                long dust = rew.getSocketGemDust();
                Economy.dust.addBalance(player, dust);
                rew.setSocketGemDust(0);
            }
            if (tt[0].equalsIgnoreCase("keys")) {
                String type = tt[1];
                Crate cr = CrateManager.getCrate(type);
                assert cr != null;
                long amount = rew.getKeys().get(type);
                long rest = CrateManager.giveKeysSafe(player, cr, amount);
                rew.getKeys().put(type, rest);
            }
        }));
        ItemScriptFunction.registerFunction("autominer:upgrade", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            AutoMinerAI model = AutoMinerUtils.getModel(player);
            AutoMinerUpgrade upgrade = AutoMinerUpgrade.valueOf(args[0].toUpperCase());
            int level = model.getUpgrade(upgrade);
            if (level >= upgrade.getMaxLevel()) {
                return;
            }
            if (!Economy.tokens.hasBalance(player, upgrade.getPrice(level))) {
                player.sendMessage("§cYou cannot afford that upgrade.");
                return;
            }
            Economy.tokens.removeBalance(player, upgrade.getPrice(level));
            model.addUpgrade(upgrade, 1);
        }));
        ItemScriptFunction.registerFunction("autominer:reskin", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            AutoMinerAI model = AutoMinerUtils.getModel(player);
            AutoMinerUpgrade upgrade = AutoMinerUpgrade.valueOf(args[0].toUpperCase());
            int level = model.getUpgrade(upgrade);
            if (Economy.tokens.getBalance(player).compareTo(BigInteger.valueOf(upgrade.getPrice(level))) >= 0) {
                player.sendMessage("§cYou cannot afford that upgrade.");
                return;
            }
            Economy.tokens.removeBalance(player, upgrade.getPrice(level));
            model.addUpgrade(upgrade, 1);
        }));
        ItemScriptFunction.registerFunction("autominer:collectime", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            AutoMinerAI model = AutoMinerUtils.getModel(player);
            StoreRank rank = StoreRank.valueOf(args[0].toUpperCase());
            if (StoreRank.getRank(player).getMultiplier() < rank.getMultiplier()) {
                player.sendMessage("§cNo permission.");
                return;
            }
            int time = rank.getAutoMinerBonus();
            if (CooldownUtils.isCooldown(player, "autominerTime_" + rank.name())) {
                model.addAutominerTime(time * 1000);
                CooldownUtils.setCooldown(player, "autominerTime_" + rank.name(), 12 * 60 * 60);
                player.sendMessage("§aClaimed " + rank.getPrefix() + "§a auto miner time.");
            } else {
                player.sendMessage("§cPlease wait before collecting that rewards again.");
            }
        }));
        ItemScriptFunction.registerFunction("socketgem:put", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            int slot = Integer.parseInt(args[0]);
            if (PickaxeUtils.getSocketGem(player, slot) != null) {
                return;
            }
            if (event.getCursor() == null || !SocketGem.isSocketGem(event.getCursor())) {
                return;
            }
            SocketGem gem = SocketGem.fromItem(event.getCursor());
            event.setCursor(null);
            assert gem != null;
            player.getInventory().setItem(0, PickaxeUtils.setSocketGem(player, slot, gem));
        }));
        ItemScriptFunction.registerFunction("autominer:summon", new ItemScriptFunction((player, event, inv, args) -> {
            String m = MineManager.getMineAtXZ(player.getLocation());
            if (m == null) {
                return;
            }
            if (m.equalsIgnoreCase("thunt")) {
                return;
            }
            NPC npc = AutoMinerUtils.getModel(player).getNpc();
            if (npc.isSpawned()) {
                npc.despawn();
            } else {
                npc.spawn(player.getLocation());
            }
        }));
        ItemScriptFunction.registerFunction("tinker:craft", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            SocketGemTier tier = SocketGemTier.valueOf(args[0]);
            int count = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (SocketGemDust.isSocketGemDust(item)) {
                    count += item.getAmount();
                }
            }
            int cost = tier.getPrice();
            if (count >= cost) {
                int am = cost;
                ItemStack[] contents = player.getInventory().getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (am <= 0) {
                        break;
                    }
                    if (SocketGemDust.isSocketGemDust(contents[i])) {
                        ItemStack it = contents[i];
                        int useAm = Math.min(it.getAmount(), am);
                        it.setAmount(it.getAmount() - useAm);
                        am -= useAm;
                        contents[i] = it;
                    }
                }
                player.getInventory().setContents(contents);
                InvUtils.giveItemMailbox(player, SocketGemEssence.getItem(tier, 1));
            }
        }));
        ItemScriptFunction.registerFunction("setting:toggle", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            String setting = args[0];
            PlayerData.setSetting(player, setting, !PlayerData.getSetting(player, setting));
        }));
        ItemScriptFunction.registerFunction("thunt:usekey", new ItemScriptFunction((player, event, inv, args) -> {
            if (TreasureHunt.isActive()) {
                player.sendMessage("§cThere is already an active treasure hunt.");
                return;
            }
            boolean hasKey = false;
            ItemStack[] cont = player.getInventory().getContents();
            for (int i = 0; i < cont.length; i++) {
                if (TreasureHuntKey.isTreasureHuntKey(cont[i])) {
                    ItemStack it = cont[i];
                    it.setAmount(it.getAmount() - 1);
                    cont[i] = it;
                    hasKey = true;
                    break;
                }
            }
            if (!hasKey) {
                player.sendMessage("§cYou need a Treasure Hunt Key to do this, you can buy one from the store (/buy)");
                return;
            }
            Bukkit.broadcastMessage("§a» &lA Treasure Hunt Key has been activated by " + player.getName());
            TreasureHunt.start(1800);
        }));
        ItemScriptFunction.registerFunction("tinkerer:remove", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            int i = Integer.parseInt(args[0]);
            String str = inv.getName();
            if (!HiddenStringUtils.hasHiddenString(str)) {
                return;
            }
            String[] data = HiddenStringUtils.extractHiddenString(str).split(":");
            if (data[0].equals("tinker_dismantle")) {
                if (data.length < i + 1) {
                    return;
                }
                ArrayList<String> strs = new ArrayList<>(Arrays.asList(data));
                SocketGem gem = SocketGem.deserialize(strs.get(i + 1));
                InvUtils.giveItemMailbox(player, gem.getItem());
                if (strs.size() > 1) {
                    strs.remove(i + 1);
                    String[] newStrs = new String[strs.size()];
                    ArrayUtils.toArray(newStrs, strs);
                    GUIManager.openGUI(player, "tinker_dismantle", newStrs);
                } else {
                    GUIManager.openGUI(player, "tinker_dismantle");
                }
            }
        }));
        ItemScriptFunction.registerFunction("tinkerer:dismantle", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            long i = Long.parseLong(args[0]);
            String str = inv.getName();
            if (!HiddenStringUtils.hasHiddenString(str)) {
                return;
            }
            String[] data = HiddenStringUtils.extractHiddenString(str).split(":");
            if (data[0].equals("tinker_dismantle")) {
                Economy.tokens.addBalance(player, i);
                GUIManager.openGUI(player, "tinker_dismantle");
            }
        }));
        ItemScriptFunction.registerFunction("pickaxe:repair", new ItemScriptFunction((player, event, inv, args) -> {
            ItemStack pickaxe = PickaxeUtils.getPickaxe(player);
            if (pickaxe == null) {
                return;
            }
            long price = PickaxeUtils.getRepairPrice(pickaxe);
            if (!Economy.tokens.hasBalance(player, price)) {
                player.sendMessage("§cYou do not have enough tokens to repair your pickaxe.");
                return;
            }
            Economy.tokens.removeBalance(player, price);
            pickaxe.setDurability((short) 0);
            player.getInventory().setItem(0, pickaxe);
        }));
        ItemScriptFunction.registerFunction("socketgem:remove", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            int slot = Integer.parseInt(args[0]);
            if (PickaxeUtils.getSocketGem(player, slot) == null) {
                return;
            }
            player.getInventory().setItem(0, PickaxeUtils.removeSocketGem(player, slot));
        }));
        ItemScriptFunction.registerFunction("keyvault:withdraw", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 2;
            String type = args[0];
            int am = Integer.parseInt(args[1]);
            ItemStack kv = player.getInventory().getItemInMainHand();
            if (!KeyVaultManager.isKeyvault(kv)) {
                player.sendMessage("§cError: Please do not modify your inventory while withdrawing keys.");
                return;
            }
            int cont = KeyVaultManager.getKeys(kv, type);
            if (cont < am) {
                player.sendMessage("§cNot enough keys.");
                return;
            }
            player.getInventory().setItemInMainHand(KeyVaultManager.removeKeys(kv, type, am));
            InvUtils.giveItem(player, Objects.requireNonNull(CrateManager.getCrate(type)).getKey(am));
            player.sendMessage("§7Withdrew §e" + am + "§7 keys from your keyvault!");
        }));
        ItemScriptFunction.registerFunction("keyvault:insert", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            String type = args[0];
            ItemStack kv = player.getInventory().getItemInMainHand();
            if (!KeyVaultManager.isKeyvault(kv)) {
                player.sendMessage("§cError: Please do not modify your inventory while withdrawing keys.");
                return;
            }
            int total = 0;
            int max = KeyVaultManager.getRemainingCapacity(kv, type);
            ItemStack[] contents = player.getInventory().getContents();
            for (int i = 0; i < contents.length; i ++) {
                ItemStack item = contents[i];
                Crate cr = CrateManager.getType(item);
                if (cr == null || !cr.name.equalsIgnoreCase(type)) {
                    continue;
                }
                int am = item.getAmount();
                if (total + am < max) {
                    total += am;
                    contents[i] = null;
                    continue;
                }
                int dif = max - total;
                ItemStack item2 = item.clone();
                item2.setAmount(am - dif);
                total += dif;
                contents[i] = item2;
            }
            player.getInventory().setContents(contents);
            player.getInventory().setItemInMainHand(KeyVaultManager.addKeys(kv, type, total));
            player.sendMessage("§7Added §e" + NumberUtils.formatFull(total) + "§7 keys to your keyvault!");
        }));
        ItemScriptFunction.registerFunction("kit:use", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            String kit = args[0];
            KitManager.useKit(player, kit);
        }));
        registerFunction("pet:set", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 2;
            String setting2 = args[0];
            int val = Integer.parseInt(args[1]);
            ItemStack pet = player.getInventory().getItemInMainHand();
            ItemStack pet2 = PetManager.setSetting(pet, setting2, val);
            player.getInventory().setItemInMainHand(pet2);
        }));
        ItemScriptFunction.registerFunction("dustbank:withdraw", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 1;
            int am = Integer.parseInt(args[0]);
            BigInteger has = Economy.dust.getBalance(player);
            if (has.compareTo(BigInteger.valueOf(am)) < 0) {
                player.sendMessage("§cNot enough dust.");
                return;
            }
            Economy.dust.removeBalance(player, am);
            InvUtils.giveItem(player, SocketGemDust.getSocketGemDust(am));
            player.sendMessage("§7Withdrew §e" + am + "§7 dust from your dust bank!");
        }));
        ItemScriptFunction.registerFunction("dustbank:insert", new ItemScriptFunction((player, event, inv, args) -> {
            assert args.length == 0;
            int total = 0;
            ItemStack[] contents = player.getInventory().getContents();
            for (int i = 0; i < contents.length; i ++) {
                ItemStack item = contents[i];
                if (!SocketGemDust.isSocketGemDust(item)) {
                    continue;
                }
                int am = item.getAmount();
                total += am;
                contents[i] = null;
            }
            player.getInventory().setContents(contents);
            Economy.dust.addBalance(player, total);
            player.sendMessage("§7Added §e" + NumberUtils.formatFull(total) + "§7 dust to your dust bank!");
        }));
    }

    public void run(Player player, InventoryClickEvent event, Inventory inv, String... args) {
        run.run(player, event, inv, args);
    }
}
