/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.mines.WandManager;
import net.ultradev.prisoncore.playerdata.PlayerData;
import net.ultradev.prisoncore.playerdata.StoreRank;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.Parser;
import net.ultradev.prisoncore.utils.gui.guis.GUIManager;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MineCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sendUsageAdmin(sender);
                return false;
            }
            Player player = (Player) sender;
            GUIManager.openGUI(player, "mines");
            return true;
        }

        if (args[0].equals("help")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mines§7: Opens the mines GUI");
                return true;
            }
            sendUsageAdmin(sender);
            return true;
        }
        if (args[0].equals("list")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            int page = 1;
            if (args.length > 1) {
                page = Integer.parseInt(args[1]);
            }
            for (String str : MineManager.getMinesPage(page)) {
                sender.sendMessage(str);
            }
            return true;
        }
        if (args[0].equals("create")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (MineManager.createMine(args[1])) {
                MineManager.saveMines();
                sender.sendMessage("§aSuccessfully created mine: " + args[1]);
                return true;
            } else {
                sender.sendMessage("§cAn error occured while creating the mine, maybe it already exists?");
                return false;
            }
        }
        if (args[0].equals("wand")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be used through console.");
                return false;
            }
            Player player = (Player) sender;
            ItemStack wand = new ItemFactory(Material.WOOD_SPADE).setName("§aMine Wand")
                    .setLore("§7Left click to select the first position", "§7Right click to select the second position")
                    .addNBT("type", "minewand").create();
            player.getInventory().addItem(wand);
            player.sendMessage("§aThe wand has been added to your inventory.");
            return true;
        }
        if (args[0].equals("setpos")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be used through console.");
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 2) {
                sender.sendMessage("§cUsage: §a/mine §2setpos <Name>");
                return false;
            }
            if (!WandManager.setPositions(args[1], player)) {
                player.sendMessage("§cMine not found!");
                return false;
            }
            MineManager.saveMines();
            player.sendMessage("§aSet positions for the mine " + args[1] + " to your current selection");
            return true;
        }
        if (args[0].equals("setspawn")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command cannot be used through console.");
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 2) {
                sender.sendMessage("§cUsage: §a/mine §2setspawn <Name>");
                return false;
            }
            if (!MineManager.setSpawnLocation(args[1], player)) {
                player.sendMessage("§cMine not found!");
                return false;
            }
            MineManager.saveMines();
            player.sendMessage("§aSet the spawn position for the mine " + args[1] + " to your current location");
            return true;
        }
        if (args[0].equals("composition")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("§cUsage:");
                sender.sendMessage("§a/mine §2composition <Name> list");
                sender.sendMessage("§a/mine §2composition <Name> add <Material> <Chance>");
                sender.sendMessage("§a/mine §2composition <Name> remove <Material>");
                return true;
            }
            Mine mine = MineManager.getMine(args[1]);
            if (mine == null) {
                sender.sendMessage("§cThat mine does not exist.");
                return false;
            }
            if (args[2].equals("list")) {
                if (args.length != 3) {
                    sender.sendMessage("§cUsage: §a/mine §2composition <Mine> list");
                    return false;
                }
                List<String> msgs = mine.listComposition();
                for (String str : msgs) {
                    sender.sendMessage(str);
                }
                return true;
            }
            if (args[2].equals("add")) {
                if (args.length != 5) {
                    sender.sendMessage("§cUsage: §a/mine §2composition <Mine> add <Material> <Chance>");
                    return false;
                }
                MaterialData mat = Parser.parseMaterialData(args[3].toUpperCase());
                if (mat == null) {
                    sender.sendMessage("§cInvalid material: " + args[3]);
                    return false;
                }
                double chance = Double.parseDouble(args[4]);
                if (mine.addMaterial(mat, chance)) {
                    MineManager.saveMines();
                    sender.sendMessage("§aSuccessfully added material to mine!");
                } else {
                    sender.sendMessage("§cPercentage too high, please remove a different material first.");
                }
                return true;
            }
            if (args[2].equals("remove")) {
                if (args.length != 4) {
                    sender.sendMessage("§cUsage: §a/mine §2composition <Mine> remove <Material>");
                    return false;
                }
                MaterialData mat = Parser.parseMaterialData(args[3].toUpperCase());
                if (mat == null) {
                    sender.sendMessage("§cInvalid material: " + args[3]);
                    return false;
                }
                if (mine.removeMaterial(mat)) {
                    MineManager.saveMines();
                    sender.sendMessage("§aSuccessfully removed material from mine!");
                } else {
                    sender.sendMessage("§cMaterial is not part of the mine's composition.");
                }
                return true;
            }
        }
        if (args[0].equals("reset")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage("§cUsage: §a/mine §2reset <Name>");
                return false;
            }
            if (!MineManager.resetMine(args[1])) {
                sender.sendMessage("§cMine not found!");
                return false;
            }
            sender.sendMessage("§aResetting the mine " + args[1]);
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage("§cUsage: §a/mine §2delete <Name>");
                return false;
            }
            if (!MineManager.mines.containsKey(args[1])) {
                sender.sendMessage("§cMine not found!");
                return false;
            }
            MineManager.mines.remove(args[1]);
            sender.sendMessage("§aDeleted the mine " + args[1]);
            return true;
        }
        if (args[0].equalsIgnoreCase("setresettimer")) {
            if (!sender.hasPermission("mines.admin")) {
                sender.sendMessage("§cUsage: §a/mine§7: Opens the mines GUI");
                return true;
            }
            if (args.length != 3) {
                sender.sendMessage("§cUsage: §a/mine §2setresettimer <Name> <Timer>");
                return false;
            }
            if (!MineManager.mines.containsKey(args[1])) {
                sender.sendMessage("§cMine not found!");
                return false;
            }
            long timer;
            try {
                timer = Long.parseLong(args[2]);
            } catch(Exception e) {
                sender.sendMessage("§cInvalid number: " + args[2]);
                return false;
            }
            if (timer < 30000) {
                sender.sendMessage("§cTime to small, must be at least 30 seconds");
                return false;
            }
            MineManager.mines.get(args[1]).setResetTime(timer);
            sender.sendMessage("§aSet reset timer for mine " + args[1]);
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ultraprison.admin")) {
                Mine mine = MineManager.getMine(args[0]);
                if (mine == null) {
                    player.sendMessage("§cThat mine does not exist.");
                    return false;
                }
                player.teleport(mine.getTpPos());
                return true;
            }
            int i = RankupManager.getIdOf(args[0].toLowerCase());
            if (i != -1) {
                if (PlayerData.getRank(player) >= i) {
                    Mine mine = MineManager.getMine(args[0]);
                    if (mine == null) {
                        player.sendMessage("§cThat mine does not exist.");
                        return false;
                    }
                    player.teleport(mine.getTpPos());
                } else {
                    player.sendMessage("§cYou do not have access to that mine.");
                }
            }

            try {
                StoreRank rank = StoreRank.valueOf(args[0].toUpperCase());
                if (!rank.has(player)) {
                    player.sendMessage("§cThat mine requires §6" + rank.getPrefix() + "§7 rank or above.");
                    return true;
                }
                Mine mine = MineManager.getMine(args[0]);
                if (mine == null) {
                    player.sendMessage("§cThat mine does not exist.");
                    return false;
                }
                Location loc = mine.getTpPos();
                if (loc == null) {
                    player.sendMessage("§cThat mine is incorrectly configured, please report it to an admin or above");
                    return true;
                }
                player.teleport(loc);
                return true;
            } catch(Exception ignored) {}
        }
        return false;
    }

    private void sendUsageAdmin(@NotNull CommandSender sender) {
        sender.sendMessage("§cUsage:");
        sender.sendMessage("§a/mine§7: Opens the mines GUI");
        sender.sendMessage("§a/mine §2list§7: List all existing mines");
        sender.sendMessage("§a/mine §2create <Name>§7: Create a new mine");
        sender.sendMessage("§a/mine §2wand§7: Get a wand to redefine mine selection.");
        sender.sendMessage("§a/mine §2setpos <Name>§7: Set the mine to the current selection.");
        sender.sendMessage("§a/mine §2setspawn <Name>§7: Set the spawn of a mine to your current location.");
        sender.sendMessage("§a/mine §2composition <Name> list§7: List the current composition of a mine.");
        sender.sendMessage(
                "§a/mine §2composition <Name> add <Material> <Chance>§7: Add a material to the composition of a mine.");
        sender.sendMessage(
                "§a/mine §2composition <Name> remove <Material>§7: Remove a material from the composition of a mine.");
        sender.sendMessage("§a/mine §2reset <Name>§7: Reset a mine");
    }
}
