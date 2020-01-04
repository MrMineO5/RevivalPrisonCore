package net.ultradev.prisoncore.multipliers;

import lombok.Getter;
import net.ultradev.prisoncore.utils.items.ItemFactory;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.items.NBTUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

public class Multiplier {
    private MultiplierType type;
    @Getter
    private long length;
    private long startTime;
    @Getter
    private double multi;
    private BossBar<?> bar;

    @NotNull
    @Contract("_ -> new")
    public static Multiplier parseFromItem(final ItemStack item) {
        assert isMultiplier(item);
        final MultiplierType type = MultiplierType.valueOf(NBTUtils.getString(item, "multi_type"));
        final long length = NBTUtils.getLong(item, "multi_length");
        final double multi = NBTUtils.getDouble(item, "multi_multi");
        return new Multiplier(type, length, multi);
    }

    public static boolean isMultiplier(final ItemStack item) {
        return ItemUtils.isType(item, "multiplier");
    }

    public Multiplier(@NotNull final MultiplierType type, final long length, final double multi) {
        this.type = type;
        this.length = length;
        this.multi = multi;
        this.startTime = -1L;
        final ViaAPI<?> api = (ViaAPI<?>) Via.getAPI();
        this.bar = (BossBar<?>) api.createBossBar("Multiplier", type.getColor(), BossStyle.SOLID);
    }

    public void start() {
        this.startTime = DateUtils.getMilliTimeStamp();
    }

    public void pause() {
        this.length = this.getRemainingTime();
        this.startTime = -1L;
    }

    public boolean isActive() {
        return this.getRemainingTime() >= 0L;
    }

    public String getName() {
        switch (this.type) {
            case MONEY: {
                return "§6" + DateUtils.convertTime(this.getRemainingTime()) + " " + this.multi + "x§7 sell multiplier";
            }
            case PICK_XP: {
                return "§6" + DateUtils.convertTime(this.getRemainingTime()) + " " + this.multi + "x§7 xp multiplier";
            }
            default: {
                return null;
            }
        }
    }

    public String getBossBar() {
        switch (this.type) {
            case MONEY: {
                return "§7" + this.multi + "x Sell Multiplier: §e" + this.getRemainingTimeStr();
            }
            case PICK_XP: {
                return "§7" + this.multi + "x Pickaxe Exp Multiplier: §e" + this.getRemainingTimeStr();
            }
            default: {
                return null;
            }
        }
    }

    public String getActivationMessage() {
        String str;
        switch (this.type) {
            case MONEY: {
                str = "sell";
                break;
            }
            case PICK_XP: {
                str = "pickaxe xp";
                break;
            }
            case AM_KEY: {
                str = "Auto Miner Key";
                break;
            }
            case AM_TOKEN: {
                str = "Auto Miner Token";
                break;
            }
            default: {
                str = "Unknown";
                break;
            }
        }
        return "§7Activated a §e" + this.multi + "x§7 " + str + " multiplier for §e" + this.getRemainingTimeStr();
    }

    public long getRemainingTime() {
        if (this.startTime == -1L) {
            return this.length;
        }
        final long endTime = this.startTime + this.length;
        final long currentTime = DateUtils.getMilliTimeStamp();
        return endTime - currentTime;
    }

    public float getPercentRemaining() {
        final float f = this.getRemainingTime() / (float) this.length;
        Debugger.log("Remaining percentage: " + f, "multiplier");
        return f;
    }

    public String getRemainingTimeStr() {
        return DateUtils.convertTime(this.getRemainingTime() / 1000L);
    }

