/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.breakblock;

import lombok.Data;
import net.ultradev.prisoncore.pets.PetType;
import net.ultradev.prisoncore.pickaxe.socketgems.SocketGemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

@Data
public class BreakBlockParameters {
    private Map<Enchantment, Integer> enchants;
    private Map<SocketGemType, Double> sockets;
    private Location loc;
    private Material mat;
    private byte data;
    private BlockBreaker breaker;
    private boolean canBreakSLBlocks;
    private boolean key_message;
    private boolean lb_message;
    private Map<PetType, Integer> pets;

    public BreakBlockParameters(Map<Enchantment, Integer> enchants, Map<SocketGemType, Double> sockets, Location loc, Material mat, byte data, BlockBreaker breaker, boolean canBreakSLBlocks, boolean key_message, boolean lb_message, Map<PetType, Integer> pets) {
        this.enchants = enchants;
        this.sockets = sockets;
        this.loc = loc;
        this.mat = mat;
        this.data = data;
        this.breaker = breaker;
        this.canBreakSLBlocks = canBreakSLBlocks;
        this.key_message = key_message;
        this.lb_message = lb_message;
        this.pets = pets;
    }
}