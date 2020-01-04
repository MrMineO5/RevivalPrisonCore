/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.keyvaults;

import net.ultradev.prisoncore.utils.items.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyVaults {
    public static String v1Name = "keyvault";
    public static String v2Name = "keyvaultV2";

    public static Map<String, Integer> deserialize(@NotNull String str) {
        if (str.equals("")) {
            return new HashMap<>();
        }
        String[] strs = str.split("#\\|\\|#");
        return Arrays.stream(strs)
                .collect(Collectors.toMap(
                        (st) -> st.split("#")[0],
                        (st -> Integer.parseInt(st.split("#")[1]))
                ));
    }

    public static String serialize(@NotNull Map<String, Integer> map) {
        return map.entrySet().stream()
                .map((entr) -> entr.getKey() + "#" + entr.getValue())
                .collect(Collectors.joining("#||#"));
    }

    public static void convertKeyVaults(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (!KeyVaultManager.isKeyvault(contents[i]))
                continue;
            contents[i] = convert(contents[i]);
        }
        player.getInventory().setContents(contents);
    }

    private static ItemStack convert(ItemStack keyvault) {
        if (!ItemUtils.isType(keyvault, v1Name)) {
            return keyvault;
        }
        KeyVaultType type = map(KeyVaultManager.getType(keyvault));
        return KeyVaultManager.generateKeyVault(type);
    }

    private static KeyVaultType map(KeyVaultType type) {
        switch (type) {
            case SMALL:
                return KeyVaultType.SMALL;
            case MEDIUM:
                return KeyVaultType.MEDIUM;
            case LARGE:
                return KeyVaultType.LARGE;
            case HUGE:
                return KeyVaultType.BIG;
            case GIGANTIC:
                return KeyVaultType.HUGE;
            case BIG:
                return KeyVaultType.GIGANTIC;
        }
        return KeyVaultType.SMALL;
    }
}
