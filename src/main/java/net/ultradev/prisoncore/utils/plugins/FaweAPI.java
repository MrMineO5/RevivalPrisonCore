/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.plugins;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.config.BBC;
import com.boydti.fawe.config.Settings;
import com.boydti.fawe.example.NMSMappedFaweQueue;
import com.boydti.fawe.example.NMSRelighter;
import com.boydti.fawe.object.*;
import com.boydti.fawe.object.changeset.DiskStorageHistory;
import com.boydti.fawe.regions.FaweMaskManager;
import com.boydti.fawe.util.*;
import com.boydti.fawe.wrappers.WorldWrapper;
import com.sk89q.jnbt.*;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extension.factory.DefaultTransformParser;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * The FaweAPI class offers a few useful functions.<br>
 * - This class is not intended to replace the WorldEdit API<br>
 * - With FAWE installed, you can use the EditSession and other WorldEdit
 * classes from an async thread.<br>
 * <br>
 * FaweAPI.[some method]
 */
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"})
public class FaweAPI {
    /**
     * Offers a lot of options for building an EditSession
     *
     * @param world World to get session builder in
     * @return A new EditSessionBuilder
     * @see EditSessionBuilder
     */
    public static EditSessionBuilder getEditSessionBuilder(World world) {
        return new EditSessionBuilder(world);
    }

    /**
     * The TaskManager has some useful methods for doing things asynchronously
     *
     * @return TaskManager
     */
    public static TaskManager getTaskManager() {
        return TaskManager.IMP;
    }

    /**
     * Add a custom transform for use in
     *
     * @param methods The class with a bunch of transform methods
     * @return true if the transform was registered
     * @see com.sk89q.worldedit.command.TransformCommands
     */
    public static boolean registerTransforms(Object methods) {
        DefaultTransformParser parser = Fawe.get().getTransformParser();
        if (parser != null)
            parser.register(methods);
        return parser != null;
    }

    public static <T> T getParser(Class<T> parserClass) {
        try {
            Field field = AbstractFactory.class.getDeclaredField("parsers");
            field.setAccessible(true);
            ArrayList<InputParser> parsers = new ArrayList<>();
            parsers.addAll((List<InputParser>) field.get(WorldEdit.getInstance().getMaskFactory()));
            parsers.addAll((List<InputParser>) field.get(WorldEdit.getInstance().getBlockFactory()));
            parsers.addAll((List<InputParser>) field.get(WorldEdit.getInstance().getItemFactory()));
            parsers.addAll((List<InputParser>) field.get(WorldEdit.getInstance().getPatternFactory()));
            for (InputParser parser : parsers) {
                if (parserClass.isAssignableFrom(parser.getClass())) {
                    return (T) parser;
                }
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrap some object into a FawePlayer<br>
     * - org.bukkit.entity.Player - org.spongepowered.api.entity.living.player -
     * com.sk89q.worldedit.entity.Player - String (name) - UUID (player UUID)
     *
     * @param obj
     * @return
     */
    public static FawePlayer wrapPlayer(Object obj) {
        return FawePlayer.wrap(obj);
    }

    public static FaweQueue createQueue(String worldName, boolean autoqueue) {
        return SetQueue.IMP.getNewQueue(worldName, true, autoqueue);
    }

    /**
     * You can either use a FaweQueue or an EditSession to change blocks<br>
     * - The FaweQueue skips a bit of overhead so it's faster<br>
     * - The WorldEdit EditSession can do a lot more<br>
     * Remember to enqueue it when you're done!<br>
     *
     * @param world     The name of the world
     * @param autoqueue If it should start dispatching before you enqueue it.
     * @return
     * @see FaweQueue#enqueue()
     */
    public static FaweQueue createQueue(World world, boolean autoqueue) {
        return SetQueue.IMP.getNewQueue(world, true, autoqueue);
    }

    public static World getWorld(String worldName) {
        List<? extends World> worlds = WorldEdit.getInstance().getServer().getWorlds();
        for (World current : worlds) {
            if (Fawe.imp().getWorldName(current).equals(worldName)) {
                return WorldWrapper.wrap(current);
            }
        }
        for (World current : worlds) {
            if (current.getName().equals(worldName)) {
                return WorldWrapper.wrap(current);
            }
        }
        return null;
    }

    /**
     * Get a list of supported protection plugin masks.
     *
     * @return Set of FaweMaskManager
     */
    public static Set<FaweMaskManager> getMaskManagers() {
        return new HashSet<>(WEManager.IMP.managers);
    }

    /**
     * Check if the server has more than the configured low memory threshold
     *
     * @return True if the server has limited memory
     */
    public static boolean isMemoryLimited() {
        return MemUtil.isMemoryLimited();
    }

    /**
     * Use ThreadLocalRandom instead
     *
     * @return
     */
    @Deprecated
    public static PseudoRandom getFastRandom() {
        return new PseudoRandom();
    }

    /**
     * Get a player's allowed WorldEdit region
     *
     * @param player
     * @return
     */
    public static Region[] getRegions(FawePlayer player) {
        return WEManager.IMP.getMask(player);
    }

    /**
     * Cancel the edit with the following extent<br>
     * - The extent must be the one being used by an EditSession, otherwise an error
     * may be thrown <br>
     * - Insert an extent into the EditSession using the EditSessionEvent:
     * http://wiki.sk89q.com/wiki/WorldEdit/API/Hooking_EditSession <br>
     *
     * @param extent
     * @param reason
     * @see EditSession#getRegionExtent() To get the FaweExtent
     * for an EditSession
     */
    public static void cancelEdit(Extent extent, BBC reason) {
        try {
            WEManager.IMP.cancelEdit(extent, reason);
        } catch (WorldEditException ignore) {
        }
    }

    public static void addMaskManager(FaweMaskManager maskMan) {
        WEManager.IMP.managers.add(maskMan);
    }

    /**
     * Get the DiskStorageHistory object representing a File
     *
     * @param file
     * @return
     */
    public static DiskStorageHistory getChangeSetFromFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("Not a file!");
        }
        if (!file.getName().toLowerCase().endsWith(".bd")) {
            throw new IllegalArgumentException("Not a BD file!");
        }
        if (Settings.IMP.HISTORY.USE_DISK) {
            throw new IllegalArgumentException("History on disk not enabled!");
        }
        String[] path = file.getPath().split(File.separator);
        if (path.length < 3) {
            throw new IllegalArgumentException("Not in history directory!");
        }
        String worldName = path[path.length - 3];
        String uuidString = path[path.length - 2];
        World world = getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("Corresponding world does not exist: " + worldName);
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID from file path: " + uuidString);
        }
        DiskStorageHistory history = new DiskStorageHistory(world, uuid,
                Integer.parseInt(file.getName().split("\\.")[0]));
        return history;
    }

