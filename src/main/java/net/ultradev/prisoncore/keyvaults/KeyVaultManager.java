/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.keyvaults;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class KeyVaultManager {
    public static ItemStack generateKeyVault(KeyVaultType type) {
        ItemStack item = new ItemFactory(Material.DISPENSER)
                .setName("§e" + type.getName() + " Key Vault")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)
                .hideFlags()
                .addNBT("type", KeyVaults.v2Name)
                .addNBT("kvtype", type.name())
                .addNBT("kvdata", "")
                .addNBT("kvuuid", UUID.randomUUID().toString())
                .create();
        return updateLore(item);
    }

    public static boolean isKeyvault(ItemStack item) {
        return ItemUtils.isType(item, KeyVaults.v2Name);
    }

    public static KeyVaultType getType(ItemStack item) {
        assert isKeyvault(item);
        return KeyVaultType.valueOf(NBTUtils.getString(item, "kvtype"));
    }

    public static ItemStack updateLore(ItemStack item) {
        if (!isKeyvault(item)) {
            return item;
        }
        KeyVaultType type = KeyVaultType.valueOf(NBTUtils.getString(item, "kvtype"));
        Map<String, Integer> caps = type.getKeyCapacities();
        Map<String, Integer> keys = KeyVaults.deserialize(Objects.requireNonNull(NBTUtils.getString(item, "kvdata")));
        if (!keys.containsKey("mine"))
            keys.put("mine", 0);
        if (!keys.containsKey("rare"))
            keys.put("rare", 0);
        if (!keys.containsKey("legendary"))
            keys.put("legendary", 0);
        List<String> lore = new ArrayList<>();
        lore.add("§7Crate Keys found get");
        lore.add("§7stored inside this vault!");
        lore.add("§7");
        lore.add("§bMine Keys§7: §f" + NumberUtils.formatFull(keys.get("mine")) + "§7/" + NumberUtils.formatFull(caps.get("mine")));
        lore.add("§dRare Keys§7: §f" + NumberUtils.formatFull(keys.get("rare")) + "§7/" + NumberUtils.formatFull(caps.get("rare")));
        lore.add("§cLegendary Keys§7: §f" + NumberUtils.formatFull(keys.get("legendary")) + "§7/" + NumberUtils.formatFull(caps.get("legendary")));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return NBTUtils.setString(item, "kvdata", KeyVaults.serialize(keys));
    }

    public static int getKeys(ItemStack item, String key) {
        if (!isKeyvault(item)) {
            return -1;
        }
        return KeyVaults.deserialize(Objects.requireNonNull(NBTUtils.getString(item, "kvdata"))).get(key.toLowerCase());
    }

    @NotNull
    private static ItemStack setKeys(ItemStack item, @NotNull String key, int amount) {
        Map<String, Integer> keys = KeyVaults.deserialize(Objects.requireNonNull(NBTUtils.getString(item, "kvdata")));
        keys.put(key.toLowerCase(), amount);
        return updateLore(NBTUtils.setString(item, "kvdata", KeyVaults.serialize(keys)));
    }

    @NotNull
    public static ItemStack addKeys(ItemStack item, String key, int amount) {
        return setKeys(item, key, getKeys(item, key) + amount);
    }

    @NotNull
    public static ItemStack removeKeys(ItemStack item, String key, int amount) {
        return setKeys(item, key, getKeys(item, key) - amount);
    }

    public static int getCapacity(ItemStack item, String crate) {
        assert isKeyvault(item);
        KeyVaultType type = KeyVaultType.valueOf(NBTUtils.getString(item, "kvtype").toUpperCase());
        return type.getKeyCapacities().get(crate);
    }

    public static int getRemainingCapacity(ItemStack item, String crate) {
        return getCapacity(item, crate) - getKeys(item, crate);
    }

    public static int addRemaining(ItemStack item, String crate, int amount) {
        int cap = getRemainingCapacity(item, crate);
        addKeys(item, crate, Math.min(cap, amount));
        return amount - Math.min(cap, amount);
    }

    public static boolean isMergeMode(ItemStack item) {
        if (!NBTUtils.hasTag(item, "mergemode")) {
            return false;
        }
        return NBTUtils.getInt(item, "mergemode") == 1;
    }
    public static ItemStack setMergeMode(ItemStack item, boolean mode) {
        return NBTUtils.setInt(item, "mergemode", mode ? 1 : 0);
    }
}
