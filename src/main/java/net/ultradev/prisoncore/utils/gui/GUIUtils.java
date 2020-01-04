package net.ultradev.prisoncore.utils.gui;

import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIUtils {
    public static String[][] noop = {{"noop"}};
    public static ItemStack filler = new ItemFactory(Material.STAINED_GLASS_PANE, 1, (short)15).setName("§7").setClickEvent(GUIUtils.noop).create();
    public static ItemStack error = new ItemFactory(Material.BARRIER).setName("§cError").setLore("§7Please create a ticket stating", "§7how to recreate this error.").setClickEvent(GUIUtils.noop).create();

    public static ItemStack getFiller(final int meta) {
        return getFiller((short)meta);
    }

    public static ItemStack getFiller(final short meta) {
        return new ItemFactory(Material.STAINED_GLASS_PANE, 1, meta).setName("§7").setClickEvent(GUIUtils.noop).create();
    }

    public static ItemStack addClickEvent(final ItemStack item, final String[][] script) {
        final String scr = ItemScriptEncoder.encodeInstructions(script);
        return NBTUtils.setString(item, "itemscript_general", scr);
    }

    public static void runClick(final Player player, final Inventory inv, final InventoryClickEvent event, final ItemStack item) {
        Debugger.log("Running general click...", "gui_click");
        final String scr = NBTUtils.getString(item, "itemscript_general");
        ItemScriptInterpreter.interpretItemScript(scr, player, event, inv);
    }

    public static ItemStack addClickEvent(final ItemStack item, final String[][] script, final GUIClickType clicktype) {
        final String scr = ItemScriptEncoder.encodeInstructions(script);
        return NBTUtils.setString(item, "itemscript_" + clicktype.toString(), scr);
    }

    public static void runClick(final Player player, final Inventory inv, final InventoryClickEvent event, final ItemStack item, final GUIClickType clicktype) {
        Debugger.log("Running " + clicktype.name() + " click...", "gui_click");
        final String scr = NBTUtils.getString(item, "itemscript_" + clicktype.toString());
        ItemScriptInterpreter.interpretItemScript(scr, player, event, inv);
    }
}
