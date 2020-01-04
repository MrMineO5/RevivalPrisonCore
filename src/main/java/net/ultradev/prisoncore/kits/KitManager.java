/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.kits;

import net.ultradev.prisoncore.commands.beta.RequiredRank;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.time.DateUtils;
import net.ultradev.prisoncore.withdraw.TokenItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class KitManager {
    private static Map<String, Kit> kits = new HashMap<>();

    static {
        List<ItemStack> toolsRewards = new ArrayList<>();
        toolsRewards.add(new ItemFactory(Material.DIAMOND_SPADE)
                .setName("§aStarter Shovel")
                .addEnchantment(Enchantment.DIG_SPEED, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        toolsRewards.add(new ItemFactory(Material.DIAMOND_AXE)
                .setName("§aStarter Axe")
                .addEnchantment(Enchantment.DIG_SPEED, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        kits.put("tools", new Kit("tools", "§aTools", Material.DIAMOND_SPADE, toolsRewards, 24 * 60 * 60));

        List<ItemStack> pvpRewards = new ArrayList<>();
        pvpRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§aPVP Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .showFlags()
                .create());
        pvpRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§aPVP Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .showFlags()
                .create());
        pvpRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§aPVP Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .showFlags()
                .create());
        pvpRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§aPVP Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .showFlags()
                .create());
        pvpRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§aPVP Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        kits.put("pvp", new Kit("pvp", "§aPVP", Material.DIAMOND_SWORD, pvpRewards, 24 * 60 * 60));

        List<ItemStack> ultraRewards = new ArrayList<>();
        ultraRewards.add(TokenItem.getTokenItem(250000, 1));
        ultraRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§c§lUltra§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ultraRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§c§lUltra§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ultraRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§c§lUltra§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ultraRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§c§lUltra§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ultraRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§c§lUltra§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .showFlags()
                .create());
        ultraRewards.add(Objects.requireNonNull(CrateManager.getCrate("legendary")).getKey(10));
        ultraRewards.add(new ItemFactory(Material.GOLDEN_APPLE, 16, 1)
                .setName("§c§lUltra§a Gapple")
                .showFlags()
                .create());
        ultraRewards.add(new ItemFactory(Material.GLASS, 64).create());
        ultraRewards.add(new ItemFactory(Material.GLASS, 64).create());
        ultraRewards.add(new ItemFactory(Material.GLOWSTONE, 64).showFlags().create());
        kits.put("ultra", new Kit("ultra", "§c§lUltra", Material.GLASS, ultraRewards, RequiredRank.ULTRA));

        List<ItemStack> obsidianRewards = new ArrayList<>();
        obsidianRewards.add(TokenItem.getTokenItem(100000, 1));
        obsidianRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§5§lObsidian§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        obsidianRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§5§lObsidian§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        obsidianRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§5§lObsidian§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        obsidianRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§5§lObsidian§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        obsidianRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§5§lObsidian§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.FIRE_ASPECT, 1)
                .showFlags()
                .create());
        obsidianRewards.add(Objects.requireNonNull(CrateManager.getCrate("legendary")).getKey(5));
        obsidianRewards.add(new ItemFactory(Material.GOLDEN_APPLE, 4, 1)
                .setName("§5§lObsidian§a Gapple")
                .showFlags()
                .create());
        obsidianRewards.add(new ItemFactory(Material.GLASS, 64).create());
        obsidianRewards.add(new ItemFactory(Material.GLOWSTONE, 32).create());
        kits.put("obsidian", new Kit("obsidian", "§5§lObsidian", Material.OBSIDIAN, obsidianRewards, RequiredRank.OBSIDIAN));

        List<ItemStack> emeraldRewards = new ArrayList<>();
        emeraldRewards.add(TokenItem.getTokenItem(50000, 1));
        emeraldRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§a§lEmerald§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        emeraldRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§a§lEmerald§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        emeraldRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§a§lEmerald§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        emeraldRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§a§lEmerald§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        emeraldRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§a§lEmerald§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.FIRE_ASPECT, 1)
                .showFlags()
                .create());
        emeraldRewards.add(new ItemFactory(Material.GLASS, 64).create());
        emeraldRewards.add(new ItemFactory(Material.GLOWSTONE, 32).create());
        kits.put("emerald", new Kit("emerald", "§a§lEmerald", Material.EMERALD_BLOCK, emeraldRewards, RequiredRank.EMERALD));

        List<ItemStack> diamondRewards = new ArrayList<>();
        diamondRewards.add(TokenItem.getTokenItem(50000, 1));
        diamondRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§b§lDiamond§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        diamondRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§b§lDiamond§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        diamondRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§b§lDiamond§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        diamondRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§b§lDiamond§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        diamondRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§b§lDiamond§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.FIRE_ASPECT, 1)
                .showFlags()
                .create());
        diamondRewards.add(new ItemFactory(Material.GLASS, 48).create());
        diamondRewards.add(new ItemFactory(Material.GLOWSTONE, 16).create());
        kits.put("diamond", new Kit("diamond", "§b§lDiamond", Material.DIAMOND_BLOCK, diamondRewards, RequiredRank.DIAMOND));

        List<ItemStack> goldRewards = new ArrayList<>();
        goldRewards.add(TokenItem.getTokenItem(25000, 1));
        goldRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§e§lGold§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        goldRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§e§lGold§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        goldRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§e§lGold§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        goldRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§e§lGold§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        goldRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§e§lGold§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        goldRewards.add(new ItemFactory(Material.GLASS, 32).create());
        goldRewards.add(new ItemFactory(Material.GLOWSTONE, 8).create());
        kits.put("gold", new Kit("gold", "§e§lGold", Material.GOLD_BLOCK, goldRewards, RequiredRank.GOLD));

        List<ItemStack> ironRewards = new ArrayList<>();
        ironRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§f§lIron§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ironRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§f§lIron§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ironRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§f§lIron§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ironRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§f§lIron§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ironRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§f§lIron§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        ironRewards.add(new ItemFactory(Material.GLASS, 16).create());
        kits.put("iron", new Kit("iron", "§f§lIron", Material.IRON_BLOCK, ironRewards, RequiredRank.IRON));

        List<ItemStack> coalRewards = new ArrayList<>();
        coalRewards.add(new ItemFactory(Material.DIAMOND_HELMET)
                .setName("§8§lCoal§a Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        coalRewards.add(new ItemFactory(Material.DIAMOND_CHESTPLATE)
                .setName("§8§lCoal§a Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        coalRewards.add(new ItemFactory(Material.DIAMOND_LEGGINGS)
                .setName("§8§lCoal§a Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        coalRewards.add(new ItemFactory(Material.DIAMOND_BOOTS)
                .setName("§8§lCoal§a Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        coalRewards.add(new ItemFactory(Material.DIAMOND_SWORD)
                .setName("§8§lCoal§a Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .showFlags()
                .create());
        coalRewards.add(new ItemFactory(Material.GLASS, 16).create());
        kits.put("coal", new Kit("coal", "§8§lCoal", Material.COAL_BLOCK, coalRewards, RequiredRank.COAL));
    }

    @Nullable
    public static Kit getKit(String name) {
        return kits.getOrDefault(name, null);
    }

    public static void useKit(Player player, String name) {
        Kit k = getKit(name.toLowerCase());
        if (k == null) {
            player.sendMessage("§cThat kit does not exist.");
            return;
        }
        if (!k.hasPermission(player)) {
            player.sendMessage("§7That kit requires " + k.getRank() + "§7 or above.");
            return;
        }
        if (!k.isCooldown(player)) {
            player.sendMessage("§7Please wait " + DateUtils.convertTime(k.getCooldown(player) / 1000L) + " before using that kit again.");
            return;
        }
        k.use(player);
        player.sendMessage("§aUsed kit " + k.getDisplayname());
    }
}
