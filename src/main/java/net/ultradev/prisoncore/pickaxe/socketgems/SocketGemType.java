/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.pickaxe.socketgems;

import lombok.Getter;
import net.ultradev.prisoncore.utils.math.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum SocketGemType {
    MERCHANT(1, 6),
    FORTUNE(5, 100),
    CHARITY(5, 100),
    FAVORED(5, 100),
    EXPLOSIVE(5, 100),
    OBLITERATE(5, 100),
    SELL_MULTI(2, 50),
    LIGHTNING(2, 50),
    LUCKY_MINE(5, 100),
    LUCKY_RARE(5, 100),
    LUCKY_LEGENDARY(5, 100),
    // MAGNETIC(2, 50),
    EPIC_CATACLYSM(5, 100),
    EPIC_LIGHTNING(2, 50),
    EPIC_LUCKY(2, 10),
    EPIC_LASER(2, 50),
    EPIC_DETONATION(2, 50);

    @Getter
    private double minPercent;
    @Getter
    private double maxPercent;

    SocketGemType(double minPercent, double maxPercent) {
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
    }

    public static SocketGemType random(boolean isEpic) {
        List<SocketGemType> valids = new ArrayList<>();
        if (isEpic) {
            valids.add(SocketGemType.EPIC_CATACLYSM);
            valids.add(SocketGemType.EPIC_DETONATION);
            valids.add(SocketGemType.EPIC_LASER);
            valids.add(SocketGemType.EPIC_LIGHTNING);
            valids.add(SocketGemType.EPIC_LUCKY);
        } else {
            valids.add(SocketGemType.EXPLOSIVE);
            valids.add(SocketGemType.FORTUNE);
            valids.add(SocketGemType.OBLITERATE);
            valids.add(SocketGemType.LUCKY_MINE);
            valids.add(SocketGemType.LUCKY_RARE);
            valids.add(SocketGemType.LUCKY_LEGENDARY);
            valids.add(SocketGemType.CHARITY);
            valids.add(SocketGemType.MERCHANT);
            valids.add(SocketGemType.FAVORED);
            valids.add(SocketGemType.LIGHTNING);
            valids.add(SocketGemType.SELL_MULTI);
        }
        return valids.get(MathUtils.random(0, valids.size() - 1));
    }

    @NotNull
    @Contract(pure = true)
    public String getName() {
        switch (this) {
            case MERCHANT:
                return "Merchant+";
            case FORTUNE:
                return "Fortune+";
            case CHARITY:
                return "Charity+";
			/*case UNITY:
				return "Unity+";*/
            case FAVORED:
                return "Favored+";
            case EXPLOSIVE:
                return "Explosive+";
            case OBLITERATE:
                return "Obliterate+";
            case SELL_MULTI:
                return "Sell Multi+";
            case LIGHTNING:
                return "Lightning+";
            case LUCKY_MINE:
                return "Lucky+ (Mine Keys)";
            case LUCKY_RARE:
                return "Lucky+ (Rare Keys)";
            case LUCKY_LEGENDARY:
                return "Lucky+ (Legendary Keys)";
            case EPIC_CATACLYSM:
                return "Cataclysm++";
            case EPIC_LIGHTNING:
                return "Lightning++";
            case EPIC_LUCKY:
                return "Lucky++ (Mythical Keys)";
            case EPIC_LASER:
                return "Lazer++";
            case EPIC_DETONATION:
                return "Detonation++";
        }
        return "§cError: Report to UltraDev with the following: " + name() + "-N";
    }

    @NotNull
    public List<String> getDescription(double percent) {
        List<String> ret = new ArrayList<>();
        switch (this) {
            case MERCHANT:
                ret.add("§7Merchant has a §e" + percent + "%§7 chance to double");
                ret.add("§7the tokens from a sell");
                break;
            case FORTUNE:
                ret.add("§7Fortune has a §e" + percent + "%§7 chance give an");
                ret.add("§7additional block from mining");
                break;
            case CHARITY:
                ret.add("§7Charity has a §e" + percent + "%§7 chance to increase");
                ret.add("§7the amount of tokens given");
                break;
            case FAVORED:
                ret.add("§7Favored has a §e" + percent + "%§7 chance to increase");
                ret.add("§7rewards from lucky blocks");
                break;
            case EXPLOSIVE:
                ret.add("§7Explosive has a §e" + percent + "%§7 chance to break");
                ret.add("§7additional blocks");
                break;
            case OBLITERATE:
                ret.add("§7Obliterate has a §e" + percent + "%§7 chance to have");
                ret.add("§7a bigger explosion");
                break;
            case SELL_MULTI:
                ret.add("§7Increases your sell multiplier by §e" + percent + "%");
                break;
            case LIGHTNING:
                ret.add("§7Lightning has a §e" + percent + "%§7 chance to destroy");
                ret.add("§7a larger radius");
                break;
            case LUCKY_MINE:
                ret.add("§7Lucky has a §e" + percent + "%§7 higher");
                ret.add("§7chance of finding Mine Keys");
                break;
            case LUCKY_RARE:
                ret.add("§7Lucky has a §e" + percent + "%§7 higher");
                ret.add("§7chance of finding Rare Keys");
                break;
            case LUCKY_LEGENDARY:
                ret.add("§7Lucky has a §e" + percent + "% §7higher chance");
                ret.add("§7of finding Legendary Keys");
                break;
            case EPIC_CATACLYSM:
                ret.add("§7Cataclysm has a §e" + percent + "%§7 chance to");
                ret.add("§7summon additional balls of fire");
                break;
            case EPIC_LIGHTNING:
                ret.add("§7Lightning has a §e" + percent + "%§7 chance to");
                ret.add("§7summon two more lightning bolts");
                break;
            case EPIC_LUCKY:
                ret.add("§7Lucky has a §e" + percent + "%§7 chance");
                ret.add("§7to reward a MythicalCrate Key upon");
                ret.add("§7finding a Legendary Crate Key");
                break;
            case EPIC_LASER:
                ret.add("§7Laser has a §e" + percent + "%§7 chance to shoot");
                ret.add("§7another laser in the opposite direction");
                break;
            case EPIC_DETONATION:
                ret.add("§7Detonation has a §e" + percent + "%§7 chance to");
                ret.add("§7turn a larger area into magma");
                break;
            default:
                ret.add("§cError: Report to UltraDev with the following: " + name() + "-D");
        }
        return ret;
    }

    @NotNull
    public List<String> getLore(double percent, @NotNull SocketGemTier tier) {
        List<String> lore = new ArrayList<>(getDescription(percent));
        lore.add("");
        lore.add("§eTier: " + tier.getDisplayName());
        return lore;
    }

    public double getTotal(@NotNull List<Double> doubles) {
        if (doubles.isEmpty()) {
            return 0.0;
        }
        return doubles.stream().mapToDouble(Double::doubleValue).sum();
    }
}
