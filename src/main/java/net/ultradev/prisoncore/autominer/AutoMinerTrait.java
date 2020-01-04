/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import lombok.Getter;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.ultradev.prisoncore.utils.logging.Debugger;

public class AutoMinerTrait extends Trait {
    @Getter
    private AutoMinerAI ai;

    AutoMinerTrait() {
        super("autominer");
    }

    public void onSpawn() {
        npc.getNavigator().getLocalParameters().range(26);
        ai.reset();
        ai.onSpawn();
        SkinnableEntity skent = (SkinnableEntity) npc.getEntity();
        if (!skent.getSkinName().equalsIgnoreCase(ai.getSkin())) {
            Debugger.log("Setting skin...", "autominer");
            skent.setSkinName(ai.getSkin(), true);
        }
    }

    public void onAttach() {
        Debugger.log("Creating new AI...", "autominer");
        ai = new AutoMinerAI(npc);
        Debugger.log("Loading AI data...", "autominer");
        ai.loadConfig();
        Debugger.log("Setting NPC name...", "autominer");
        npc.setName("§e§lMiner");
    }

    public void onDespawn() {
        Debugger.log("Disabling AI...", "autominer");
        ai.run = false;
    }

    public void run() {
        if (npc.isSpawned()) {
            Debugger.log("Running AI...", "autominer");
            ai.run();
        }
    }

    /*
    @EventHandler
    public void onLeftClick(@NotNull NPCClickEvent e) {
        if (e.getNPC().isSpawned()) {
            if (e.getNPC() == npc && e.getClicker().getUniqueId().equals(model.getPlayer().getUniqueId())) {

            }
        }
    }
     */
}
