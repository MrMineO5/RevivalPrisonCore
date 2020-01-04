/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Arguments {
    private static final Argument<String> STRING = new Argument<String>() {
        public String parse(String str) {
            return str;
        }

        public boolean isNotValid(String str) {
            return false;
        }
    };
    private static final Argument<Integer> INTEGER = new Argument<Integer>() {
        public Integer parse(String str) {
            return Integer.parseInt(str);
        }

        public boolean isNotValid(String str) {
            try {
                Integer.parseInt(str);
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    };
    private static final Argument<Double> DOUBLE = new Argument<Double>() {
        public Double parse(String str) {
            return Double.parseDouble(str);
        }

        public boolean isNotValid(String str) {
            try {
                Double.parseDouble(str);
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    };
    private static final Argument<Long> LONG = new Argument<Long>() {
        public Long parse(String str) {
            return Long.parseLong(str);
        }

        public boolean isNotValid(String str) {
            try {
                Long.parseLong(str);
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    };
    private static final Argument<BigInteger> BIGINTEGER = new Argument<BigInteger>() {
        public BigInteger parse(String str) {
            return new BigInteger(str);
        }

        public boolean isNotValid(String str) {
            try {
                new BigInteger(str);
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    };
    private static final Argument<Player> PLAYER = new Argument<Player>() {
        public Player parse(String str) {
            return Bukkit.getPlayer(str);
        }

        public boolean isNotValid(String str) {
            Player player = Bukkit.getPlayer(str);
            return (player == null);
        }
    };
    private static final Argument<OfflinePlayer> OFFLINEPLAYER = new Argument<OfflinePlayer>() {
        public OfflinePlayer parse(String str) {
            return Bukkit.getOfflinePlayer(str);
        }

        public boolean isNotValid(String str) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(str);
            return (player == null);
        }
    };
    private static final Argument<Mine> MINE = new Argument<Mine>() {
        public Mine parse(String str) {
            return MineManager.getMine(str);
        }

        public boolean isNotValid(String str) {
            return (MineManager.getMine(str) == null);
        }
    };
    private static final Argument<Enchantment> ENCHANTMENT = new Argument<Enchantment>() {
        public Enchantment parse(String str) {
            return CustomEnchant.getByName(str);
        }

        public boolean isNotValid(String str) {
            try {
                Enchantment enchant = CustomEnchant.getByName(str);
                assert enchant != null;
            } catch (Exception e) {
                return true;
            }
            return false;
        }
    };
    private static Map<Class<?>, Argument<?>> args = new HashMap<>();

    static {
        args.put(String.class, STRING);
        args.put(Integer.class, INTEGER);
        args.put(Double.class, DOUBLE);
        args.put(Long.class, LONG);
        args.put(BigInteger.class, BIGINTEGER);
        args.put(Player.class, PLAYER);
        args.put(OfflinePlayer.class, OFFLINEPLAYER);
        args.put(Mine.class, MINE);
        args.put(Enchantment.class, ENCHANTMENT);
    }

    @NotNull
    public static Argument<?> getArgument(Class<?> clazz) {
        if (args.containsKey(clazz))
            return args.get(clazz);
        if (clazz.isEnum()) {
            args.put(clazz, new EnumArgument(clazz));
            return getArgument(clazz);
        }
        return args.get(String.class);
    }
}
