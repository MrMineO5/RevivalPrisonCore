/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {
    private static ArrayList<Material> pickaxes = new ArrayList<>();

    static {
        pickaxes.add(Material.WOOD_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.GOLD_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);
    }

    public static ItemStack addToLore(ItemStack item, String string) {
        ItemStack newi = item.clone();
        ItemMeta meta = newi.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(string);
        meta.setLore(lore);
        newi.setItemMeta(meta);
        return newi;
    }

    @Deprecated
    public static ItemStack clearLore(ItemStack item) {
        ItemStack newI = item.clone();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.clear();
        meta.setLore(lore);
        newI.setItemMeta(meta);
        return newI;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        ItemStack newI = item.clone();
        ItemMeta meta = newI.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        newI.setItemMeta(meta);
        return newI;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemStack newI = item.clone();
        ItemMeta meta = newI.getItemMeta();
        meta.setLore(lore);
        newI.setItemMeta(meta);
        return newI;
    }

    public static String getBlockName(Material mat) {
        if (mat.equals(Material.SPONGE)) {
            return "§eLucky Block";
        }
        if (mat.equals(Material.ENDER_STONE)) {
            return "§cSuper Lucky Block";
        }
        ItemStack item = new ItemStack(mat);
        net.minecraft.server.v1_12_R1.ItemStack it = NBTUtils
                .convertToNMS(item);
        return it.getName();
    }

    public static boolean isType(ItemStack item, String type) {
        return (NBTUtils.hasTag(item, "type") && NBTUtils.getString(item, "type").equals(type));
    }

    @NotNull
    public static ItemStack setType(ItemStack item, String type) {
        return NBTUtils.setString(item, "type", type);
    }

    public static List<ItemStack> split(ItemStack item, int count) {
        int dc = count;
        List<ItemStack> ret = new ArrayList<>();

        // Get stack size
        int stacksize = item.getMaxStackSize();

        // Create stack
        ItemStack stack = item.clone();
        stack.setAmount(stacksize);

        // Add stack until insufficient items left
        while (dc > stacksize) {
            ret.add(stack);
            dc -= stacksize;
        }

        // Add remaining items
        if (dc > 0) {
            ItemStack remain = item.clone();
            remain.setAmount(dc);
            ret.add(remain);
        }

        return ret;
    }
}
