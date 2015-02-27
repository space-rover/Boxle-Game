package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.save.io.IOThread;
import net.acomputerdog.boxle.save.world.WorldSave;
import net.acomputerdog.boxle.save.world.files.Region;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.logger.CLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    public static final CLogger LOGGER = new CLogger("World_Saves", false, true);

    private static final File worldsDir = createWorldsDir();

    private static final Map<String, WorldSave> saveMap = new HashMap<>();

    public static boolean worldExists(String name) {
        return new File(worldsDir, name).isDirectory();
    }

    public static WorldSave loadOrCreateWorldSave(String name) throws IOException {
        if (worldExists(name)) {
            return loadWorldSave(name);
        } else {
            return createWorldSave(name);
        }
    }

    public static WorldSave getLoadedWorldSave(String name) {
        return saveMap.get(name);
    }

    private static WorldSave loadWorldSave(String name) throws IOException {
        WorldSave save = saveMap.get(name);
        if (save == null) {
            save = new WorldSave(name);
            save.open();
            save.createWorld().getSaveIO(); //create world and create thread
            saveMap.put(name, save);
        }
        return save;
    }

    public static WorldSave createWorldSave(String name) {
        WorldSave save = saveMap.get(name);
        if (save == null) {
            initializeWorldDirectory(name);
            save = new WorldSave(name);
            save.createWorld().getSaveIO(); //create world and create thread
            saveMap.put(name, save);
        }
        return save;
    }

    public static void saveWorld(World world) throws IOException {
        world.getWorldSave().save();
    }

    public static void saveChunkDelayed(Chunk chunk) {
        chunk.getWorld().getSaveIO().addSave(chunk);
    }

    public static void unloadRegion(Region region) {
        region.getWorld().getSaveIO().addRegion(region);
    }

    public static void initializeWorldDirectory(String name) {
        File worldDir = getWorldDir(name);
        File regions = new File(worldDir, "/regions/");
        if (!regions.isDirectory() && !regions.mkdirs()) {
            LOGGER.logWarning("Unable to create world region directory!");
        }
    }

    public static void loadChunkDelayed(World world, Vec3i loc) {
        world.getSaveIO().addLoad(loc);
    }

    public static File getRegionFile(String world, int x, int y, int z) {
        File dir = new File(getWorldDir(world), "/regions/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            LOGGER.logWarning("Unable to create regions directory for world " + world);
        }
        return new File(dir, "/" + x + "_" + y + "_" + z + ".region");
    }

    private static File createWorldsDir() {
        File worlds = new File("./worlds/");
        if (!(worlds.isDirectory() || worlds.mkdirs())) {
            LOGGER.logWarning("Unable to create saves directory!");
        }
        return worlds;
    }

    public static File getWorldDir(String name) {
        File dir = new File(worldsDir, "/" + name + "/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            LOGGER.logWarning("Unable to create world directory for world " + name);
        }
        return dir;
    }

    public static void waitForSave() {
        IOThread.waitForEnd();
    }

    public static Chunk loadOrGenerateChunk(World world, Vec3i loc) {
        Chunk chunk = world.getChunks().getChunk(loc);
        if (chunk == null) {
            Region region = world.getRegion(loc);
            if (region == null || region.hasChunkGlobal(loc)) {
                SaveManager.loadChunkDelayed(world, loc);
            } else {
                return world.createNewChunk(loc);
            }
        }
        return chunk;
    }

}
