package net.ultradev.prisoncore.utils.plugins;

import net.milkbowl.vault.permission.*;
import net.milkbowl.vault.chat.*;
import org.bukkit.*;
import net.ultradev.prisoncore.*;
import net.milkbowl.vault.economy.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;

public class VaultUtils
{
    private static Permission perms = null;
    private static Chat chat = null;
    private static UltraEconomy econInstance;

    public static void init() {
        if (!setupEconomy()) {
            Bukkit.getLogger().severe("[PrisonCore] Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
            return;
        }
        setupPermissions();
        setupChat();
    }

    public static void deinit() {
        Bukkit.getServicesManager().unregister(VaultUtils.econInstance);
    }

    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        VaultUtils.econInstance = new UltraEconomy();
        final Plugin vault = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        Bukkit.getServicesManager().register(Economy.class, VaultUtils.econInstance, vault, ServicePriority.High);
        return true;
    }

    private static void setupChat() {
        final RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        VaultUtils.chat = rsp.getProvider();
    }

    private static void setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        VaultUtils.perms = rsp.getProvider();
    }

    public static String getPrefix(final Player p) {
        return VaultUtils.chat.getPlayerPrefix(p);
    }
}
