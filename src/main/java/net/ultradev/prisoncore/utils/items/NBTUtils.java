/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.items;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NBTUtils {
    @NotNull
    public static net.minecraft.server.v1_12_R1.ItemStack convertToNMS(@NotNull ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.hasTag()) {
            nmsItem.setTag(new NBTTagCompound());
        }
        return nmsItem;
    }

    @NotNull
    private static ItemStack convertToBukkit(net.minecraft.server.v1_12_R1.ItemStack item) {
        return CraftItemStack.asBukkitCopy(item);
    }

    @NotNull
    public static ItemStack setInt(ItemStack item, String key, int value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setInt(key, value);
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setDouble(ItemStack item, String key, double value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setDouble(key, value);
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setLong(ItemStack item, String key, long value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setLong(key, value);
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setString(key, value);
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setBigDecimal(ItemStack item, String key, @NotNull BigDecimal value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setString(key, value.toString());
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setBigInteger(ItemStack item, String key, @NotNull BigInteger value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().setString(key, value.toString());
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static Integer getInt(ItemStack item, String key) throws IllegalArgumentException {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag().getInt(key);
    }

    @NotNull
    public static Double getDouble(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag().getDouble(key);
    }


    @NotNull
    public static Long getLong(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag().getLong(key);
    }

    public static String getString(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag().getString(key);
    }

    @Contract("_, _ -> new")
    @NotNull
    public static BigDecimal getBigDecimal(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return new BigDecimal(nmsItem.getTag().getString(key));
    }

    @Contract("_, _ -> new")
    @NotNull
    public static BigInteger getBigInteger(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return new BigInteger(nmsItem.getTag().getString(key));
    }

    public static boolean hasTag(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag().hasKey(key);
    }

    @NotNull
    public static ItemStack remove(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        nmsItem.getTag().remove(key);
        return convertToBukkit(nmsItem);
    }

    @NotNull
    public static ItemStack setNBT(ItemStack item, NBTTagCompound nbts) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        nmsItem.setTag(nbts);
        return convertToBukkit(nmsItem);
    }

    public static boolean hasNBTCompound(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        return nmsItem.hasTag();
    }

    @NotNull
    public static NBTTagCompound getNBTCompound(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = convertToNMS(item);
        assert nmsItem.getTag() != null;
        return nmsItem.getTag();
    }
}
