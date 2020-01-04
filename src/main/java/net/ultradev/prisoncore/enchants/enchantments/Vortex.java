/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Vortex extends Enchantment {

    public Vortex(int id) {
        super(id);
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
        return 300;
    }

    @Override
    public String getName() {
        return "vortex";
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