    public ItemStack getItem() {
        ItemStack item = null;
        switch (this.type) {
            case MONEY: {
                item = new ItemFactory(Material.PAPER)
                        .setName("§eSell Multiplier")
                        .setLore(
                                "§7Right click to boost",
                                "§7your sell multiplier!",
                                "§7",
                                "§7Duration: §e" + DateUtils.convertTime(this.length / 1000L),
                                "§7Multiplier: §ex" + this.multi
                        )
                        .create();
                break;
            }
            case PICK_XP: {
                item = new ItemFactory(Material.INK_SACK, 1, 4)
                        .setName("§bPickaxe XP Booster")
                        .setLore(
                                "§7Right click to boost", "§7Your Pickaxe experience", "", "§7Duration: §e" + DateUtils.convertTime(this.length / 1000L), "§7Multiplier: §e" + this.multi + "x").create();
                break;
            }
            case AM_KEY: {
                item = new ItemFactory(Material.REDSTONE).setName("§aAuto Miner Key Multiplier").setLore("§7Right click to boost", "§7your Auto Miner's", "§7key multiplier!", "§7", "§7Duration: §e" + DateUtils.convertTime(this.length / 1000L), "§7Multiplier: §ex" + this.multi).create();
                break;
            }
            case AM_TOKEN: {
                item = new ItemFactory(Material.GLOWSTONE_DUST).setName("§aAuto Miner Token Multiplier").setLore("§7Right click to boost", "§7your Auto Miner's", "§7token multiplier!", "§7", "§7Duration: §e" + DateUtils.convertTime(this.length / 1000L), "§7Multiplier: §ex" + this.multi).create();
                break;
            }
        }
        item = NBTUtils.setString(item, "type", "multiplier");
        item = NBTUtils.setString(item, "multi_type", this.type.toString());
        item = NBTUtils.setLong(item, "multi_length", this.length);
        item = NBTUtils.setDouble(item, "multi_multi", this.multi);
        return item;
    }

    public MultiplierType getType() {
        return this.type;
    }

    public void render(@NotNull final Player player) {
        if (!this.type.hasBossBar()) {
            return;
        }
        if (this.bar == null) {
            final ViaAPI<?> api = (ViaAPI<?>) Via.getAPI();
            this.bar = (BossBar<?>) api.createBossBar("Multiplier", this.type.getColor(), BossStyle.SOLID);
        }
        if (this.bar.getPlayers() == null || !this.bar.getPlayers().contains(player.getUniqueId())) {
            this.bar.addPlayer(player.getUniqueId());
        }
        this.bar.show();
        this.bar.setTitle(this.getBossBar());
        this.bar.setHealth(this.getPercentRemaining());
    }

    public void unrender(@NotNull final Player player) {
        if (this.bar.getPlayers().contains(player.getUniqueId())) {
            this.bar.removePlayer(player.getUniqueId());
        }
        if (this.bar.getPlayers().isEmpty()) {
            this.bar.hide();
        }
    }

    public String expiryMessage() {
        switch (this.type) {
            case MONEY: {
                return "§7Your sell multiplier has expired.";
            }
            case PICK_XP: {
                return "§7Your xp multiplier has expired.";
            }
            case AM_KEY: {
                return "§7Your Auto Miner Key multiplier has expired.";
            }
            case AM_TOKEN: {
                return "§7Your Auto Miner Token multiplier has expired.";
            }
            default: {
                return "§cError: Unknown message";
            }
        }
    }

    public void expire(@NotNull final Player player) {
        player.sendMessage(this.expiryMessage());
        this.unrender(player);
    }

    public void addTime(long time) {
        this.length += time;
    }

    public Multiplier(@NotNull final ConfigurationSection sec) {
        this.type = MultiplierType.valueOf(sec.getString("type"));
        this.length = sec.getLong("length");
        this.multi = sec.getDouble("multi");
        this.startTime = -1L;
    }

    public void serialize(@NotNull final ConfigurationSection sec) {
        sec.set("type", this.type.toString());
        sec.set("length", this.getRemainingTime());
        sec.set("multi", this.multi);
    }

    public enum MultiplierType {
        MONEY,
        PICK_XP,
        AM_KEY,
        AM_TOKEN;

        @Contract(pure = true)
        public BossColor getColor() {
            switch (this) {
                case MONEY: {
                    return BossColor.WHITE;
                }
                case PICK_XP: {
                    return BossColor.BLUE;
                }
                default: {
                    return BossColor.PURPLE;
                }
            }
        }

        public boolean hasBossBar() {
            switch (this) {
                case MONEY:
                case PICK_XP: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }
}