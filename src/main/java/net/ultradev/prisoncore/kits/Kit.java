/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.kits;

import lombok.Getter;
import net.ultradev.prisoncore.commands.beta.RequiredRank;
import net.ultradev.prisoncore.utils.items.InvUtils;
import net.ultradev.prisoncore.utils.time.CooldownUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {
    @Getter
    private String name;
    @Getter
    private String displayname;
    @Getter
    private Material displayitem;
    @Getter
    private List<ItemStack> rewards;
    private int cooldown;
    @Getter
    private RequiredRank rank;

    public Kit(String name, String displayname, Material displayitem, List<ItemStack> rewards, RequiredRank rank) {
        this(name, displayname, displayitem, rewards, 3 * 24 * 60 * 60, rank);
    }

    public Kit(String name, String displayname, Material displayitem, List<ItemStack> rewards) {
        this(name, displayname, displayitem, rewards, 3 * 24 * 60 * 60);
    }

    public Kit(String name, String displayname, Material displayitem, List<ItemStack> rewards, int cooldown) {
        this(name, displayname, displayitem, rewards, cooldown, RequiredRank.NONE);
    }

    public Kit(String name, String displayname, Material displayitem, List<ItemStack> rewards, int cooldown, RequiredRank rank) {
        this.name = name;
        this.displayname = displayname;
        this.displayitem = displayitem;
        this.rewards = rewards;
        this.cooldown = cooldown;
        this.rank = rank;
    }

    public boolean hasPermission(Player player) {
        return rank.hasRank(player);
    }

    public boolean isCooldown(Player player) {
        return CooldownUtils.isCooldown(player, "kits_" + name);
    }

    public long getCooldown(Player player) {
        return CooldownUtils.getCooldownTime(player, "kits_" + name);
    }

    public void setCooldown(Player player) {
        CooldownUtils.setCooldown(player, "kits_" + name, cooldown);
    }

    public void use(Player player) {
        setCooldown(player);
        for (ItemStack item : rewards) {
            InvUtils.giveItemMailbox(player, item);
        }
    }
}
