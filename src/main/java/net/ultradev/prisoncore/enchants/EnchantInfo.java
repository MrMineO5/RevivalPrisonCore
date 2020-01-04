/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.enchants;

import net.ultradev.prisoncore.Main;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.playerdata.Economy;
import net.ultradev.prisoncore.utils.math.NumberUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantInfo {
    public static Map<Enchantment, EnchantmentInfo> enchants = new HashMap<>();
    private static HashMap<Integer, Map<Enchantment, Integer>> levelMap = new HashMap<>();

    static {
        Map<Enchantment, Integer> level0 = new HashMap<>();
        level0.put(Enchantment.DIG_SPEED, 250);
        level0.put(Enchantment.LOOT_BONUS_BLOCKS, 150);
        level0.put(Enchantment.DURABILITY, 250);
        level0.put(CustomEnchant.LUCKY, 75);
        level0.put(CustomEnchant.TOKENGREED, 175);
        level0.put(CustomEnchant.SPEED, 0);
        level0.put(CustomEnchant.JUMPBOOST, 0);
        level0.put(CustomEnchant.EXPLOSIVE, 25);
        level0.put(CustomEnchant.OBLITERATE, 3);
        level0.put(CustomEnchant.CLUSTER, 0);
        level0.put(CustomEnchant.ADEPT, 30);
        level0.put(CustomEnchant.LIGHTNING, 15);
        level0.put(CustomEnchant.FAVORED, 150);
        level0.put(CustomEnchant.MERCHANT, 150);
        level0.put(CustomEnchant.SURGE, 2);
        level0.put(CustomEnchant.ERUPTION, 25);
        level0.put(CustomEnchant.CHARITY, 75);
        level0.put(CustomEnchant.MAGNETIC, 100);
        level0.put(CustomEnchant.MYSTIC, 100);
        levelMap.put(0, level0);

        Map<Enchantment, Integer> level5 = new HashMap<>();
        level5.put(Enchantment.DIG_SPEED, 300);
        level5.put(Enchantment.LOOT_BONUS_BLOCKS, 200);
        level5.put(Enchantment.DURABILITY, 300);
        level5.put(CustomEnchant.LUCKY, 100);
        level5.put(CustomEnchant.TOKENGREED, 250);
        level5.put(CustomEnchant.SPEED, 1);
        level5.put(CustomEnchant.JUMPBOOST, 1);
        level5.put(CustomEnchant.EXPLOSIVE, 50);
        level5.put(CustomEnchant.OBLITERATE, 5);
        level5.put(CustomEnchant.CLUSTER, 1);
        level5.put(CustomEnchant.ADEPT, 50);
        level5.put(CustomEnchant.LIGHTNING, 20);
        level5.put(CustomEnchant.FAVORED, 200);
        level5.put(CustomEnchant.MERCHANT, 200);
        level5.put(CustomEnchant.SURGE, 3);
        level5.put(CustomEnchant.ERUPTION, 50);
        level5.put(CustomEnchant.CHARITY, 125);
        level5.put(CustomEnchant.MAGNETIC, 150);
        level5.put(CustomEnchant.MYSTIC, 150);
        levelMap.put(5, level5);

        Map<Enchantment, Integer> level10 = new HashMap<>();
        level10.put(Enchantment.DIG_SPEED, 1250);
        level10.put(Enchantment.LOOT_BONUS_BLOCKS, 600);
        level10.put(Enchantment.DURABILITY, 1250);
        level10.put(CustomEnchant.LUCKY, 400);
        level10.put(CustomEnchant.TOKENGREED, 600);
        level10.put(CustomEnchant.SPEED, 2);
        level10.put(CustomEnchant.JUMPBOOST, 2);
        level10.put(CustomEnchant.EXPLOSIVE, 75);
        level10.put(CustomEnchant.OBLITERATE, 15);
        level10.put(CustomEnchant.CLUSTER, 3);
        level10.put(CustomEnchant.ADEPT, 100);
        level10.put(CustomEnchant.LIGHTNING, 60);
        level10.put(CustomEnchant.FAVORED, 400);
        level10.put(CustomEnchant.MERCHANT, 500);
        level10.put(CustomEnchant.SURGE, 15);
        level10.put(CustomEnchant.ERUPTION, 80);
        level10.put(CustomEnchant.MAGNETIC, 300);
        level10.put(CustomEnchant.MYSTIC, 300);
        levelMap.put(10, level10);

        Map<Enchantment, Integer> level15 = new HashMap<>();
        level15.put(Enchantment.LOOT_BONUS_BLOCKS, 900);
        level15.put(CustomEnchant.LUCKY, 700);
        level15.put(CustomEnchant.TOKENGREED, 800);
        level15.put(CustomEnchant.SPEED, 3);
        level15.put(CustomEnchant.JUMPBOOST, 3);
        level15.put(CustomEnchant.EXPLOSIVE, 100);
        level15.put(CustomEnchant.OBLITERATE, 20);
        level15.put(CustomEnchant.CLUSTER, 5);
        level15.put(CustomEnchant.ADEPT, 125);
        level15.put(CustomEnchant.LIGHTNING, 90);
        level15.put(CustomEnchant.FAVORED, 450);
        level15.put(CustomEnchant.MERCHANT, 700);
        level15.put(CustomEnchant.SURGE, 20);
        level15.put(CustomEnchant.ERUPTION, 100);
        level15.put(CustomEnchant.MAGNETIC, 400);
        level15.put(CustomEnchant.MYSTIC, 400);
        levelMap.put(15, level15);

        Map<Enchantment, Integer> level20 = new HashMap<>();
        level20.put(Enchantment.LOOT_BONUS_BLOCKS, 1250);
        level20.put(CustomEnchant.LUCKY, 900);
        level20.put(CustomEnchant.TOKENGREED, 1000);
        level20.put(CustomEnchant.EXPLOSIVE, 125);
        level20.put(CustomEnchant.LIGHTNING, 110);
        level20.put(CustomEnchant.FAVORED, 500);
        level20.put(CustomEnchant.MERCHANT, 800);
        level20.put(CustomEnchant.ERUPTION, 200);
        level20.put(CustomEnchant.MAGNETIC, 450);
        level20.put(CustomEnchant.MYSTIC, 400);
        levelMap.put(20, level20);

        Map<Enchantment, Integer> level25 = new HashMap<>();
        level25.put(CustomEnchant.LUCKY, 1200);
        level25.put(CustomEnchant.TOKENGREED, 1200);
        level25.put(CustomEnchant.LIGHTNING, 120);
        level25.put(CustomEnchant.FAVORED, 600);
        level25.put(CustomEnchant.MERCHANT, 900);
        level25.put(CustomEnchant.ERUPTION, 225);
        level25.put(CustomEnchant.MAGNETIC, 500);
        level25.put(CustomEnchant.MYSTIC, 450);
        levelMap.put(25, level25);

        Map<Enchantment, Integer> level30 = new HashMap<>();
        level30.put(CustomEnchant.MERCHANT, 1000);
        level30.put(CustomEnchant.MYSTIC, 500);
        levelMap.put(30, level30);
    }

    public static void loadEnchants() {
        ConfigurationSection conf = Main.getInstance().getConfig().getConfigurationSection("enchantprices");
        enchants.put(
                Enchantment.DIG_SPEED,
                new EnchantmentInfo(
                        "§7Efficiency",
                        1300,
                        200,
                        new PriceFunction(conf.getConfigurationSection("efficiency")),
                        "§7Increases block break speed"
                )
        );
        enchants.put(
                Enchantment.LOOT_BONUS_BLOCKS,
                new EnchantmentInfo(
                        "§7Fortune",
                        1500,
                        250,
                        new PriceFunction(conf.getConfigurationSection("fortune")),
                        "§7Increased drops from all blocks"
                )
        );
        enchants.put(
                Enchantment.DURABILITY,
                new EnchantmentInfo(
                        "§7Unbreaking",
                        1300,
                        200,
                        new PriceFunction(conf.getConfigurationSection("unbreaking")),
                        "§7Decreases the the rate at which",
                        "§7your pickaxe loses durability"
                )
        );

        enchants.put(
                CustomEnchant.MERCHANT,
                new EnchantmentInfo(
                        "§3Merchant",
                        1100,
                        150,
                        new PriceFunction(conf.getConfigurationSection("merchant")),
                        "§7Increases the amount of Tokens",
                        "§7you get for selling blocks"
                )
        );
        enchants.put(
                CustomEnchant.FAVORED,
                new EnchantmentInfo(
                        "§eFavored",
                        1200,
                        150,
                        new PriceFunction(conf.getConfigurationSection("favored")),
                        "§7Better rewards from all", "§7Lucky Block variants"
                )
        );
        enchants.put(
                CustomEnchant.SURGE,
                new EnchantmentInfo(
                        "§bSurge",
                        20,
                        5,
                        new PriceFunction(conf.getConfigurationSection("surge")),
                        "§7Gain a small multiplier when raining, and a",
                        "§7large multiplier when thundering"
                )
        );
        enchants.put(CustomEnchant.OBLITERATE,
                new EnchantmentInfo("§cObliterate", 20, 10, new PriceFunction(conf.getConfigurationSection("obliterate")),
                        "§7Explosive occasionally destroys a large", "§7area including Lucky Blocks"));
        enchants.put(CustomEnchant.NIGHTVISION, new EnchantmentInfo("§9Night Vision", 1, 0,
                new PriceFunction(conf.getConfigurationSection("nightvision")), "§7Equip your pickaxe to gain a permanent", "§7Night Vision effect"));
        enchants.put(CustomEnchant.JUMPBOOST, new EnchantmentInfo("§aJump Boost", 3, 3,
                new PriceFunction(conf.getConfigurationSection("jumpboost")), "§7Equip your pickaxe to gain a permanent", "§7Jump Boost effect"));
        enchants.put(CustomEnchant.SPEED, new EnchantmentInfo("§bSpeed", 3, 3, new PriceFunction(conf.getConfigurationSection("speed")),
                "§7Equip your pickaxe to gain a permanent", "§7Speed effect"));
        enchants.put(
                CustomEnchant.LIGHTNING,
                new EnchantmentInfo(
                        "§fLightning", 120, 30, new PriceFunction(conf.getConfigurationSection("lightning")),
                        "§7Chance for lightning to strike down from", "§7the clouds and destroys a huge area"));
        enchants.put(
                CustomEnchant.SATURATED,
                new EnchantmentInfo(
                        "§6Saturated", 100, 25, new PriceFunction(conf.getConfigurationSection("saturated")),
                        "§7Mining blocks has a chance to refill", "§7your hunger bar"));
        enchants.put(
                CustomEnchant.EXPLOSIVE,
                new EnchantmentInfo("§cExplosive",
                        250,
                        50,
                        new PriceFunction(conf.getConfigurationSection("explosive")),
                        "§7Chance to break adjacent",
                        "§7blocks when mining"
                )
        );
        enchants.put(
                CustomEnchant.TOKENGREED,
                new EnchantmentInfo("§6Token Greed", 1250, 250,
                        new PriceFunction(conf.getConfigurationSection("tokengreed")), "§7Increased Token drop rate and", "§7quantity from mined blocks"));
        enchants.put(
                CustomEnchant.LUCKY,
                new EnchantmentInfo("§eLucky", 1100, 250, new PriceFunction(conf.getConfigurationSection("lucky")),
                        "§7Find tokens and crate keys", "§7more frequently whilst mining"));
        enchants.put(
                CustomEnchant.ADEPT,
                new EnchantmentInfo("§cAdept", 275, 25, new PriceFunction(conf.getConfigurationSection("adept")),
                        "§7Increases the rate at which your", "§7pickaxe gains experience"));
        /*
         * enchants.put(CustomEnchant.HORDE, new EnchantmentInfo( "§eHorde", 5, 5, new
         * PriceFunction(100, 1.0) "§7Gain a bigger sell multiplier for each",
         * "§7Gang member online" ));
         */
        enchants.put(
                CustomEnchant.CLUSTER,
                new EnchantmentInfo("§aCluster", 5, 5, new PriceFunction(conf.getConfigurationSection("cluster")),
                        "§7Gains a bigger sell multiplier for each player with", "§7this enchant in the same mine as you"));
        enchants.put(CustomEnchant.ERUPTION, new EnchantmentInfo("§6Eruption", 200, 25, new PriceFunction(conf.getConfigurationSection("eruption")),
                "§7Chance for mined blocks to release a huge", "§7shower of Tokens"));
        enchants.put(CustomEnchant.MYSTIC,
                new EnchantmentInfo(
                        "§aMystic",
                        600, 150,
                        new PriceFunction(conf.getConfigurationSection("mystic")),
                        "§7Chance for keys acquired through mining", "§7to be of a higher Crate tier"));
        enchants.put(CustomEnchant.MAGNETIC,
                new EnchantmentInfo(
                        "§bMagnetic",
                        600, 150,
                        new PriceFunction(conf.getConfigurationSection("magnetic")),
                        "§7Chance to gain additional amounts of", "§7keys when mining"));
        /*
         * enchants.put(CustomEnchant.UNION, new EnchantmentInfo( "§3Union", 100, 20,
         * new PriceFunction(100, 1.0), "§7Chance to reward online Gang members",
         * "§7with Tokens when you mine" ));
         */
        enchants.put(
                CustomEnchant.CHARITY,
                new EnchantmentInfo(
                        "§dCharity",
                        150,
                        25,
                        new PriceFunction(conf.getConfigurationSection("charity")),
                        "§7Have a chance to reward everyone online",
                        "§7with Tokens when you mine"
                )
        );

        enchants.put(CustomEnchant.UNBREAKABLE,
                new EnchantmentInfo("§4Unbreakable", 0, 1, new PriceFunction(100, 1.0),
                        "§7Makes your pickaxe impervious to any", "§7and all durability damage", "",
                        "§7§oSource: Mythical Crates"));
        enchants.put(CustomEnchant.DETONATION,
                new EnchantmentInfo("§6Detonation", 0, 10, new PriceFunction(100, 1.0),
                        "§7Blocks broken have a chance to release", "§7a shower of Magma onto nearby blocks", "",
                        "§7§oSource: Scorching Chasm Dunegon"));
        enchants.put(CustomEnchant.VORTEX,
                new EnchantmentInfo("§5Vortex", 0, 10, new PriceFunction(100, 1.0), "§7Teleport higher valued blocks",
                        "§7towards you when mining", "", "§7§oSource: Lost Fortress Dunegon"));
        enchants.put(CustomEnchant.LASER,
                new EnchantmentInfo("§2Laser", 0, 10, new PriceFunction(100, 1.0), "§7Summon a laser to break blocks",
                        "§7within your line of sight", "", "§7§oSource: Ocean Trench Dunegon"));
        enchants.put(CustomEnchant.CATACLYSM,
                new EnchantmentInfo(
                        "§4Cataclysm",
                        0,
                        5,
                        new PriceFunction(100, 1.0),
                        "§7Summon fireballs to rain down and",
                        "§7destroy the mine you're within",
                        "",
                        "§7§oSource: §c§lShady Trader"));
        enchants.put(CustomEnchant.TRANSCENDENTAL,
                new EnchantmentInfo("§cTranscendental", 0, 1, new PriceFunction(100, 1.0),
                        "§7Morph adjacent blocks into those", "§7of a higher selling power", "", "§7§oSource: ???"));
        enchants.put(CustomEnchant.WEIGHT,
                new EnchantmentInfo("§cWeight", 0, 1, new PriceFunction(100, 1.0),
                        "§7Morph adjacent blocks into those", "§7of a higher selling power", "", "§7§oSource: ???"));
    }

    private static EnchantmentInfo getInfo(Enchantment enchant) {
        return enchants.get(enchant);
    }

    public static String getEnchantName(Enchantment enchant) {
        EnchantmentInfo info = getInfo(enchant);
        if (info == null) {
            return "§7Error: Report to Ultra " + enchant.getName();
        } else {
            return info.name;
        }
    }

    public static List<String> getEnchantDescription(Enchantment enchant) {
        EnchantmentInfo info = getInfo(enchant);
        if (info == null) {
            return Arrays.asList("§7Error: Report to Ultra " + enchant.getName());
        } else {
            return Arrays.asList(info.description);
        }
    }

    public static int getMaxLevel(Enchantment enchant) {
        EnchantmentInfo info = getInfo(enchant);
        if (info == null) {
            return -1;
        } else {
            return info.maxLevel;
        }
    }

    public static int getMaxScrolls(Enchantment enchant) {
        EnchantmentInfo info = getInfo(enchant);
        if (info == null) {
            return -1;
        } else {
            return info.maxScrolls;
        }
    }

    public static int getMaxScrollLevel(Enchantment enchant) {
        EnchantmentInfo info = getInfo(enchant);
        if (info == null) {
            return -1;
        } else {
            return info.maxLevel + info.maxScrolls;
        }
    }

    public static double getPrice(String enchant, int level) {
        return getPrice(CustomEnchant.getByName(enchant), level);
    }

    public static long getPrice(Enchantment enchant, int level) {
        return getInfo(enchant).price.getPrice(level);
    }

    public static boolean canAfford(Player player, Enchantment enchant) {
        Pickaxe pick = new Pickaxe(player);
        int level = pick.getEnchantmentLevel(enchant);
        if (level >= EnchantInfo.getMaxLevel(enchant)) {
            return false;
        }
        return (Economy.tokens.hasBalance(player, getPrice(enchant, level)));
    }

    public static boolean canAfford(Player player, String enchant) {
        return canAfford(player, CustomEnchant.getByName(enchant));
    }

    public static String getPriceString(Enchantment enchant, int level) {
        if (level < getMaxLevel(enchant)) {
            return NumberUtils.formatFull(getPrice(enchant, level)) + " Tokens";
        } else {
            return "§eMaxed!";
        }
    }

    public static void runEnchantment(Enchantment enchant, Player player, Block block) {
        int level = new Pickaxe(player).getEnchantmentLevel(enchant);
        if (level <= 0) {
            return;
        }
        getInfo(enchant).run.run(player, block, level);
    }

    public static int getMaxLevel(Enchantment enchant, int pickLevel) {
        if (!levelMap.containsKey(Math.floorDiv(pickLevel, 5) * 5)) {
            return getMaxScrollLevel(enchant);
        }
        return levelMap.get(Math.floorDiv(pickLevel, 5) * 5).getOrDefault(enchant, getMaxScrollLevel(enchant));
    }

    private interface EnchantmentRunnable {
        void run(Player player, Block block, int level);
    }

    private static class PriceFunction {
        private int start;
        private double growth;

        PriceFunction(int start, double growth) {
            this.start = start;
            this.growth = growth;
        }

        PriceFunction(ConfigurationSection section) {
            this.start = section.getInt("start");
            this.growth = section.getDouble("growth");
        }

        long getPrice(int level) {
            return (long) Math.floor((Math.pow((level / growth), 3)) + start);
        }

        long getPriceSum(int level, int am) {
            return 0;
        }
    }

    private static class EnchantmentInfo {
        public String name;
        public int maxLevel;
        public int maxScrolls;
        public PriceFunction price;
        public String[] description;
        public EnchantmentRunnable run;

        private EnchantmentInfo(String name, int maxLevel, int maxScrolls, PriceFunction price, String... description) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.maxScrolls = maxScrolls;
            this.price = price;
            this.description = description;
            this.run = null;
        }

        private EnchantmentInfo(String name, int maxLevel, int maxScrolls, PriceFunction price, EnchantmentRunnable run, String... description) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.maxScrolls = maxScrolls;
            this.price = price;
            this.description = description;
            this.run = run;
        }
    }
}
