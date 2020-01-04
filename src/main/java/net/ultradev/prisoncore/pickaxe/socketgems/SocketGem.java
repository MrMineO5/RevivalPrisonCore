/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import lombok.Getter;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SocketGem {
    @Getter
    private SocketGemType type;
    @Getter
    private SocketGemTier tier;
    @Getter
    private double percent;
    @Getter
    private SocketGemColor color;

    public SocketGem(SocketGemType type, SocketGemTier tier, double percent) {
        this(type, tier, percent, SocketGemColor.Companion.random());
    }

    private SocketGem(SocketGemType type, SocketGemTier tier, double percent, SocketGemColor color) {
        this.type = type;
        this.tier = tier;
        this.percent = percent;
        this.color = color;
    }

    public static SocketGem deserialize(String str) {
        if (str == null) {
            return null;
        }
        String[] ds = str.split("\\|\\|");
        if (ds.length < 3 || !ds[0].equals("SocketGem")) {
            Debugger.error("Invalid data passed to SocketGem deserializer", "SocketGem");
            return null;
        }
        SocketGemType type;
        try {
            type = SocketGemType.valueOf(ds[1]);
        } catch (IllegalArgumentException ex) {
            Debugger.error("Invalid SocketGemType passed to SocketGem deserializer", "SocketGem");
            return null;
        }
        SocketGemTier tier;
        try {
            tier = SocketGemTier.valueOf(ds[2]);
        } catch (IllegalArgumentException ex) {
            Debugger.error("Invalid SocketGemTier passed to SocketGem deserializer", "SocketGem");
            return null;
        }
        double percent;
        try {
            percent = Double.parseDouble(ds[3]);
        } catch (NumberFormatException e) {
            Debugger.error("Invalid percent passed to SocketGem deserializer", "SocketGem");
            return null;
        }
        if (ds.length == 4) {
            return new SocketGem(type, tier, percent);
        }
        SocketGemColor color = SocketGemColor.Companion.deserialize(ds[4]);
        if (color == null) {
            Debugger.error("Invalid SocketGemColor passed to SocketGem deserializer", "SocketGem");
            return null;
        }
        return new SocketGem(type, tier, percent, color);
    }

    public static boolean isSocketGem(ItemStack item) {
        if (!NBTUtils.hasTag(item, "type")) {
            return false;
        }
        return NBTUtils.getString(item, "type").equals("socketGem");
    }

    public static SocketGem fromItem(ItemStack item) {
        assert isSocketGem(item);
        return deserialize(NBTUtils.getString(item, "socketgem"));
    }

    public ItemStack getItem() {
        ItemStack item = new ItemFactory(Material.FIREWORK_CHARGE)
                .setName(tier.getTierColor() + type.getName())
                .setLore(type.getLore(percent, tier))
                .create();
        ItemMeta meta = item.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect fireworkEffect = FireworkEffect.builder()
                .withColor(color.toColor())
                .build();
        metaFw.setEffect(fireworkEffect);
        item.setItemMeta(metaFw);
        item = NBTUtils.setString(item, "type", "socketGem");
        item = NBTUtils.setString(item, "socketgem", serialize());
        return item;
    }

    public String serialize() {
        return "SocketGem||" + type.name() + "||" + tier.name() + "||" + percent + "||" + color.toString();
    }
}
