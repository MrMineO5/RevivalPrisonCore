/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.mines;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.Getter;
import lombok.Setter;
import net.ultradev.prisoncore.utils.Parser;
import net.ultradev.prisoncore.utils.Synchronizer;
import net.ultradev.prisoncore.utils.items.ItemUtils;
import net.ultradev.prisoncore.utils.logging.Debugger;
import net.ultradev.prisoncore.utils.math.MathUtils;
import net.ultradev.prisoncore.utils.plugins.FaweAPI;
import net.ultradev.prisoncore.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class Mine {
    public HashMap<MaterialData, Double> composition = new HashMap<>();
    @Getter
    private String name;
    private World world;

    @Getter private Integer maxX = null;
    @Getter private Integer minX = null;
    @Getter private Integer maxY = null;
    @Getter private Integer minY = null;
    @Getter private Integer maxZ = null;
    @Getter private Integer minZ = null;
    @Getter private double tpX;
    @Getter private double tpY;
    @Getter private double tpZ;
    @Getter private int blockCount;
    @Getter private int airCount;

    private long lastReset;
    @Setter@Getter private long resetTime;

    public Mine(String name) {
        this.name = name;
        this.tpY = -1;
        this.resetTime = DateUtils.getMilliTimeStamp();
        this.blockCount = 0;
    }

    public Mine(String name, @NotNull Location spawnpoint, @NotNull Location min, @NotNull Location max) {
        this.name = name;
        this.tpX = spawnpoint.getX();
        this.tpY = spawnpoint.getY();
        this.tpZ = spawnpoint.getZ();
        this.maxX = max.getBlockX();
        this.maxY = max.getBlockY();
        this.maxZ = max.getBlockZ();
        this.minX = min.getBlockX();
        this.minY = min.getBlockY();
        this.minZ = min.getBlockZ();
        this.lastReset = DateUtils.getMilliTimeStamp();
        this.resetTime = 180000;
        this.blockCount = (this.maxX - this.minX) * (this.maxY - this.minY) * (this.maxZ - this.minZ);
    }

    public Mine(String name, @NotNull Location spawnpoint, @NotNull Location min, @NotNull Location max,
                HashMap<MaterialData, Double> composition) {
        this.name = name;
        this.tpX = spawnpoint.getX();
        this.tpY = spawnpoint.getY();
        this.tpZ = spawnpoint.getZ();
        this.maxX = max.getBlockX();
        this.maxY = max.getBlockY();
        this.maxZ = max.getBlockZ();
        this.minX = min.getBlockX();
        this.minY = min.getBlockY();
        this.minZ = min.getBlockZ();
        this.composition = composition;
        this.lastReset = DateUtils.getMilliTimeStamp();
        this.resetTime = 180000;
        this.blockCount = (this.maxX - this.minX) * (this.maxY - this.minY) * (this.maxZ - this.minZ);
    }

    private Mine(@NotNull ConfigurationSection sec) {
        minX = sec.getInt("minX");
        minY = sec.getInt("minY");
        minZ = sec.getInt("minZ");
        maxX = sec.getInt("maxX");
        maxY = sec.getInt("maxY");
        maxZ = sec.getInt("maxZ");
        String worldstr = sec.getString("world");
        if (worldstr.equals("none")) {
            worldstr = "mines";
        }
        world = Bukkit.getWorld(worldstr);
        if (world == null) {
            Bukkit.getLogger().severe("Error: World does not exist: " + worldstr);
        }
        Map<String, Double> sComposition = new HashMap<>();
        for (String str : sec.getConfigurationSection("composition").getKeys(false)) {
            sComposition.put(str, sec.getDouble("composition." + str));
        }
        composition = new HashMap<>();
        for (Map.Entry<String, Double> entry : sComposition.entrySet()) {
            composition.put(Parser.parseMaterialData(entry.getKey()), entry.getValue());
        }
        name = sec.getString("name");
        if (sec.contains("tpY")) {
            tpX = sec.getDouble("tpX");
            tpY = sec.getDouble("tpY");
            tpZ = sec.getDouble("tpZ");
        }
        this.resetTime = 180000;
        if (sec.contains("resetTime")) {
            this.resetTime = sec.getLong("resetTime");
        }
        this.lastReset = DateUtils.getMilliTimeStamp();
        this.blockCount = (this.maxX - this.minX) * (this.maxY - this.minY) * (this.maxZ - this.minZ);
    }

    @NotNull
    @Contract("_, _ -> new")
    static Mine load(@NotNull FileConfiguration config, String name) {
        return new Mine(config.getConfigurationSection("mines." + name));
    }

    public static ArrayList<CompositionEntry> mapComposition(Map<MaterialData, Double> compositionIn) {
        ArrayList<CompositionEntry> probabilityMap = new ArrayList<>();
        Map<MaterialData, Double> composition = new HashMap<>(compositionIn);
        double max = 0;
        for (Map.Entry<MaterialData, Double> entry : composition.entrySet()) {
            max += entry.getValue();
        }
        // Pad the remaining percentages with air
        if (max < 1) {
            composition.put(new MaterialData(Material.AIR), 1 - max);
            max = 1;
        }
        double i = 0;
        for (Map.Entry<MaterialData, Double> entry : composition.entrySet()) {
            double v = entry.getValue() / max;
            i += v;
            probabilityMap.add(new CompositionEntry(entry.getKey(), i));
        }
        return probabilityMap;
    }

    public World getWorld() {
        return this.world;
    }

    public Location getTpPos() {
        return new Location(world, tpX, tpY, tpZ);
    }

    public void setTpPos(@NotNull Location loc) {
        this.tpX = loc.getX();
        this.tpY = loc.getY();
        this.tpZ = loc.getZ();
    }

    public boolean isInside(@NotNull Player p) {
        return isInside(p.getLocation());
    }

    public boolean isInside(Location l) {
        if (world == null) {
            return false;
        }
        return (l.getWorld().getName().equals(world.getName())) && (l.getBlockX() >= minX && l.getBlockX() <= maxX)
                && (l.getBlockY() >= minY && l.getBlockY() <= maxY) && (l.getBlockZ() >= minZ && l.getBlockZ() <= maxZ);
    }

    public void resetMine() {
        if (world == null) {
            Debugger.error("World for mine " + name + " is null", "mine");
            return;
        }
        List<CompositionEntry> probabilityMap = mapComposition(composition);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Location l = p.getLocation();
            if (isInside(p)) {
                if (tpY >= 0) {
                    Location loc = p.getLocation();
                    Location newLoc = new Location(loc.getWorld(), loc.getX(), this.maxY + 1, loc.getZ());
                    p.teleport(newLoc);
                    p.sendMessage("§eThis mine is resetting, so your were teleported to the surface.");
                } else {
                    Location tp = new Location(world, l.getX(), maxY + 1, l.getZ());
                    Block block = tp.getBlock();

                    if (block.getType() != Material.AIR || block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                        tp = new Location(world, l.getX(),
                                l.getWorld().getHighestBlockYAt(l.getBlockX(), l.getBlockZ()), l.getZ());
                    }
                    p.teleport(tp);
                }
            }
        }

        EditSession editSession = FaweAPI
                .getEditSessionBuilder(FaweAPI.getWorld(this.world.getName()))
                .fastmode(true)
                .build();
        Vector min = new Vector(this.minX, this.minY, this.minZ);
        Vector max = new Vector(this.maxX, this.maxY, this.maxZ);
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock next(Vector vector) {
                double r = MathUtils.random(0.0, 1.0);
                for (CompositionEntry ce : probabilityMap) {
                    if (r <= ce.getChance()) {
                        return new BaseBlock(ce.getBlock().getItemTypeId(), ce.getBlock().getData());
                    }
                }
                return null;
            }

            @Override
            public BaseBlock next(int i, int i1, int i2) {
                return next(new Vector(i, i1, i2));
            }
        };
        try {
            editSession.setBlocks(new CuboidRegion(min, max), pattern);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        editSession.flushQueue();
        airCount = 0;
        lastReset = DateUtils.getMilliTimeStamp();
    }

    public void clearMine() {
        Debugger.log("Resetting mine " + this.name + "...", "mine_reset");
        if (world == null) {
            Debugger.log("world is null, cancelling mine reset.", "mine_reset");
            return;
        }
        Debugger.log("Starting with clear...", "mine_reset");

        EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(this.world.getName())).build();
        Vector min = new Vector(this.minX, this.minY, this.minZ);
        Vector max = new Vector(this.maxX, this.maxY, this.maxZ);
        try {
            editSession.setBlocks(new CuboidRegion(min, max), new SingleBlockPattern(new BaseBlock(Material.AIR.getId())));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        editSession.flushQueue();
        Debugger.log("Done!", "mine_reset");
    }

    public void save(FileConfiguration config) {
        Map<String, Object> me = new HashMap<>();
        me.put("name", name);
        me.put("maxX", maxX);
        me.put("minX", minX);
        me.put("maxY", maxY);
        me.put("minY", minY);
        me.put("maxZ", maxZ);
        me.put("minZ", minZ);
        me.put("tpX", tpX);
        me.put("tpY", tpY);
        me.put("tpZ", tpZ);
        if (world == null) {
            me.put("world", "none");
        } else {
            me.put("world", world.getName());
        }
        Map<String, Double> SComposition = new HashMap<String, Double>();
        for (Map.Entry<MaterialData, Double> entry : composition.entrySet()) {
            SComposition.put(entry.getKey().getItemType().toString() + ":" + entry.getKey().getData(),
                    entry.getValue());
        }
        me.put("resetTime", resetTime);
        me.put("composition", SComposition);
        config.set("mines." + name, me);
    }

    public void setLocations(Location pos1, Location pos2) {
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())) {
            System.out.println("Error: Two locations in different worlds specified for mine size.");
            return;
        }

        this.world = pos1.getWorld();

        int x1 = pos1.getBlockX();
        int x2 = pos2.getBlockX();
        int y1 = pos1.getBlockY();
        int y2 = pos2.getBlockY();
        int z1 = pos1.getBlockZ();
        int z2 = pos2.getBlockZ();

        if (x1 > x2) {
            this.maxX = x1;
            this.minX = x2;
        } else {
            this.maxX = x2;
            this.minX = x1;
        }
        if (y1 > y2) {
            this.maxY = y1;
            this.minY = y2;
        } else {
            this.maxY = y2;
            this.minY = y1;
        }
        if (z1 > z2) {
            this.maxZ = z1;
            this.minZ = z2;
        } else {
            this.maxZ = z2;
            this.minZ = z1;
        }
    }

    public List<String> listComposition() {
        List<String> messages = new ArrayList<>();
        messages.add("§aMaterials:");
        for (MaterialData data : this.composition.keySet()) {
            messages.add("§7- " + data.getItemType().toString().toLowerCase() + ":" + data.getData() + " : §6"
                    + this.composition.get(data));
        }
        return messages;
    }

    public boolean addMaterial(MaterialData mat, double chance) {
        double total = 0;
        for (double doub : composition.values()) {
            total += doub;
        }
        if (total + chance > 100.0) {
            return false;
        }
        composition.put(mat, chance);
        return true;
    }

    public boolean removeMaterial(MaterialData mat) {
        if (composition.containsKey(mat)) {
            composition.remove(mat);
            return true;
        }
        return false;
    }

    public boolean positionsSet() {
        if (this.maxX == null)
            return false;
        if (this.maxY == null)
            return false;
        if (this.maxZ == null)
            return false;
        if (this.minX == null)
            return false;
        if (this.minY == null)
            return false;
        return this.minZ != null;
    }

    public void blocksBroken(int amount) {
        this.airCount += amount;
    }

    public double getEmptiness() {
        /*this.blockCount = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        this.airCount = 0;
        for (int x = minX; x <= maxX; x++) {
            final int xL = x;
            for (int y = minY; y <= maxY; y++) {
                final int yL = y;
                Synchronizer.synchronize(() -> {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = world.getBlockAt(xL, yL, z);
                        if (block.getType().equals(Material.AIR))
                            airCount++;
                    }
                });
            }

        }*/
        return (((double) airCount) * 100) / ((double) blockCount);
    }

    public List<String> getGUIComposition() {
        List<String> ret = new ArrayList<>();
        for (MaterialData data : composition.keySet()) {
            Material mat = data.getItemType();
            String name = ItemUtils.getBlockName(mat);
            ret.add("§7 - " + name);
        }
        return ret.stream()
                .sorted((o1, o2) -> {
                    if (o1.contains("Lucky Block")) {
                        return 1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    public boolean isInOutMine(Location loc) {
        return (loc.getZ() > this.tpZ - 22 && loc.getZ() < this.tpZ + 156 && loc.getX() > this.tpX - 37
                && loc.getX() < this.tpX + 37);
    }

    public long timeUntilReset() {
        return resetTime - (DateUtils.getMilliTimeStamp() - lastReset);
    }

    public void update() {
        if (timeUntilReset() <= 0) {
            resetMine();
            return;
        }
        if (getEmptiness() > 50.0) {
            resetMine();
        }
    }

    public static class CompositionEntry {
        private MaterialData block;
        private double chance;

        public CompositionEntry(MaterialData block, double chance) {
            this.block = block;
            this.chance = chance;
        }

        public MaterialData getBlock() {
            return block;
        }

        public double getChance() {
            return chance;
        }
    }
}
