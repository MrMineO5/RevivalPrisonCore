package net.ultradev.prisoncore.utils.gui.guis.pickaxe;

import net.ultradev.prisoncore.enchants.CustomEnchant;
import net.ultradev.prisoncore.enchants.EnchantInfo;
import net.ultradev.prisoncore.pickaxe.Pickaxe;
import net.ultradev.prisoncore.pickaxe.PickaxeUtils;
import net.ultradev.prisoncore.utils.gui.GUIClickType;
import net.ultradev.prisoncore.utils.gui.GUIUtils;
import net.ultradev.prisoncore.utils.gui.guis.GUI;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PickaxeGUI implements GUI
{
    private ItemStack generateBuyItem(final Enchantment enchant, final Pickaxe pick, final int picklevel, final int page) {
        return this.generateBuyItem(enchant, pick.getEnchantmentLevel(enchant), picklevel, page);
    }

    private ItemStack generateBuyItem(final Enchantment enchant, final int level, final int picklevel, final int page) {
        return this.generateBuyItem(enchant, level, picklevel, page, EnchantInfo.getMaxLevel(enchant) != 0);
    }

    private ItemStack generateBuyItem(final Enchantment enchant, final int level, final int picklevel, final int page, final boolean def) {
        if (def) {
            return this.generateBuyItemDef(enchant, level, picklevel, page);
        }
        return this.generateBuyItemScr(enchant, level);
    }

    private ItemStack generateBuyItemDef(final Enchantment enchant, final int level, final int pickLevel, final int page) {
        final List<String> lore = new ArrayList<String>(EnchantInfo.getEnchantDescription(enchant));
        lore.add("§7");
        lore.add("§eCurrent Level: §f" + level);
        lore.add("§eMax Level: §f" + EnchantInfo.getMaxLevel(enchant));
        lore.add("§eMax Scrolls: §f" + EnchantInfo.getMaxScrolls(enchant));
        lore.add("§7");
        lore.add("§6Cost: §e" + EnchantInfo.getPriceString(enchant, level));
        if (EnchantInfo.getMaxLevel(enchant, pickLevel) > level) {
            lore.add("§7");
            lore.add("§eLeft Click§7 +1");
            lore.add("§eRight Click§7 +10");
            lore.add("§eMiddle Click§7 +50");
        }
        else {
            final int reqLevel = Math.floorDiv(pickLevel, 5) * 5 + 5;
            lore.add("§7Requires pickaxe level " + reqLevel);
        }
        final String[][] leftScript = { { "pickaxe:upgrade", enchant.getName(), "1" }, { "inv:open", "pickaxe", Integer.toString(page) } };
        final String[][] rightScript = { { "pickaxe:upgrade", enchant.getName(), "10" }, { "inv:open", "pickaxe", Integer.toString(page) } };
        final String[][] middleScript = { { "pickaxe:upgrade", enchant.getName(), "50" }, { "inv:open", "pickaxe", Integer.toString(page) } };
        ItemFactory fac = new ItemFactory(Material.BOOK).setName(EnchantInfo.getEnchantName(enchant)).setLore(lore);
        if (EnchantInfo.getMaxLevel(enchant, pickLevel) > level) {
            fac = fac.setClickEvent(GUIClickType.LEFTCLICK, (level + 1 <= EnchantInfo.getMaxLevel(enchant)) ? leftScript : GUIUtils.noop).setClickEvent(GUIClickType.RIGHTCLICK, (level + 10 <= EnchantInfo.getMaxLevel(enchant)) ? rightScript : GUIUtils.noop).setClickEvent(GUIClickType.MIDDLECLICK, (level + 50 <= EnchantInfo.getMaxLevel(enchant)) ? middleScript : GUIUtils.noop);
        }
        return fac.setClickEvent(GUIUtils.noop).create();
    }

    private ItemStack generateBuyItemScr(final Enchantment enchant, final int level) {
        final List<String> lore = new ArrayList<String>(EnchantInfo.getEnchantDescription(enchant));
        lore.add("§7");
        lore.add("§eCurrent Level: §f" + level);
        lore.add("§eMax Scrolls: §f" + EnchantInfo.getMaxScrolls(enchant));
        return new ItemFactory(Material.ENCHANTED_BOOK).setName(EnchantInfo.getEnchantName(enchant)).setLore(lore).setClickEvent(GUIUtils.noop).create();
    }

    @Override
    public Inventory generateGUI(final Player player, @NotNull final String... args) {
        int page = 0;
        if (args.length == 1) {
            page = Integer.parseInt(args[0]);
        }
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§7        §4§lPickaxe Upgrades");
        final ItemStack pick = player.getInventory().getItem(0);
        final Pickaxe pickaxe = new Pickaxe(player);
        final ItemStack pickItem = GUIUtils.addClickEvent(pick, GUIUtils.noop);
        final String[][] repairScript = { { "pickaxe:repair" }, { "inv:close" } };
        final ItemStack repair = new ItemFactory(Material.ANVIL).setName("§aRepair").setLore("§7Click to fully repair", "§7your pickaxe!", "", "§6Cost: §e" + PickaxeUtils.getRepairPrice(player) + " Tokens").setClickEvent(repairScript).create();
        final String[][] socketsScript = { { "inv:open", "pickaxe_sockets" } };
        final ItemStack sockets = new ItemFactory(Material.FIREWORK_CHARGE).setName("§6Sockets").setLore("§7Click to modify your", "§7pickaxe socket gems!").setClickEvent(socketsScript).create();
        inv.setItem(10, pickItem);
        inv.setItem(28, repair);
        inv.setItem(37, sockets);
        final String[][] nextArrowScript = { { "inv:open", "pickaxe", Integer.toString(page + 1) } };
        final ItemStack nextArrow = new ItemFactory(Material.ARROW).setName("§bNext Page").setClickEvent(nextArrowScript).create();
        final String[][] prevArrowScript = { { "inv:open", "pickaxe", Integer.toString(page - 1) } };
        final ItemStack prevArrow = new ItemFactory(Material.ARROW).setName("§bPrevious Page").setClickEvent(prevArrowScript).create();
        final int pickLevel = NBTUtils.getInt(pick, "level");
        if (page == 0) {
            inv.setItem(12, this.generateBuyItem(Enchantment.DIG_SPEED, pickaxe, pickLevel, page));
            inv.setItem(13, this.generateBuyItem(Enchantment.LOOT_BONUS_BLOCKS, pickaxe, pickLevel, page));
            inv.setItem(14, this.generateBuyItem(Enchantment.DURABILITY, pickaxe, pickLevel, page));
            inv.setItem(15, this.generateBuyItem(CustomEnchant.LUCKY, pickaxe, pickLevel, page));
            inv.setItem(16, this.generateBuyItem(CustomEnchant.TOKENGREED, pickaxe, pickLevel, page));
            inv.setItem(21, this.generateBuyItem(CustomEnchant.NIGHTVISION, pickaxe, pickLevel, page));
            inv.setItem(22, this.generateBuyItem(CustomEnchant.SPEED, pickaxe, pickLevel, page));
            inv.setItem(23, this.generateBuyItem(CustomEnchant.JUMPBOOST, pickaxe, pickLevel, page));
            inv.setItem(24, this.generateBuyItem(CustomEnchant.SATURATED, pickaxe, pickLevel, page));
            inv.setItem(25, this.generateBuyItem(CustomEnchant.EXPLOSIVE, pickaxe, pickLevel, page));
            inv.setItem(30, this.generateBuyItem(CustomEnchant.OBLITERATE, pickaxe, pickLevel, page));
            inv.setItem(31, this.generateBuyItem(CustomEnchant.LIGHTNING, pickaxe, pickLevel, page));
            inv.setItem(32, this.generateBuyItem(CustomEnchant.FAVORED, pickaxe, pickLevel, page));
            inv.setItem(33, this.generateBuyItem(CustomEnchant.MERCHANT, pickaxe, pickLevel, page));
            inv.setItem(34, this.generateBuyItem(CustomEnchant.SURGE, pickaxe, pickLevel, page));
            inv.setItem(43, nextArrow);
        }
        if (page == 1) {
            inv.setItem(13, this.generateBuyItem(CustomEnchant.ERUPTION, pickaxe, pickLevel, page));
            inv.setItem(14, this.generateBuyItem(CustomEnchant.CLUSTER, pickaxe, pickLevel, page));
            inv.setItem(15, this.generateBuyItem(CustomEnchant.ADEPT, pickaxe, pickLevel, page));
            inv.setItem(16, this.generateBuyItem(CustomEnchant.CHARITY, pickaxe, pickLevel, page));
            inv.setItem(22, this.generateBuyItem(CustomEnchant.MAGNETIC, pickaxe, pickLevel, page));
            inv.setItem(23, this.generateBuyItem(CustomEnchant.MYSTIC, pickaxe, pickLevel, page));
            inv.setItem(24, this.generateBuyItem(CustomEnchant.UNBREAKABLE, pickaxe, pickLevel, page));
            inv.setItem(25, this.generateBuyItem(CustomEnchant.TRANSCENDENTAL, pickaxe, pickLevel, page));
            inv.setItem(30, this.generateBuyItem(CustomEnchant.CATACLYSM, pickaxe, pickLevel, page));
            inv.setItem(31, this.generateBuyItem(CustomEnchant.LASER, pickaxe, pickLevel, page));
            inv.setItem(32, this.generateBuyItem(CustomEnchant.DETONATION, pickaxe, pickLevel, page));
            inv.setItem(33, this.generateBuyItem(CustomEnchant.VORTEX, pickaxe, pickLevel, page));
            inv.setItem(39, prevArrow);
        }
        inv = InvUtils.fillEmpty(inv, GUIUtils.getFiller((short)3));
        return inv;
    }
}
