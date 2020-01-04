/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands.beta;

import net.citizensnpcs.api.npc.NPC;
import net.ultradev.prisoncore.autominer.AutoMinerUtils;
import net.ultradev.prisoncore.bombs.Bombs;
import net.ultradev.prisoncore.commands.beta.commandtypes.GUICommand;
import net.ultradev.prisoncore.commands.beta.commandtypes.MessageCommand;
import net.ultradev.prisoncore.crates.Crate;
import net.ultradev.prisoncore.crates.CrateManager;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.keyvaults.KeyVaultManager;
import net.ultradev.prisoncore.keyvaults.KeyVaultType;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGem;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemTier;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.antivpn.Detector;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.math.BigMath;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.text.TextUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import net.ultradev.prisoncore.withdraw.TokenItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static Map<String, Command> cmdMap = new HashMap<>();

    private static void register(Command cmd) {
        if (cmdMap.containsKey(cmd.getName().toLowerCase())) {
            return;
        }
        cmdMap.put(cmd.getName().toLowerCase(), cmd);
    }

    public static void registerAll(JavaPlugin plugin) {
        register(new Command("givebomb", "<Player> <Power> <Amount>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            int power = (int) args[1];
            int amount = (int) args[2];
            InvUtils.giveItemMailbox(player, Bombs.generateBombItem(power, amount));
            sender.sendMessage("§7Given §6" + player.getName() + " §6" + amount + " power §6" + power + " bombs§7!");
            return true;
        }, Player.class, Integer.class, Integer.class));
        register(new Command("givesocketgem", "<Player> <Type> <Tier> <Percent>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            SocketGemType type = (SocketGemType) args[1];
            SocketGemTier tier = (SocketGemTier) args[2];
            double percent = (double) args[3];
            InvUtils.giveItemMailbox(player, new SocketGem(type, tier, percent).getItem());
            sender.sendMessage("§7Given §6" + player.getName() + " a " + tier.getDisplayName() + " " + type.getName() + " " + percent + " Socket Gem!");
            return true;
        }, Player.class, SocketGemType.class, SocketGemTier.class, Double.class));
        register(new Command("setrank", "<Player> <Rank>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            int rank = RankupManager.getIdOf((String) args[1]);
            if (rank == -1) {
                sender.sendMessage("§cThat rank does not exist.");
                return false;
            }
            PlayerData.setRank(player, rank);
            return true;
        }, Player.class, String.class));
        register(new Command("givekey", "<Player> <Type> <Amount>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            Integer amount = (Integer) args[2];
            Crate crate = CrateManager.getCrate((String) args[1]);
            if (crate == null) {
                sender.sendMessage("Invalid Crate: " + args[1]);
                return true;
            }
            CrateManager.giveKeys(player, crate, amount);
            sender.sendMessage("§aSuccess.");
            return true;
        }, Player.class, String.class, Integer.class));
        register(new Command("addtokens", "<Player> <Type> <Amount>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            Long amount = (Long) args[1];
            Economy.tokens.addBalance(player, amount);
            sender.sendMessage("§aSuccess.");
            return true;
        }, Player.class, Long.class));
        register(new Command("clearmine", "<Mine>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Mine mine = (Mine) args[0];
            mine.clearMine();
            sender.sendMessage("§aSuccess.");
            return true;
        }, Mine.class));
        register(new Command("addtime", "<Player> <Seconds>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            BigInteger i = (BigInteger) args[1];
            PlayerData.addAutoSellTime(player, i);
            sender.sendMessage("§aSuccess.");
            return true;
        }, Player.class, BigInteger.class));
        register(new Command("givescroll", "<Player> <Enchantment> <Amount>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            Enchantment enchant = (Enchantment) args[1];
            int amount = (int) args[2];
            ItemStack item = ScrollUtils.createScroll(enchant);
            item.setAmount(amount);
            InvUtils.giveItemMailbox(player, item);
            sender.sendMessage("§aSuccess!");
            return true;
        }, Player.class, Enchantment.class, Integer.class));
        register(new Command("fix", "", false, RequiredRank.EMERALD, plugin, (sender, args) -> {
            Player player = (Player) sender;
            player.getInventory().getItem(0).setDurability((short) 0);
            player.sendMessage("§aYour pickaxe was repaired successfully.");
            return true;
        }));
        register(new Command("resetmine", "<Mine>", false, RequiredRank.DIAMOND, plugin, (sender, args) -> {
            Player player = (Player) sender;
            if (!CooldownUtils.isCooldown(player, "resetmine")) {
                player.sendMessage("§cYou need to wait " + Math.floorDiv(CooldownUtils.getCooldownTime(player, "resetmine"), 1000) + " seconds before using that again.");
                return true;
            }
            CooldownUtils.setCooldown(player, "resetmine", 5);
            Mine mine = (Mine) args[0];
            mine.resetMine();
            player.sendMessage("§aYou successfully reset that mine.");
            return true;
        }, Mine.class));
        register(new Command("testgui", "<GUI>", false, RequiredRank.ADMIN, plugin, (sender, args) -> {
            String gui = (String) args[0];
            Player player = (Player) sender;
            GUIManager.openGUI(player, gui);
            return true;
        }, String.class));
        register(new Command("fly", "", false, RequiredRank.EMERALD, plugin, (sender, args) -> {
            Player player = (Player) sender;
            boolean flying = player.getAllowFlight();
            player.setAllowFlight(!flying);
            if (flying) {
                player.sendMessage("§cFlight disabled.");
            } else {
                player.sendMessage("§aFlight enabled.");
            }
            return true;
        }));
        register(new Command("test", "", false, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) sender;
            NPC npc = AutoMinerUtils.getModel(player).getNpc();
            if (npc.isSpawned()) {
                npc.despawn();
            } else {
                npc.spawn(player.getLocation());
            }
            return true;
        }));
        register(new Command("max", "<Player>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            Pickaxe pick = new Pickaxe(player);
            for (Enchantment enchant : EnchantInfo.enchants.keySet()) {
                pick.setEnchantmentLevel(enchant, EnchantInfo.getMaxScrollLevel(enchant));
            }
            pick.applyPickaxe();
            return true;
        }, Player.class));
        register(new Command("new", "<Player>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            Pickaxe pick = new Pickaxe(player);
            for (Enchantment enchant : EnchantInfo.enchants.keySet()) {
                pick.setEnchantmentLevel(enchant, 0);
            }
            pick.applyPickaxe();
            return true;
        }, Player.class));
        register(new Command("givekeyvault", "<Player> <Type>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            Player player = (Player) args[0];
            String t = (String) args[1];
            KeyVaultType type = KeyVaultType.valueOf(t.toUpperCase());
            InvUtils.giveItemMailbox(player, KeyVaultManager.generateKeyVault(type));
            return true;
        }, Player.class, String.class));
        register(new Command("opengui", "<Player> <GUI>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            GUIManager.openGUI((Player) args[0], (String) args[1]);
            return true;
        }, Player.class, String.class));
        register(new Command("updaterank", "", false, RequiredRank.NONE, plugin, (sender, args) -> {
            GUIManager.openGUI((Player) sender, "tinker");
            return true;
        }));
        register(new Command("withdraw", "<Amount>", false, RequiredRank.NONE, plugin, (sender, args) -> {
            int amount = (int) args[0];
            if (amount < 1) {
                sender.sendMessage("§cHey! No tokens, no item!");
                return false;
            }
            Player player = (Player) sender;
            if (!Economy.tokens.hasBalance(player, amount)) {
                player.sendMessage("§cYou do not have enough tokens.");
                return false;
            }
            Economy.tokens.removeBalance(player, amount);
            player.getInventory().addItem(TokenItem.getTokenItem(amount, 1));
            return true;
        }, Integer.class));
        register(new Command("vote", "", false, RequiredRank.NONE, plugin, (sender, args) -> {
            Player player = (Player) sender;
            player.spigot().sendMessage(TextUtils.createVoteLink("https://minecraft-mp.com/server/238093/vote/"));
            player.spigot().sendMessage(TextUtils.createVoteLink("https://minecraftservers.org/vote/567987"));
            player.spigot().sendMessage(TextUtils.createVoteLink("https://topg.org/Minecraft/in-585822"));
            player.spigot().sendMessage(TextUtils.createVoteLink("https://minecraft-server.net/vote/RevivalPrison/"));
            return true;
        }));
        register(new Command("rankup", "", false, RequiredRank.NONE, plugin, (sender, args) -> {
            Player player = (Player) sender;
            if (PlayerData.canAffordRankup(player)) {
                PlayerData.rankup(player);
                Bukkit.broadcastMessage(
                        "§a» §e" + player.getName() + " ranked up to §7[" + PlayerData.getRankDisplayName(player) + "§7]");
            } else {
                player.sendMessage("§cInsufficient funds! You need §e"
                        + NumberUtils.formatFull(RankupManager.getRankupCost(player)) + " §cto rank up!");
            }
            return true;
        }));
        register(new Command("maxrankup", "", false, RequiredRank.NONE, plugin, (sender, args) -> {
            Player player = (Player) sender;
            if (PlayerData.canAffordRankup(player)) {
                PlayerData.rankupMax(player);
                Bukkit.broadcastMessage(
                        "§a» §e" + player.getName() + " ranked up to §7[" + PlayerData.getRankDisplayName(player) + "§7]");
            } else {
                player.sendMessage("§cInsufficient funds! You need §e"
                        + NumberUtils.formatFull(RankupManager.getRankupCost(player)) + " §cto rank up!");
            }
            return true;
        }));
        register(new Command("pay", "<Player> <Amount>", false, RequiredRank.NONE, plugin, (sender, args) -> {
            Player player = (Player) sender;
            Player p2 = (Player) args[0];
            int amount = (int) args[1];
            if (amount < 1) {
                player.sendMessage("§cRUDE... Pay at least 1 Tokens!");
                return true;
            }
            if (Economy.tokens.hasBalance(player, amount)) {
                Economy.tokens.removeBalance(player, amount);
                Economy.tokens.addBalance(p2, amount);
                player.sendMessage("§7Sent §e" + NumberUtils.formatFull(amount) + " Tokens§7 to §e" + p2.getName() + "§7!");
                p2.sendMessage("§7Received §e" + NumberUtils.formatFull(amount) + " Tokens§7 from §e" + player.getName() + "§7!");
            }
            return true;
        }, Player.class, Integer.class));
        register(new Command("whois", "<Player>", true, RequiredRank.ADMIN, plugin, (sender, args) -> {
            OfflinePlayer op = (OfflinePlayer) args[0];
            sender.sendMessage("§7§m----------§7 §c§l" + op.getName() + " §7§m----------");
            if (op.isOnline()) {
                sender.sendMessage("§7 - Nick: §e" + op.getPlayer().getDisplayName());
            }
            sender.sendMessage("§7 - UUID: §e" + op.getUniqueId().toString());
            if (op.isOnline()) {
                sender.sendMessage("§7 - Health: §e" + op.getPlayer().getHealth() + "/" + op.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                sender.sendMessage("§7 - Hunger: §e" + op.getPlayer().getFoodLevel() + "/20");
                sender.sendMessage("§7 - Exp: §e" + op.getPlayer().getTotalExperience() + " (Level " + op.getPlayer().getExpToLevel() + ")");
                // sender.sendMessage("§7 - Location: §e" + ); TODO: Location
            }
            sender.sendMessage("§7 - Tokens: §e" + NumberUtils.formatFull(Economy.tokens.getBalance(op)));
            sender.sendMessage("§7 - Platinum Tokens: §e" + NumberUtils.formatFull(Economy.platinumTokens.getBalance(op)));
            sender.sendMessage("§7 - Dust Bank: §e" + NumberUtils.formatFull(Economy.dust.getBalance(op)));
            if (op.isOnline()) {
                Player p = op.getPlayer();
                String ip = p.getAddress().getHostName();
                sender.sendMessage("§7 - IP Address: §e" + ip);
                sender.sendMessage("§7 - IP Location: §e" + Detector.getLocation(ip));
                sender.sendMessage("§7 - Gamemode: §e" + p.getGameMode().name());
                sender.sendMessage("§7 - Fly mode: §e" + p.getAllowFlight() + (p.isFlying() ? " (flying)" : ""));
            }
            return true;
        }, OfflinePlayer.class));

        register(new GUICommand("autominer", plugin));
        register(new GUICommand("tinker", plugin));
        register(new GUICommand("settings", plugin));
        register(new GUICommand("faq", plugin));
        register(new GUICommand("dustbank", plugin));
        register(new GUICommand("kvcraft", plugin));

        register(new GUICommand("baltop", "leaderboard", new String[]{"TOKENS"}, plugin));
        register(new GUICommand("ranktop", "leaderboard", new String[]{"RANKS"}, plugin));

        register(new MessageCommand("discord", "§7Discord: §edc.revivalprison.com", RequiredRank.NONE, plugin));

        new StandardCommand("playtime", "[Player]", true, RequiredRank.NONE, plugin, (sender, cmd, label, args) -> {
            OfflinePlayer target;
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cUsage: /playtime <Player>");
                    return true;
                }
                target = (Player) sender;
            } else {
                target = Bukkit.getOfflinePlayer(args[0]);
            }
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }
            BigInteger playtime = PlayerData.getPlayTime(target);
            sender.sendMessage("§e" + target.getName() + "'s §7playtime: §e" + DateUtils.readableDate(playtime.divide(BigMath.THOUSAND)));
            return true;
        });
    }
}
