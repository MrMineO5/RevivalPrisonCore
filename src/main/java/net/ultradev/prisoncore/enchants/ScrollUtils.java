/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants;

import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ScrollUtils {
    public static ItemStack createScroll(Enchantment e) {
        String name = EnchantInfo.getEnchantName(e);
        return new ItemFactory(Material.ENCHANTED_BOOK).setName(name + " §7Scroll")
                .setLore(
                        "§7Right click to add one level",
                        "§7of " + name + "§7 to your pickaxe!"
                )
                .addNBT("type", "scroll")
                .addNBT("enchant", e.getName()).create();
    }

    public static boolean isScroll(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return ItemUtils.isType(item, "scroll");
    }

    public static void applyScroll(@NotNull Player player) {
        Debugger.log("Checking for scroll...", "apply_scroll");
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isScroll(item)) {
            return;
        }
        Debugger.log("Item is scroll", "apply_scroll");
        Enchantment enchant = CustomEnchant.getByName(Objects.requireNonNull(NBTUtils.getString(item, "enchant")));
        Pickaxe pick = new Pickaxe(player);
        if (pick.getEnchantmentLevel(enchant) >= EnchantInfo.getMaxScrollLevel(enchant)) {
            player.sendMessage("§7Enchantment is already at maximum scroll level.");
            return;
        }
        pick.addEnchantmentLevel(enchant, 1);
        pick.applyPickaxe();
        player.getInventory().setItemInMainHand(null);
        player.sendMessage("§7A level of " + EnchantInfo.getEnchantName(enchant) + "§7 has been added to your pickaxe.");
    }
}
