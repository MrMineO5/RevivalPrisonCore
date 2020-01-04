/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pets;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class PetManager {
    public static ItemStack createPet(@NotNull PetType type) {
        ItemStack item = (new ItemFactory(type.getMaterial())).setName(type.defaultName()).addEnchantment(Enchantment.DIG_SPEED).hideFlags().addNBT("type", "pet").addNBT("uuid", UUID.randomUUID().toString()).addNBT("petType", type.name()).addNBT("level", 1).addNBT("xp", 0).create();
        return update(item);
    }


    public static boolean isPet(ItemStack item) {
        return ItemUtils.isType(item, "pet");
    }


    @NotNull
    public static PetType getType(ItemStack item) {
        assert isPet(item);
        return PetType.valueOf(NBTUtils.getString(item, "petType"));
    }


    public static int getLevel(ItemStack item) {
        return NBTUtils.getInt(item, "level");
    }


    public static int getXp(ItemStack item) {
        return NBTUtils.getInt(item, "xp");
    }


    @Contract("_, _ -> param1")
    @NotNull
    public static ItemStack setXp(ItemStack item, int xp) {
        return update(NBTUtils.setInt(item, "xp", xp));
    }


    @NotNull
    public static ItemStack addXp(ItemStack item, int xp) {
        return setXp(item, getXp(item) + xp);
    }


    @Contract("_ -> param1")
    public static ItemStack update(ItemStack item) {
        if (!isPet(item)) {
            return item;
        }
        PetType type = getType(item);
        int level = getLevel(item);
        int xp = getXp(item);
        int req = type.getRequiredXp(level);
        while (xp > req && level < type.getMaxLevel()) {
            xp -= req;
            level++;
            req = type.getRequiredXp(level);
        }
        item = NBTUtils.setInt(item, "level", level);
        item = NBTUtils.setInt(item, "xp", xp);
        List<String> lore = new ArrayList<>();
        lore.add("§7Level §e" + level);
        if (level < type.getMaxLevel()) {
            lore.add(TextUtils.generateBar('e', '7', '|', 40, xp, req) + " §7" + NumberUtils.formatFull(xp) + "/" + NumberUtils.formatFull(req));
        } else {
            lore.add("§aMaxed!");
        }
        lore.add("§7");
        lore.addAll(type.getDescription());
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static int getLevel(@NotNull Player player, PetType type) {
        int i = getPet(player, type);
        if (i == -1) {
            return 0;
        }
        return getLevel(player.getInventory().getItem(i));
    }

    public static int getPet(@NotNull Player player, PetType type) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (isPet(item) && getType(item).equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean hasPet(Player player, PetType type) {
        return (getPet(player, type) != -1);
    }


    public static Map<PetType, Integer> getPets(Player player) {
        Map<PetType, Integer> pets = new HashMap<>();
        for (PetType value : PetType.values()) {
            int i = getPet(player, value);
            if (i != -1) {


                ItemStack item = player.getInventory().getItem(i);
                if (isPet(item)) {

                    pets.put(value, getLevel(item));
                }
            }
        }
        return pets;
    }

    public static int getSetting(ItemStack item, String setting) {
        if (!NBTUtils.hasTag(item, "setting_" + setting.toLowerCase())) {
            return 0;
        }
        return NBTUtils.getInt(item, "setting_" + setting.toLowerCase());
    }

    @NotNull
    public static ItemStack setSetting(ItemStack pet, String setting, int val) {
        return NBTUtils.setInt(pet, "setting_" + setting.toLowerCase(), val);
    }
}
