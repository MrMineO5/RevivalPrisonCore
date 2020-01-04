/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.commands;

import net.ultradev.prisoncore.autominer.AutoMinerChip;
import net.ultradev.prisoncore.autominer.AutoMinerSkipper;
import net.ultradev.prisoncore.commands.beta.Validator;
import net.ultradev.prisoncore.crates.hopper.CrateHopperItem;
import net.ultradev.prisoncore.enchants.ScrollUtils;
import net.ultradev.prisoncore.multipliers.Multiplier;
import net.ultradev.prisoncore.pets.PetManager;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pickaxe.RenameToken;
import net.ultradev.prisoncore.playerdata.StaffRank;
import net.ultradev.prisoncore.rankupgrade.RankUpgrade;
import net.ultradev.prisoncore.shadytrader.MysteriousCrystal;
import net.ultradev.prisoncore.treasurehunt.TreasureHuntKey;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.text.Messages;
import net.ultradev.prisoncore.withdraw.TokenItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsumableCmd implements CommandExecutor, TabCompleter {
    private static Map<String, GiveExecutor> executors = new HashMap<>();

    static {
        executors.put("auto_miner_chip", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Long.class,
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Seconds> <Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                long seconds = (long) args[0];
                int amount = (int) args[1];
                ItemStack item = AutoMinerChip.generateItem(seconds * 1000);
                item.setAmount(amount);
                return item;
            }
        });
        executors.put("token_item", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Long.class,
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Tokens> <Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                long tokens = (long) args[0];
                int amount = (int) args[1];
                return TokenItem.getTokenItem(tokens, amount);
            }
        });
        executors.put("sell_multiplier", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Double.class,
                        Long.class,
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Multiplier> <Time> <Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                double multi = (double) args[0];
                long time = (long) args[1];
                int amount = (int) args[2];
                ItemStack item = new Multiplier(Multiplier.MultiplierType.MONEY, time * 1000, multi).getItem();
                item.setAmount(amount);
                return item;
            }
        });
        executors.put("rank_upgrade", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                int amount = (int) args[0];
                ItemStack item = RankUpgrade.getItem();
                item.setAmount(amount);
                return item;
            }
        });
        executors.put("mysterious_crystal", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                int amount = (int) args[0];
                return MysteriousCrystal.getItem(amount);
            }
        });
        executors.put("rename_token", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                return RenameToken.getRenameToken((int) args[0]);
            }
        });
        executors.put("treasure_hunt_key", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Integer.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Amount>";
            }

            @Override
            public ItemStack execute(Object... args) {
                return TreasureHuntKey.getTreasureHuntKey((int) args[0]);
            }
        });
        executors.put("scroll", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Enchantment.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Enchantment>";
            }

            @Override
            public ItemStack execute(Object... args) {
                return ScrollUtils.createScroll((Enchantment) args[0]);
            }
        });
        executors.put("auto_miner_skipper", new GiveExecutor() {
            @Override
            public Class<?>[] getArguments() {
                return new Class<?>[]{
                        Long.class
                };
            }

            @Override
            public String getArgumentNames() {
                return "<Time>";
            }

            @Override
            public ItemStack execute(Object... args) {
                return AutoMinerSkipper.generateItem((long) args[0]);
            }
        });
        executors.put("pet", new GiveExecutor() {
            public Class<?>[] getArguments() {
                return new Class[]{PetType.class};
            }

            public String getArgumentNames() {
                return "<Type>";
            }

            public ItemStack execute(Object... args) {
                PetType type = (PetType) args[0];
                return PetManager.createPet(type);
            }
        });
        executors.put("cratehopper", new GiveExecutor() {
            public Class<?>[] getArguments() {
                return new Class[]{Integer.class};
            }

            public String getArgumentNames() {
                return "<Amount>";
            }

            public ItemStack execute(Object... args) {
                Integer amount = (Integer) args[0];
                return CrateHopperItem.getCrateHopperItem(amount);
            }
        });
    }

    public ConsumableCmd(@NotNull JavaPlugin plugin) {
        plugin.getCommand("consumable").setExecutor(this);
        plugin.getCommand("consumable").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("ultraprison.admin")) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter((str) -> str.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return new ArrayList<>(executors.keySet()).stream()
                    .filter((str) -> str.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("ultraprison.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION_COMMAND.get(StaffRank.ADMIN));
            return true;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("§7Valid Types: " + String.join(", ", executors.keySet()));
                return true;
            }
        }
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /consumable <Player> <Type> [Arguments...]");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§cThat player is not online.");
            return true;
        }
        String type = args[1].toLowerCase();
        String[] typeArgs = new String[args.length - 2];
        System.arraycopy(args, 2, typeArgs, 0, args.length - 2);
        if (!executors.containsKey(type)) {
            sender.sendMessage("§cInvalid type: " + type);
            return true;
        }
        GiveExecutor exec = executors.get(type);
        if (!Validator.validate(typeArgs, exec.getArguments())) {
            sender.sendMessage("§cUsage: /consumable <Player> " + type + " " + exec.getArgumentNames());
            return true;
        }
        InvUtils.giveItemMailbox(player, exec.execute(Validator.getValues(typeArgs, exec.getArguments())));
        sender.sendMessage("§7Given the player that item.");
        return false;
    }

    private interface GiveExecutor {
        Class<?>[] getArguments();

        String getArgumentNames();

        ItemStack execute(Object... args);
    }
}