    /**
     * Used in the RollBack to generate a list of DiskStorageHistory objects<br>
     * - Note: An edit outside the radius may be included if it overlaps with an
     * edit inside that depends on it.
     *
     * @param origin   - The origin location
     * @param user     - The uuid (may be null)
     * @param radius   - The radius from the origin of the edit
     * @param timediff - The max age of the file in milliseconds
     * @param shallow  - If shallow is true, FAWE will only read the first
     *                 Settings.IMP.BUFFER_SIZE bytes to obtain history info<br>
     *                 Reading only part of the file will result in unreliable
     *                 bounds info for large edits
     * @return
     */
    public static List<DiskStorageHistory> getBDFiles(FaweLocation origin, UUID user, int radius, long timediff,
                                                      boolean shallow) {
        File history = MainUtil.getFile(Fawe.imp().getDirectory(),
                Settings.IMP.PATHS.HISTORY + File.separator + origin.world);
        if (!history.exists()) {
            return new ArrayList<>();
        }
        long now = System.currentTimeMillis();
        ArrayList<File> files = new ArrayList<>();
        for (File userFile : history.listFiles()) {
            if (!userFile.isDirectory()) {
                continue;
            }
            UUID userUUID;
            try {
                userUUID = UUID.fromString(userFile.getName());
            } catch (IllegalArgumentException e) {
                continue;
            }
            if (user != null && !userUUID.equals(user)) {
                continue;
            }
            // ArrayList<Integer> ids = new ArrayList<>();
            for (File file : userFile.listFiles()) {
                if (file.getName().endsWith(".bd")) {
                    if (timediff >= Integer.MAX_VALUE || now - file.lastModified() <= timediff) {
                        files.add(file);
                        if (files.size() > 2048) {
                            return null;
                        }
                    }
                }
            }
        }
        World world = origin.getWorld();
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                String aName = a.getName();
                String bName = b.getName();
                int aI = Integer.parseInt(aName.substring(0, aName.length() - 3));
                int bI = Integer.parseInt(bName.substring(0, bName.length() - 3));
                long value = aI - bI;
                return value == 0 ? 0 : value < 0 ? -1 : 1;
            }
        });
        RegionWrapper bounds = new RegionWrapper(origin.x - radius, origin.x + radius, origin.z - radius,
                origin.z + radius);
        RegionWrapper boundsPlus = new RegionWrapper(bounds.minX - 64, bounds.maxX + 512, bounds.minZ - 64,
                bounds.maxZ + 512);
        HashSet<RegionWrapper> regionSet = new HashSet<RegionWrapper>(Arrays.asList(bounds));
        ArrayList<DiskStorageHistory> result = new ArrayList<>();
        for (File file : files) {
            UUID uuid = UUID.fromString(file.getParentFile().getName());
            DiskStorageHistory dsh = new DiskStorageHistory(world, uuid,
                    Integer.parseInt(file.getName().split("\\.")[0]));
            DiskStorageHistory.DiskStorageSummary summary = dsh.summarize(boundsPlus, shallow);
            RegionWrapper region = new RegionWrapper(summary.minX, summary.maxX, summary.minZ, summary.maxZ);
            boolean encompassed = false;
            boolean isIn = false;
            for (RegionWrapper allowed : regionSet) {
                isIn = isIn || allowed.intersects(region);
                if (encompassed = allowed.isIn(region.minX, region.maxX) && allowed.isIn(region.minZ, region.maxZ)) {
                    break;
                }
            }
            if (isIn) {
                result.add(0, dsh);
                if (!encompassed) {
                    regionSet.add(region);
                }
                if (shallow && result.size() > 64) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * The DiskStorageHistory class is what FAWE uses to represent the undo on disk.
     *
     * @param world
     * @param uuid
     * @param index
     * @return
     * @see DiskStorageHistory#toEditSession(FawePlayer)
     */
    public static DiskStorageHistory getChangeSetFromDisk(World world, UUID uuid, int index) {
        return new DiskStorageHistory(world, uuid, index);
    }

    /**
     * Compare two versions
     *
     * @param version
     * @param major
     * @param minor
     * @param minor2
     * @return true if version is >= major, minor, minor2
     */
    public static boolean checkVersion(final int[] version, final int major, final int minor, final int minor2) {
        return (version[0] > major) || ((version[0] == major) && (version[1] > minor))
                || ((version[0] == major) && (version[1] == minor) && (version[2] >= minor2));
    }

    @Deprecated
    public static int fixLighting(String world, Region selection) {
        return fixLighting(world, selection, FaweQueue.RelightMode.ALL);
    }

    @Deprecated
    public static int fixLighting(String world, Region selection, final FaweQueue.RelightMode mode) {
        return fixLighting(world, selection, null, mode);
    }

    @Deprecated
    public static int fixLighting(String world, Region selection, @Nullable FaweQueue queue,
                                  final FaweQueue.RelightMode mode) {
        return fixLighting(getWorld(world), selection, queue, mode);
    }

    /**
     * Fix the lighting in a selection<br>
     * - First removes all lighting, then relights - Relights in parallel (if
     * enabled) for best performance<br>
     * - Also resends chunks<br>
     *
     * @param world
     * @param selection (assumes cuboid)
     * @return
     */
    public static int fixLighting(World world, Region selection, @Nullable FaweQueue queue,
                                  final FaweQueue.RelightMode mode) {
        final Vector bot = selection.getMinimumPoint();
        final Vector top = selection.getMaximumPoint();

        final int minX = bot.getBlockX() >> 4;
        final int minZ = bot.getBlockZ() >> 4;

        final int maxX = top.getBlockX() >> 4;
        final int maxZ = top.getBlockZ() >> 4;

        int count = 0;
        if (queue == null) {
            queue = SetQueue.IMP.getNewQueue(world, true, false);
        }
        // Remove existing lighting first
        if (queue instanceof NMSMappedFaweQueue) {
            final NMSMappedFaweQueue nmsQueue = (NMSMappedFaweQueue) queue;
            NMSRelighter relighter = new NMSRelighter(nmsQueue);
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    relighter.addChunk(x, z, null, 65535);
                    count++;
                }
            }
            if (mode != FaweQueue.RelightMode.NONE) {
                boolean sky = nmsQueue.hasSky();
                if (sky) {
                    relighter.fixSkyLighting();
                }
                relighter.fixBlockLighting();
            } else {
                relighter.removeLighting();
            }
            relighter.sendChunks();
        }
        return count;
    }

    /**
     * If a schematic is too large to be pasted normally<br>
     * - Skips any block history - Ignores nbt
     *
     * @param file
     * @param loc
     * @return
     */
    @Deprecated
    public static void streamSchematic(final File file, final FaweLocation loc) {
        try {
            final FileInputStream is = new FileInputStream(file);
            streamSchematic(is, loc);
        } catch (final IOException e) {
            MainUtil.handleError(e);
        }
    }

    /**
     * @param url
     * @param loc
     * @deprecated Since I haven't finished it yet If a schematic is too large to be
     * pasted normally<br>
     * - Skips any block history - Ignores nbt
     */
    @Deprecated
    public static void streamSchematic(final URL url, final FaweLocation loc) {
        try {
            final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            final InputStream is = Channels.newInputStream(rbc);
            streamSchematic(is, loc);
        } catch (final IOException e) {
            MainUtil.handleError(e);
        }
    }

    /**
     * @param is
     * @param loc
     * @throws IOException
     * @deprecated Since I haven't finished it yet If a schematic is too large to be
     * pasted normally<br>
     * - Skips any block history - Ignores some block data - Not
     * actually streaming from disk, but it does skip a lot of overhead
     */
    @Deprecated
    public static void streamSchematic(final InputStream is, final FaweLocation loc) throws IOException {
        final NBTInputStream stream = new NBTInputStream(new GZIPInputStream(is));
        Tag tag = stream.readNamedTag().getTag();
        stream.close();

        Map<String, Tag> tagMap = (Map<String, Tag>) tag.getValue();

        final short width = ((ShortTag) tagMap.get("Width")).getValue();
        final short length = ((ShortTag) tagMap.get("Length")).getValue();
        final short height = ((ShortTag) tagMap.get("Height")).getValue();
        byte[] ids = ((ByteArrayTag) tagMap.get("Blocks")).getValue();
        byte[] datas = ((ByteArrayTag) tagMap.get("Data")).getValue();

        // final String world = loc.world;

        final int x_offset = loc.x + ((IntTag) tagMap.get("WEOffsetX")).getValue();
        final int y_offset = loc.y + ((IntTag) tagMap.get("WEOffsetY")).getValue();
        final int z_offset = loc.z + ((IntTag) tagMap.get("WEOffsetZ")).getValue();

        FaweQueue queue = SetQueue.IMP.getNewQueue(getWorld(loc.world), true, true);

        for (int y = 0; y < height; y++) {
            final int yy = y_offset + y;
            if (yy > 255) {
                continue;
            }
            final int i1 = y * width * length;
            for (int z = 0; z < length; z++) {
                final int i2 = (z * width) + i1;
                final int zz = z_offset + z;
                for (int x = 0; x < width; x++) {
                    final int i = i2 + x;
                    final int xx = x_offset + x;
                    final short id = (short) (ids[i] & 0xFF);
                    queue.setBlock(xx, yy, zz, id, datas[i]);
                }
            }
        }

        queue.enqueue();
    }

    /**
     * Set a task to run when the global queue (SetQueue class) is empty
     *
     * @param whenDone
     */
    public static void addTask(final Runnable whenDone) {
        SetQueue.IMP.addEmptyTask(whenDone);
    }

    /**
     * Have a task run when the server is low on memory (configured threshold)
     *
     * @param run
     */
    public static void addMemoryLimitedTask(Runnable run) {
        MemUtil.addMemoryLimitedTask(run);
    }

    /**
     * Have a task run when the server is no longer low on memory (configured
     * threshold)
     *
     * @param run
     */
    public static void addMemoryPlentifulTask(Runnable run) {
        MemUtil.addMemoryPlentifulTask(run);
    }

    /**
     * @return
     * @see BBC
     */
    public static BBC[] getTranslations() {
        return BBC.values();
    }

    /**
     * @see #getEditSessionBuilder(World)
     * @deprecated
     */
    @Deprecated
    public static EditSession getNewEditSession(@Nonnull FawePlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        return player.getNewEditSession();
    }

    /**
     * @see #getEditSessionBuilder(World)
     * @deprecated
     */
    @Deprecated
    public static EditSession getNewEditSession(World world) {
        return WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
    }

}