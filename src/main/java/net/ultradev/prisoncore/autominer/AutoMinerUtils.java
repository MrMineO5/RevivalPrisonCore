/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.autominer;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.Map;

public class AutoMinerUtils {
    public static AutoMinerAI getModel(Player player) {
        Map<Player, NPC> npcs = AutominerCore.getCore().npcs;
        assert npcs != null;
        NPC npc = npcs.get(player);
        assert npc != null;
        AutoMinerTrait trait = npc.getTrait(AutoMinerTrait.class);
        assert trait != null;
        return trait.getAi();
    }


}
