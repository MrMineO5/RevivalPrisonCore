/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.playerdata;

import org.bukkit.OfflinePlayer;

import java.math.BigInteger;
import java.util.UUID;

public class Economy {
    public static final Economy tokens = new Economy("tokens");
    public static final Economy dust = new Economy("dust");
    public static final Economy platinumTokens = new Economy("platinumtokens");

    private String name;

    public Economy(String name) {
        this.name = name;
    }

    public BigInteger getBalance(UUID id) {
        return PlayerData.getEconomy(id, name);
    }
    public BigInteger getBalance(OfflinePlayer op) {
        return PlayerData.getEconomy(op, name);
    }

    public BigInteger setBalance(OfflinePlayer op, long amount) {
        return setBalance(op, BigInteger.valueOf(amount));
    }

    public BigInteger setBalance(OfflinePlayer op, BigInteger amount) {
        return PlayerData.setEconomy(op, name, amount);
    }

    public BigInteger addBalance(OfflinePlayer op, long amount) {
        return addBalance(op, BigInteger.valueOf(amount));
    }

    public BigInteger addBalance(OfflinePlayer op, BigInteger amount) {
        return PlayerData.addEconomy(op, name, amount);
    }

    public BigInteger removeBalance(OfflinePlayer op, long amount) {
        return removeBalance(op, BigInteger.valueOf(amount));
    }

    public BigInteger removeBalance(OfflinePlayer op, BigInteger amount) {
        return PlayerData.removeEconomy(op, name, amount);
    }

    public boolean hasBalance(OfflinePlayer op, long amount) {
        return hasBalance(op, BigInteger.valueOf(amount));
    }

    public boolean hasBalance(OfflinePlayer op, BigInteger amount) {
        return getBalance(op).compareTo(amount) > 0;
    }
}
