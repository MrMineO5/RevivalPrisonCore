/*
 * Copyright (c) 2020. UltraDev
 */

package net.ultradev.prisoncore.dungeons;

public class DungeonManager {
    private DungeonManager() {}
    private static DungeonManager instance;
    public static DungeonManager getInstance() {
        if (instance == null) {
            instance = new DungeonManager();
        }
        return instance;
    }

    private Dungeon runningDungeon;

    public boolean runDungeon(Dungeon dungeon) {
        if (runningDungeon != null) {
            return false;
        }
        this.runningDungeon = dungeon;
        return true;
    }
    public void init() {
        
    }
}
