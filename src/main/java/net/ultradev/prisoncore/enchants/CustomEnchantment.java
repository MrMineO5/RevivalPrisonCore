/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantment extends Enchantment {
    private String name;

    public CustomEnchantment(String name, int id) {
        super(id);
        this.name = name;
    }

    @Override
    public boolean canEnchantItem(ItemStack arg0) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public int getMaxLevel() {
        return 65535;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
