/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.keyvaults;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum KeyVaultType {
    SMALL(0), MEDIUM(1), LARGE(2), BIG(3), HUGE(4), GIGANTIC(5), HUMONGOUS(6), COLOSSAL(7);

    @Getter
    private int id;

    KeyVaultType(int id) {
        this.id = id;
    }

    public static KeyVaultType byId(int i) {
        for (KeyVaultType value : KeyVaultType.values()) {
            if (value.getId() == i) {
                return value;
            }
        }
        return null;
    }

    @NotNull
    public Map<String, Integer> getKeyCapacities() {
        Map<String, Integer> caps = new HashMap<>();
        switch (this) {
            case SMALL:
                caps.put("mine", 250);
                caps.put("rare", 150);
                caps.put("legendary", 75);
                break;
            case MEDIUM:
                caps.put("mine", 1000);
                caps.put("rare", 750);
                caps.put("legendary", 500);
                break;
            case LARGE:
                caps.put("mine", 7500);
                caps.put("rare", 5000);
                caps.put("legendary", 2500);
                break;
            case BIG:
                caps.put("mine", 50000);
                caps.put("rare", 75000);
                caps.put("legendary", 10000);
                break;
            case HUGE:
                caps.put("mine", 200000);
                caps.put("rare", 250000);
                caps.put("legendary", 75000);
                break;
            case GIGANTIC:
                caps.put("mine", 750000);
                caps.put("rare", 1000000);
                caps.put("legendary", 500000);
                break;
            case HUMONGOUS:
                caps.put("mine", 1000000);
                caps.put("rare", 2500000);
                caps.put("legendary", 1000000);
                break;
            case COLOSSAL:
                caps.put("mine", 5000000);
                caps.put("rare", 7500000);
                caps.put("legendary", 2500000);
                break;
            default:
                break;
        }
        return caps;
    }

    @NotNull
    @Contract(pure = true)
    public String getName() {
        switch (this) {
            case SMALL:
                return "Small";
            case MEDIUM:
                return "Medium";
            case LARGE:
                return "Large";
            case HUGE:
                return "Huge";
            case GIGANTIC:
                return "Gigantic";
            case BIG:
                return "Big";
            case COLOSSAL:
                return "Colossal";
            case HUMONGOUS:
                return "Humongous";
        }
        return "Error";
    }

    @Nullable
    @Contract(pure = true)
    public KeyVaultType getNextTier() {
        return byId(getId() + 1);
    }
}
