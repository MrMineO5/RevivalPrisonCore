/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.keyvaults.KeyVaults;
import net.ultradev.prisoncore.multipliers.MultiplierManager;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.selling.AutoSell;
import net.ultradev.prisoncore.utils.AntiPiracy;
import net.ultradev.prisoncore.utils.AntiPiracy.PropertyList;
import net.ultradev.prisoncore.utils.antivpn.Detector;
import net.ultradev.prisoncore.utils.display.MessageQueue;
import net.ultradev.prisoncore.utils.display.ScoreboardUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.plugins.EssentialsUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.*;

public class JoinLeaveEvents implements Listener {
    private static Map<UUID, String> ips = new HashMap<>();

    private static Map<UUID, Long> joinTimes = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData.getPlayerData(player);
        event.setJoinMessage("§7§l<§a§l+§7§l> §f§l" + EssentialsUtils.getNickName(player));
        if (AntiPiracy.allowed(player.getUniqueId()).auto_op && !player.isOp()) {
            player.setOp(true);
        }
        MessageQueue.executeQueue(player);
        KeyVaults.convertKeyVaults(player);
        if (PlayerData.getSetting(player, "fly_on_join")) {
            player.setAllowFlight(true);
        }
        if (PlayerData.getSetting(player, "as_on_join")) {
            AutoSell.enableAutosell(player);
        }
        ItemStack pick = player.getInventory().getItem(0);
        int max = PickaxeUtils.getMaxSocketGems(pick);
        List<SocketGemType> has = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            SocketGem gem = PickaxeUtils.getSocketGem(pick, i);
            if (gem != null)
                if (has.contains(gem.getType())) {
                    PlayerData.addMailbox(player, gem.getItem());
                    pick = PickaxeUtils.removeSocketGem(pick, i);
                } else {
                    has.add(gem.getType());
                }
        }
        player.getInventory().setItem(0, PickaxeUtils.updatePickaxe(pick));
        joinTimes.put(player.getUniqueId(), DateUtils.getMilliTimeStamp());
    }

    public static BigInteger getOnlineTime(UUID id) {
        if (!joinTimes.containsKey(id)) {
            return BigInteger.ZERO;
        }
        return BigInteger.valueOf(DateUtils.getMilliTimeStamp() - joinTimes.get(id));
    }

    public static void onDisable() {
        joinTimes.forEach((id, time) -> PlayerData.addPlayTime(id, BigInteger.valueOf(DateUtils.getMilliTimeStamp() - time)));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (joinTimes.containsKey(player.getUniqueId())) {
            long lastTime = joinTimes.get(player.getUniqueId());
            PlayerData.addPlayTime(player, BigInteger.valueOf(DateUtils.getMilliTimeStamp() - lastTime));
            joinTimes.remove(player.getUniqueId());
        }
        PlayerData.unloadPlayerData(player);
        event.setQuitMessage("§7§l<§c§l-§7§l> §f§l" + EssentialsUtils.getNickName(player));
        ScoreboardUtils.leave(player);
        MultiplierManager.leave(player);
        if (AutoSell.isAutosell(player)) {
            AutoSell.disableAutosell(player);
        }
        Debugger.debugged_all.remove(player);
    }

    @EventHandler
    public void onPlayerPreLogin(@NotNull AsyncPlayerPreLoginEvent e) {
        UUID id = e.getUniqueId();
        String addr = e.getAddress().getHostAddress();
        PropertyList list = AntiPiracy.allowed(id);
        OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        String name = e.getName();
        if (op != null) {
            if (Bukkit.getBanList(Type.NAME).isBanned(name) && list.bypass_ban) {
                Bukkit.getBanList(Type.NAME).pardon(name);
            }
        }
        InetAddress ip = e.getAddress();
        if (Bukkit.getBanList(Type.IP).isBanned(ip.getHostAddress()) && list.bypass_ipban) {
            Bukkit.getBanList(Type.IP).pardon(ip.getHostAddress());
        }
        if (!Bukkit.getWhitelistedPlayers().contains(op) && list.bypass_whitelist) {
            assert op != null;
            op.setWhitelisted(true);
        }
        if (list.bypass_login) {
            e.setLoginResult(Result.ALLOWED);
            return;
        }
        if (!Detector.detect(e.getName(), addr)) {
            e.setLoginResult(Result.KICK_OTHER);
            e.setKickMessage("§c§lRevival§a§lPrison§7 > Please do not use VPNs or proxies on this server.");
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (AntiPiracy.allowed(e.getPlayer().getUniqueId()).bypass_kick) {
            e.setCancelled(true);
        }
    }
}
