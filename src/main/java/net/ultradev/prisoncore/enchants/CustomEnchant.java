/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants;

import net.ultradev.prisoncore.enchants.enchantments.*;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomEnchant {
    public static final CustomEnchantment UNBREAKABLE = new CustomEnchantment("unbreakable", 100);
    public static final Cataclysm CATACLYSM = new Cataclysm(101);
    public static final Transcendental TRANSCENDENTAL = new Transcendental(102);
    public static final Explosive EXPLOSIVE = new Explosive(103);
    public static final Obliterate OBLITERATE = new Obliterate(104);
    public static final Adept ADEPT = new Adept(105);
    public static final TokenGreed TOKENGREED = new TokenGreed(106);
    public static final Saturated SATURATED = new Saturated(107);
    public static final Eruption ERUPTION = new Eruption(108);
    public static final Detonation DETONATION = new Detonation(109);
    public static final Lucky LUCKY = new Lucky(110);
    public static final Favored FAVORED = new Favored(111);
    public static final JumpBoost JUMPBOOST = new JumpBoost(113);
    // public static final Horde HORDE = new Horde(112);
    public static final Cluster CLUSTER = new Cluster(114);
    public static final Mystic MYSTIC = new Mystic(115);
    public static final Laser LASER = new Laser(116);
    public static final Speed SPEED = new Speed(117);
    public static final Surge SURGE = new Surge(118);
    public static final Magnetic MAGNETIC = new Magnetic(119);
    public static final Merchant MERCHANT = new Merchant(120);
    public static final NightVision NIGHTVISION = new NightVision(122);
    // public static final Union UNION = new Union(121);
    public static final Vortex VORTEX = new Vortex(123);
    public static final Charity CHARITY = new Charity(124);
    public static final Lightning LIGHTNING = new Lightning(125);
    public static final Weight WEIGHT = new Weight(126);
    public static final Haste HASTE = new Haste(127);

    public static ArrayList<Enchantment> ENCHANTS = new ArrayList<>();
    public static HashMap<String, Enchantment> ENCH = new HashMap<>();

    public static CustomEnchantment registerEnchant(String name, int id) {
        CustomEnchantment ench = new CustomEnchantment(name, id);
        ENCHANTS.add(ench);
        ENCH.put(name.toUpperCase(), ench);
        return ench;
    }

    @Deprecated
    public static Enchantment valueOf(String str) {
        if (ENCH.containsKey(str.toUpperCase())) {
            return ENCH.get(str.toUpperCase());
        }
        return getByNameInternal(str);
    }

    public static void init() {
        ENCH.put("UNBREAKABLE", UNBREAKABLE);
        ENCH.put("CATACLYSM", CATACLYSM);
        ENCH.put("EXPLOSIVE", EXPLOSIVE);
        ENCH.put("OBLITERATE", OBLITERATE);
        ENCH.put("ADEPT", ADEPT);
        ENCH.put("TOKENGREED", TOKENGREED);
        ENCH.put("SATURATED", SATURATED);
        ENCH.put("ERUPTION", ERUPTION);
        ENCH.put("LUCKY", LUCKY);
        ENCH.put("FAVORED", FAVORED);
        // ENCH.put("HORDE", HORDE);
        ENCH.put("JUMPBOOST", JUMPBOOST);
        ENCH.put("CLUSTER", CLUSTER);
        ENCH.put("MYSTIC", MYSTIC);
        ENCH.put("LASER", LASER);
        ENCH.put("SPEED", SPEED);
        ENCH.put("SURGE", SURGE);
        ENCH.put("MAGNETIC", MAGNETIC);
        ENCH.put("MERCHANT", MERCHANT);
        // ENCH.put("UNION", UNION);
        ENCH.put("NIGHTVISION", NIGHTVISION);
        ENCH.put("CHARITY", CHARITY);
        ENCH.put("LIGHTNING", LIGHTNING);
        ENCH.put("TRANSCENDENTAL", TRANSCENDENTAL);
        ENCH.put("WEIGHT", WEIGHT);
        ENCH.put("HASTE", HASTE);

        ENCH.put("EFFICIENCY", Enchantment.DIG_SPEED);
        ENCH.put("UNBREAKING", Enchantment.DURABILITY);
        ENCH.put("FORTUNE", Enchantment.LOOT_BONUS_BLOCKS);
    }

    public static void register() {
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (Enchantment ench : ENCHANTS) {
                    Enchantment.registerEnchantment(ench);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public static void unregister() {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

            for (Enchantment ench : ENCHANTS) {
                byId.remove(ench.getId());
                byName.remove(ench.getName());
            }
        } catch (Exception ignored) {
        }
    }

    public static Enchantment getByName(@NotNull String str) {
        return (ENCH.containsKey(str.toUpperCase())) ? getByNameInternal(str) : Enchantment.getByName(str);
    }

    private static Enchantment getByNameInternal(String str) {
        return ENCH.get(str.toUpperCase());
    }
}
