/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.gui.guis.leaderboards;

import lombok.Getter;
import net.ultradev.prisoncore.leaderboards.LeaderBoards;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.math.BigMath;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderBoardGUI implements GUI {
    private static Map.Entry<UUID, BigInteger> getNth(LinkedHashMap<UUID, BigInteger> map, int id) {
        if (map == null) {
            return null;
        }
        if (map.size() <= id) {
            return null;
        }
        return map.entrySet().stream().skip(id).findFirst().get();
    }

    private static ItemStack generateCategoryItem(LeaderBoardType type, LeaderBoardType sel) {
        boolean selected = type.equals(sel);
        String[][] script = {{"inv:open", "leaderboard", type.name()}};
        switch (type) {
            case TOKENS:
                return new ItemFactory(Material.DOUBLE_PLANT)
                        .setName("§6§lTokens")
                        .setClickEvent(selected ? GUIUtils.noop : script)
                        .addEnchantment(selected ? Enchantment.DIG_SPEED : null)
                        .hideFlags()
                        .create();
            case RANKS:
                return new ItemFactory(Material.GOLD_INGOT)
                        .setName("§6§lRanks")
                        .setClickEvent(selected ? GUIUtils.noop : script)
                        .addEnchantment(selected ? Enchantment.DIG_SPEED : null)
                        .hideFlags()
                        .create();
            case PICKAXE_LEVEL:
                return new ItemFactory(Material.DIAMOND_PICKAXE)
                        .setName("§6§lPickaxe Levels")
                        .setClickEvent(selected ? GUIUtils.noop : script)
                        .addEnchantment(selected ? Enchantment.DIG_SPEED : null)
                        .hideFlags()
                        .create();
            case PLAYTIME:
                return new ItemFactory(Material.WATCH)
                        .setName("§6§lPlaytime")
                        .setClickEvent(selected ? GUIUtils.noop : script)
                        .addEnchantment(selected ? Enchantment.DIG_SPEED : null)
                        .hideFlags()
                        .create();
        }
        return null;
    }

    private static ItemStack createLeaderboardItem(LeaderBoardType type, int id) {
        Map.Entry<UUID, BigInteger> entr = type.getNth(id);
        if (entr == null) {
            return null;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(entr.getKey());
        ItemStack i = new ItemFactory(Material.SKULL_ITEM, 1, 3)
                .setName((id < 3 ? "§6§l" : "§6") + "#" + (id + 1) + " " + op.getName())
                .setLore(type.getLore(entr))
                .create();
        SkullMeta meta = (SkullMeta) i.getItemMeta();
        meta.setOwningPlayer(op);
        i.setItemMeta(meta);
        i = GUIUtils.addClickEvent(i, GUIUtils.noop);
        return i;
    }

    public Inventory generateGUI(Player player, String... args) {
        Inventory inv = Bukkit.createInventory(null, 54, "§2§lLeaderboard");
        LeaderBoardType type = LeaderBoardType.TOKENS;
        if (args.length == 1) {
            type = LeaderBoardType.valueOf(args[0]);
        }
        inv.setItem(0, generateCategoryItem(LeaderBoardType.TOKENS, type));
        inv.setItem(1, generateCategoryItem(LeaderBoardType.RANKS, type));
        inv.setItem(2, generateCategoryItem(LeaderBoardType.PICKAXE_LEVEL, type));
        inv.setItem(3, generateCategoryItem(LeaderBoardType.PLAYTIME, type));

        inv.setItem(13, createLeaderboardItem(type, 0));
        inv.setItem(21, createLeaderboardItem(type, 1));
        inv.setItem(23, createLeaderboardItem(type, 2));
        for (int i = 27; i < 54; i++) {
            inv.setItem(i, createLeaderboardItem(type, i - 24));
        }
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller(9));
        return inv;
    }

    public enum LeaderBoardType {
        TOKENS("tokens"),
        RANKS("ranks"),
        PICKAXE_LEVEL("pickaxeLevel"),
        PLAYTIME("playtime");

        @Getter
        private String name;
        LeaderBoardType(String name) {
            this.name = name;
        }

        public Map.Entry<UUID, BigInteger> getNth(int id) {
            LinkedHashMap<UUID, BigInteger> map = LeaderBoards.getElementMap().get(this.name).getSorted();
            return LeaderBoardGUI.getNth(map, id);
        }

        public String[] getLore(Map.Entry<UUID, BigInteger> m) {
            switch (this) {
                case TOKENS:
                    return new String[]{"§7Tokens: §e" + NumberUtils.formatFull(m.getValue())};
                case RANKS:
                    return new String[]{"§7Rank: §7[" + RankupManager.getRankDisplayName(m.getValue().intValue()) + "§7]"};
                case PICKAXE_LEVEL:
                    return new String[]{"§7Pickaxe Level: §e" + m.getValue().toString()};
                case PLAYTIME:
                    return new String[]{"§7Play time: §e" + DateUtils.readableDate(m.getValue().divide(BigMath.THOUSAND))};
            }
            return new String[]{"§cInvalid Type"};
        }
    }
}
