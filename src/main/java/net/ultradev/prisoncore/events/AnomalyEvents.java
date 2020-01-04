/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.events;

import net.ultradev.prisoncore.anomalies.AnomalyType;
import net.ultradev.prisoncore.anomalies.RewardGenerator;
import net.ultradev.prisoncore.rewards.Reward;
import net.ultradev.prisoncore.utils.MapSerializers;
import net.ultradev.prisoncore.utils.Scheduler;
import net.ultradev.prisoncore.utils.betterspigot.EntityMeta;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AnomalyEvents implements Listener {
    public static Map<UUID, Long> lastDamaged = new HashMap<>();
    public Plugin plugin = null;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity ent = e.getEntity();
        lastDamaged.remove(ent.getUniqueId());
        if (!ent.hasMetadata("anomalyData")) {
            return;
        }
        String str = ent.getMetadata("anomalyData").get(0).asString();
        Debugger.log("Anomaly exists. Data: " + str, "anomaly_update");
        if (ent.getTicksLived() > 60 * 20 * 2) {
            Debugger.log("Anomaly is older than 2 minutes, removing...", "anomaly_update");
            ent.remove();
            return;
        }
        AnomalyType type = AnomalyType.valueOf(str);
        e.setDroppedExp(0);
        e.getDrops().clear();
        if (!ent.hasMetadata("damageMap")) {
            return;
        }
        String dat = ent.getMetadata("damageMap").get(0).asString();
        Map<UUID, Double> pls = MapSerializers.deserialize(dat);
        pls.forEach((id, dam) -> {
            Player player = Bukkit.getPlayer(id);
            if (player == null) {
                return;
            }
            List<Reward> rews = RewardGenerator.generateRewards(type, dam);
            for (Reward rew : rews) {
                if (rew == null) {
                    continue;
                }
                rew.applyReward(player, null);
            }
        });
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        if (!ent.hasMetadata("anomalyData")) {
            return;
        }
        lastDamaged.put(ent.getUniqueId(), DateUtils.getMilliTimeStamp());
        Debugger.log("Anomaly exists. Getting entity.", "anomaly_update");
        String str = ent.getMetadata("anomalyData").get(0).asString();
        Map<UUID, Double> map;
        if (ent.hasMetadata("damageMap")) {
            map = MapSerializers.deserialize(ent.getMetadata("damageMap").get(0).asString());
        } else {
            map = new HashMap<>();
        }
        double damage = e.getDamage();
        damage += map.getOrDefault(e.getDamager().getUniqueId(), 0D);
        map.put(e.getDamager().getUniqueId(), damage);
        AnomalyType type = AnomalyType.valueOf(str);
        Scheduler.scheduleSyncDelayedTask(() -> {
            e.getEntity().setVelocity(new Vector(0, 0, 0));
        }, 1);
        ent.setCustomName(type.getCustomName(((LivingEntity) e.getEntity()).getHealth()));
        ent.setMetadata("damageMap", EntityMeta.createMetadataValue(MapSerializers.serialize(map)));
    }
}
