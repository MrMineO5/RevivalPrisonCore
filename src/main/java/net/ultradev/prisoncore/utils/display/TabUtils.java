/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.display;

import de.myzelyam.api.vanish.VanishAPI;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TabUtils {
    private static void update(Player player) {
        String header = "\n§7§m-------------------------\n\n§c§lRevival§f§lPrison\n§7Online Players: §e"
                + (Bukkit.getOnlinePlayers().size() - VanishAPI.getInvisiblePlayers().size()) + "\n";
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String footer = "\n§cDiscord: §7discord.io/revivalprison\n§cStore: §7revival-prison.buycraft.net\n§cServer time: §7"
                + time + "\n\n§7§m-------------------------\n";

        IChatBaseComponent tabheader = ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabfooter = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter tablist = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = tablist.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(tablist, tabheader);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = tablist.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(tablist, tabfooter);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tablist);
        }
    }

    public static void updateAll() {
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            update(player);
        }
    }
}
