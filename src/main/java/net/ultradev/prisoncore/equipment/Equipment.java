/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.equipment;

import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Equipment {
    public static ItemStack setMultiplier(ItemStack item, double multi) {
        ItemStack ret = NBTUtils.setDouble(item, "equipmentMulti", multi);
        ItemMeta meta = ret.getItemMeta();
        List<String> lore = new ArrayList<String>();
        for (Map.Entry<Enchantment, Integer> enchant : meta.getEnchants().entrySet()) {
            lore.add("§7" + getName(enchant.getKey()) + " " + enchant.getValue());
        }
        lore.add("§7§m------------------------");
        lore.add("§aSell Multiplier: §e" + NumberUtils.formatFull(multi) + "x");
        return ret;
    }

    public static double getMultiplier(ItemStack item) {
        if (!NBTUtils.hasTag(item, "equipmentMulti")) {
            return 0.0;
        }
        return Objects.requireNonNull(NBTUtils.getDouble(item, "equipmentMulti"));
    }

    private static String getName(Enchantment enchant) {
        if (enchant.equals(Enchantment.DURABILITY)) {
            return "Unbreaking";
        }
        if (enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            return "Protection";
        }
        if (enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)) {
            return "Blast Protection";
        }
        if (enchant.equals(Enchantment.PROTECTION_PROJECTILE)) {
            return "Projectile Protection";
        }
        if (enchant.equals(Enchantment.PROTECTION_FIRE)) {
            return "Fire Protection";
        }
        return "";
    }

    public static double getMultiplier(Player p) {
        double multi = 0.0;
        for (ItemStack item : p.getInventory().getArmorContents()) {
            if (item == null)
                continue;
            multi += getMultiplier(item);
        }
        return multi;
    }
}
