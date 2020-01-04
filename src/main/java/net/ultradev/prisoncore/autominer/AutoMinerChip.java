/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class AutoMinerChip {
    public static ItemStack generateItem(long seconds) {
        return generateItem(BigInteger.valueOf(seconds));
    }
    public static ItemStack generateItem(BigInteger time) {
        return new ItemFactory(Material.PRISMARINE_SHARD).setName("§dAuto Miner Chip")
                .addEnchantment(Enchantment.DIG_SPEED, 1).hideFlags()
                .setLore(
                        "§7Right click to receive",
                        "§7Auto Miner time!", "§7",
                        "§7Duration: §e" + DateUtils.convertTimeM(time)
                )
                .addNBT("type", "autominerChip")
                .addNBT("time", time.toString())
                .create();
    }

    public static boolean isAutoMinerChip(ItemStack item) {
        return ItemUtils.isType(item, "autominerChip");
    }

    @NotNull
    public static BigInteger getTime(ItemStack item) {
        assert isAutoMinerChip(item);
        return new BigInteger(NBTUtils.getString(item, "time"));
    }
}
