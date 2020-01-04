/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.plugins;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigInteger;
import java.util.List;

public class UltraEconomy implements Economy {
    private static net.ultradev.prisoncore.playerdata.Economy econ = net.ultradev.prisoncore.playerdata.Economy.tokens;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "ultraeco";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return NumberUtils.formatFull((int) Math.floor(amount));
    }

    @Override
    public String currencyNamePlural() {
        return "Money";
    }

    @Override
    public String currencyNameSingular() {
        return "Money";
    }

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        if (op == null)
            return false;
        return hasAccount(op);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return PlayerData.getPlayerData(player) != null;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        if (op == null)
            return 0;
        return getBalance(op);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return econ.getBalance(player).doubleValue();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        if (op == null)
            return null;
        return withdrawPlayer(op, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(amount, econ.removeBalance(player, BigInteger.valueOf((long) Math.floor(amount))).doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        if (op == null)
            return null;
        return depositPlayer(op, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(amount, econ.addBalance(player, BigInteger.valueOf((long) Math.floor(amount))).doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerName);
        if (op == null) {
            return false;
        }
        return createPlayerAccount(op);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return PlayerData.getPlayerData(player) != null;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
