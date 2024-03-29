/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.items;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.ultradev.prisoncore.utils.gui.GUIClickType;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigInteger;
import java.util.*;

public class ItemFactory {
    // ItemStack
    private Material mat;
    private int amount;
    private short damage;

    // ItemMeta
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchants;
    private boolean unbreakable;

    private boolean flagsHidden = true;

    // NBT Tags
    private NBTTagCompound nbt;
    private String[][] itemscript_general;
    private Map<GUIClickType, String[][]> itemscripts;

    // Skull Info
    private OfflinePlayer skullOwner;

    public ItemFactory(ItemStack item) {
        this.mat = item.getType();
        this.amount = item.getAmount();
        this.damage = item.getDurability();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            this.name = meta.hasDisplayName() ? meta.getDisplayName() : null;
            this.lore = meta.hasLore() ? meta.getLore() : null;
            this.enchants = meta.hasEnchants() ? meta.getEnchants() : null;
            this.nbt = NBTUtils.hasNBTCompound(item) ? NBTUtils.getNBTCompound(item) : null;
        }
    }

    public ItemFactory() {
        this(Material.STONE);
    }

    public ItemFactory(Material mat) {
        this(mat, 1);
    }

    public ItemFactory(Material mat, int amount) {
        this(mat, amount, 0);
    }

    public ItemFactory(Material mat, int amount, int damage) {
        this(mat, amount, (short) damage);
    }

    public ItemFactory(Material mat, int amount, short damage) {
        this.mat = mat;
        this.amount = amount;
        this.damage = damage;
        this.lore = new ArrayList<>();
        this.enchants = new HashMap<>();
        this.nbt = new NBTTagCompound();
        this.itemscripts = new HashMap<>();
    }

    public ItemFactory setMaterial(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemFactory setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemFactory setName(String name) {
        this.name = name;
        return this;
    }

    public ItemFactory setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemFactory setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemFactory addEnchantment(Enchantment enchant) {
        return addEnchantment(enchant, 1);
    }

    public ItemFactory addEnchantment(Enchantment enchant, int level) {
        if (enchant != null) {
            this.enchants.put(enchant, level);
        }
        return this;
    }

    public ItemFactory setEnchantments(Map<Enchantment, Integer> ench) {
        this.enchants = ench;
        return this;
    }

    public ItemFactory addNBT(String key, String value) {
        nbt.setString(key, value);
        return this;
    }

    public ItemFactory addNBT(String key, BigInteger value) {
        return addNBT(key, value.toString());
    }

    public ItemFactory addNBT(String key, int value) {
        nbt.setInt(key, value);
        return this;
    }

    public ItemFactory addNBT(String key, double value) {
        nbt.setDouble(key, value);
        return this;
    }

    public ItemFactory addNBT(String key, long value) {
        nbt.setLong(key, value);
        return this;
    }

    public ItemFactory setClickEvent(String[][] script) {
        if (script != null) {
            this.itemscript_general = script;
        }
        return this;
    }

    public ItemFactory setClickEvent(GUIClickType type, String[][] script) {
        if (script != null) {
            this.itemscripts.put(type, script);
        }
        return this;
    }

    public ItemFactory setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemFactory hideFlags() {
        this.flagsHidden = true;
        return this;
    }

    public ItemFactory showFlags() {
        this.flagsHidden = false;
        return this;
    }

    public ItemFactory setSkullOwner(UUID id) {
        return setSkullOwner(Bukkit.getOfflinePlayer(id));
    }

    public ItemFactory setSkullOwner(OfflinePlayer op) {
        this.skullOwner = op;
        return this;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(mat, amount, damage);
        item = NBTUtils.setNBT(item, nbt);
        if (itemscript_general != null) {
            item = GUIUtils.addClickEvent(item, itemscript_general);
        }
        for (Map.Entry<GUIClickType, String[][]> clickEvent : itemscripts.entrySet()) {
            if (clickEvent.getValue() != null) {
                item = GUIUtils.addClickEvent(item, clickEvent.getValue(), clickEvent.getKey());
            }
        }
        item.addUnsafeEnchantments(enchants);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setUnbreakable(unbreakable);
        if (this.flagsHidden) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        item.setItemMeta(meta);
        if (item.getType().equals(Material.SKULL_ITEM)) {
            SkullMeta sm = (SkullMeta) item.getItemMeta();
            sm.setOwningPlayer(this.skullOwner);
            item.setItemMeta(sm);
        }
        return item;
    }
}
