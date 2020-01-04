/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.anomalies;

import net.ultradev.prisoncore.events.AnomalyEvents;
import net.ultradev.prisoncore.mines.Mine;
import net.ultradev.prisoncore.mines.MineManager;
import net.ultradev.prisoncore.rankup.RankupManager;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.betterspigot.EntityMeta;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnomalyManager {
    private static List<EntityType> allowedTypes = new ArrayList<>();

    static {
        allowedTypes.add(EntityType.SKELETON);
        allowedTypes.add(EntityType.BLAZE);
        allowedTypes.add(EntityType.CHICKEN);
        allowedTypes.add(EntityType.COW);
        allowedTypes.add(EntityType.PIG);
        allowedTypes.add(EntityType.PIG_ZOMBIE);
    }

    private static EntityType pickEntityType() {
        return allowedTypes.get(MathUtils.random(0, allowedTypes.size() - 1));
    }

    public static void startTimer(int delay) {
        Scheduler.scheduleSyncRepeatingTask(AnomalyManager::spawnRandomAnomaly, 100, delay);
        Scheduler.scheduleSyncRepeatingTask(AnomalyManager::updateAnomalies, 100, 15);
    }

    private static void updateAnomalies() {
        Debugger.log("Checking for anomaly.", "anomaly_update");
        for (Entity ent : Bukkit.getWorld("mines").getEntities()) {
            if (ent == null) {
                continue;
            }
            if (!ent.hasMetadata("anomalyData")) {
                continue;
            }
            Debugger.log("Anomaly exists. Getting entity.", "anomaly_update");
            String str = ent.getMetadata("anomalyData").get(0).asString();
            if (ent.getTicksLived() > 60 * 20 * 2 &&
                    (!AnomalyEvents.lastDamaged.containsKey(ent.getUniqueId()) ||
                            AnomalyEvents.lastDamaged.get(ent.getUniqueId()) - DateUtils.getMilliTimeStamp() > 60000)) {
                Debugger.log("Anomaly is older than 2 minutes, removing...", "anomaly_update");
                ent.remove();
                continue;
            }
			/*AnomalyType type = AnomalyType.valueOf(str);
			Debugger.log("Regenerating anomaly.", "anomaly_update");
			LivingEntity le = (LivingEntity) ent;
			double newHealth = le.getHealth() + type.getHealth() * 0.2;
			le.setHealth(Math.min(type.getHealth(), newHealth));*/
        }
    }

    /*
     * private static Vector reciprocal(Location loc) { return new Vector(1 /
     * loc.getX(), 1 / loc.getY(), 1 / loc.getZ()); }
     */

    private static void spawnRandomAnomaly() {
        List<String> mines = new ArrayList<>(MineManager.mines.keySet());
        String mine = mines.get(MathUtils.random(0, mines.size() - 1));
        if (RankupManager.getIdOf(mine) == -1) {
            spawnRandomAnomaly();
            return;
        }
        spawnAnomaly(mine);
    }

    @NotNull
    @Contract("_ -> new")
    private static Location getRandomLocation(String mine) {
        Mine m = MineManager.getMine(mine);
        assert m != null;
        Location loc = m.getTpPos();
        int spawnX = loc.getBlockX();
        int spawnZ = loc.getBlockZ();
        int a = MathUtils.random(1, 2);
        int b = MathUtils.random(0, 1);
        int x, z;
        if (a == 1) {
            z = spawnZ + 30 * b;
            x = MathUtils.random(spawnX - 15, spawnX + 15);
        } else {
            x = spawnX - 15 + 30 * b;
            z = MathUtils.random(spawnZ, spawnZ + 30);
        }
        return new Location(m.getWorld(), x + 0.5, loc.getBlockY(), z + 0.5);
    }

    private static void spawnAnomaly(String mine) {
        Debugger.log("Spawning anomaly.", "anomaly_spawn");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§dAn §b§lAnomaly §dhas appeared in mine §e§l" + mine.toUpperCase());
        }
        Debugger.log("Spawning entity.", "anomaly_spawn");
        Location loc = getRandomLocation(mine);
        if (!loc.getChunk().isLoaded()) {
            Debugger.log("Chunk is not loaded, loading.", "anomaly_spawn");
            loc.getChunk().load();
        }
        LivingEntity ent = (LivingEntity) loc.getWorld().spawnEntity(loc, pickEntityType());
        AnomalyType type = AnomalyType.COMMON;
        Debugger.log("Setting name.", "anomaly_spawn");
        ent.setCustomName(type.getCustomName(type.getHealth()));
        Debugger.log("Setting name visibility.", "anomaly_spawn");
        ent.setCustomNameVisible(true);
        Debugger.log("Setting health.", "anomaly_spawn");
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHealth());
        ent.setHealth(type.getHealth());
        Debugger.log("Disabling AI.", "anomaly_spawn");
        ent.setAI(false);
        ent.setMetadata("anomalyData", EntityMeta.createMetadataValue(type.name()));
        Debugger.log("Generated entity.", "anomaly_spawn");
    }

    @Contract(pure = true)
    static double getPercentage(double health, double maxHealth) {
        return (double) Math.round((health / maxHealth) * 1000) / 10;
    }

    public static void deinit() {
        Bukkit.getWorld("mines").getEntities().forEach(ent -> {
            if (ent.hasMetadata("anomalyData")) {
                ent.remove();
            }
        });
    }
}
