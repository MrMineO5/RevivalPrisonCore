/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.crates;

import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;


public class Crate {
    public LinkedHashMap<Reward, Double> drops;
    public String name;
    public String displayName;
    public String holoName;
    public String[] lore;
    public String holoLore;
    public boolean enderChest;
    public Material keyMat = Material.TRIPWIRE_HOOK;
    public boolean keyVaultSupported;

    public Crate(String name, String displayName, String holoName, String[] lore, String holoLore, boolean enderChest, boolean keyVaultSupported) {
        this.name = name.toLowerCase();
        this.displayName = displayName;
        this.holoName = holoName;
        this.lore = lore;
        this.holoLore = holoLore;
        this.enderChest = enderChest;
        this.drops = CrateManager.loadRewards(new File(Main.getInstance().getDataFolder(), "rewards/" + name + ".txt"));
        this.keyVaultSupported = keyVaultSupported;
    }


    @NotNull
    public ItemStack getKey(int amount) {
        return (new ItemFactory(this.keyMat, amount))
                .setName(this.displayName + " Crate Key")
                .setLore(this.lore)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .hideFlags()
                .addNBT("type", "cratekey")
                .addNBT("crate", this.name)
                .create();
    }


    public ItemStack getAdminCrate() {
        Material mat = this.enderChest ? Material.ENDER_CHEST : Material.CHEST;
        return (new ItemFactory(mat))
                .setName(this.displayName + " Crate")
                .addNBT("type", "admincrate")
                .addNBT("crate", this.name)
                .create();
    }

    public Reward getReward() {
        double total = this.drops.values().stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = MathUtils.random(0.0D, total);
        double curint = 0.0D;
        for (Map.Entry<Reward, Double> set : this.drops.entrySet()) {
            curint += set.getValue();
            if (randomValue < curint) {
                return set.getKey();
            }
        }
        return null;
    }
}
