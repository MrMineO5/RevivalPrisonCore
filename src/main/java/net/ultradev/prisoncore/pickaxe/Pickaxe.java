/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe;

import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Pickaxe {
    private ItemStack item;
    private Player player;

    public Pickaxe(Player player) {
        this.item = PickaxeUtils.getPickaxe(player);
        this.player = player;
    }

    public boolean hasEnchantment(Enchantment enchant) {
        if (item == null) {
            return false;
        }
        return item.getItemMeta().hasEnchant(enchant);
    }

    public boolean hasEnchantment(String enchant) {
        return hasEnchantment(CustomEnchant.getByName(enchant));
    }

    public int getEnchantmentLevel(Enchantment enchant) {
        if (!hasEnchantment(enchant)) {
            return 0;
        }
        return item.getItemMeta().getEnchantLevel(enchant);
    }

    public int getEnchantmentLevel(String enchant) {
        return getEnchantmentLevel(CustomEnchant.getByName(enchant));
    }

    public void setEnchantmentLevel(Enchantment enchant, int level) {
        Debugger.log("Applying enchantment " + enchant.getName() + " with level " + level, "enchant_set");
        if (Arrays.asList(Enchantment.values()).contains(enchant)) {
            Debugger.log("Enchantment is correctly registered.", "enchant_set");
        } else {
            Debugger.log("Enchantment is incorrectly registered, please refer to the list of enchantments below", "enchant_set");
            Debugger.log(String.join(", ", Arrays.asList(Enchantment.values()).stream().map((ench) -> ench.getName())
                    .collect(Collectors.toList())), "enchant_set");
            return;
        }
        if (level <= 0) {
            Debugger.log("Level is 0, removing enchantment...", "enchant_set");
            removeEnchantment(enchant);
        } else {
            if (hasEnchantment(enchant)) {
                Debugger.log("Enchantment already on pickaxe, removing...", "enchant_set");
                removeEnchantment(enchant);
            }
            Debugger.log("Applying enchant...", "enchant_set");
            item.addUnsafeEnchantment(enchant, level);
        }
        Debugger.log("Done", "enchant_set");
    }

    public void setEnchantmentLevel(String enchant, int level) {
        setEnchantmentLevel(CustomEnchant.getByName(enchant), level);
    }

    public void addEnchantmentLevel(Enchantment enchant, int addition) {
        setEnchantmentLevel(enchant, getEnchantmentLevel(enchant) + addition);
    }

    public void addEnchantmentLevel(String enchant, int addition) {
        addEnchantmentLevel(CustomEnchant.getByName(enchant), addition);
    }

    public void removeEnchantment(Enchantment enchant) {
        item.removeEnchantment(enchant);
    }

    public void applyPickaxe() {
        Debugger.log("Applying pickaxe...", "pickaxe_update");
        Debugger.log("Updating item info...", "pickaxe_update");
        item = PickaxeUtils.updatePickaxe(item);
        Debugger.log("Setting item in inventory...", "pickaxe_update");
        player.getInventory().setItem(0, item);
        Debugger.log("Updating player inventory...", "pickaxe_update");
        player.updateInventory();
    }
}
