/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Parser {
    @Nullable
    @SuppressWarnings("deprecation")
    public static MaterialData parseMaterialData(@NotNull String str) {
        String[] twopart = str.split(":");
        Material mat;
        try {
            mat = Material.valueOf(twopart[0]);
        } catch (EnumConstantNotPresentException e) {
            return null;
        }
        MaterialData data = new MaterialData(mat);
        if (twopart.length == 2) {
            data.setData(Byte.parseByte(twopart[1]));
        }
        return data;
    }
}
