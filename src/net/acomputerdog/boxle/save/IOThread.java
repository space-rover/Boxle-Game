package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.java.Sleep;
import net.acomputerdog.core.logger.CLogger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

//TODO keep instance with world
public class IOThread extends Thread {
    private static final Map<World, IOThread> threadMap = new HashMap<>();

    private static final CLogger LOGGER_GLOBAL = new CLogger("IO", false, true);

    private final CLogger logger;
    private final World world;

    private final Queue<Vec3i> loadQueue = new ConcurrentLinkedQueue<>();
    private final Set<Vec3i> loadSet = new ConcurrentSkipListSet<>();
    private final Queue<Chunk> saveQueue = new ConcurrentLinkedQueue<>();
    private final Set<Chunk> saveSet = new ConcurrentSkipListSet<>();
    private final Queue<Region> regionQueue = new ConcurrentLinkedQueue<>();
    private final Set<Region> regionSet = new ConcurrentSkipListSet<>();

    private IOThread(World world) {
        super();
        this.world = world;
        super.setName("IO_" + world.getName());
        super.setDaemon(false);
        logger = new CLogger("IOThread_" + world.getName(), false, true);
    }

    @Override
    public void run() {
        try {
            logger.logInfo("Starting.");
            boolean canRun = true;
            while (canRun) {
                canRun = performTick(canRun);
            }
            SaveManager.getLoadedWorld(world.getName()).getWorldMeta().save();
            logger.logInfo("Stopping.");
        } catch (Throwable t) {
            logger.logFatal("Unhandled Exception in IOThread!", t);
            Boxle.instance().stop();
        }
    }

    private boolean performTick(boolean canRun) {
        boolean canLoadChunks = Boxle.instance().canRunIO();
        boolean performedAction = false;
        if (canLoadChunks) {
            Vec3i loc = loadQueue.poll();
            if (loc != null) {
                loadSet.remove(loc);
                performedAction = true;
                try {
                    Region region = Regions.getOrLoadRegionChunkLoc(world, loc.x, loc.y, loc.z);
                    if (region.hasChunkGlobal(loc)) {
                        world.addNewChunk(region.readChunk(loc));
                    } else {
                        world.createNewChunk(loc); //tell world it can find it's own chunk
                    }
                } catch (IOException e) {
                    logger.logWarning("Unable to load chunk at " + loc.asCoords(), e);
                }
            }
        }
        Chunk chunk = saveQueue.poll();
        if (chunk != null) {
            saveSet.remove(chunk);
            performedAction = true;
            try {
                Regions.getOrLoadRegionChunkLoc(world, chunk.getXLoc(), chunk.getYLoc(), chunk.getZLoc()).writeChunk(chunk);
            } catch (Exception e) {
                logger.logWarning("Unable to save chunk at " + chunk.asCoords(), e);
            }
        } else {
            Region region = regionQueue.poll();
            if (region != null) {
                regionSet.remove(region);
                performedAction = true;
                try {
                    Regions.removeRegion(region);
                    region.save();
                    region.close();
                } catch (Exception e) {
                    logger.logWarning("Unable to save region at " + region.getLoc().asCoords(), e);
                }
            }
        }

        if (!performedAction) {
            Sleep.sleep(1);
            return canLoadChunks;
        }
        return canRun;
    }

    public World getWorld() {
        return world;
    }

    public void addLoad(Vec3i loc) {
        if (loc != null && !loadSet.contains(loc)) {
            loadSet.add(loc);
            loadQueue.add(loc);
        }
    }

    public void addSave(Chunk chunk) {
        if (chunk != null && chunk.isModifiedFromLoad() && !saveSet.contains(chunk)) {
            chunk.setModifiedFromLoad(false);
            saveSet.add(chunk);
            saveQueue.add(chunk);
        }
    }

    public void addRegion(Region region) {
        if (region != null && !regionSet.contains(region)) {
            regionSet.add(region);
            regionQueue.add(region);
        }
    }

    public static IOThread getThread(World world) {
        IOThread thread = threadMap.get(world);
        if (thread == null) {
            threadMap.put(world, thread = new IOThread(world));
            thread.start();
        }
        return thread;
    }

    public static void waitForEnd() {
        LOGGER_GLOBAL.logInfo("Saving chunks...");
        Collection<IOThread> threads = threadMap.values();
        boolean waiting = true;
        while (waiting) {
            waiting = false;
            for (IOThread thread : threads) {
                if (thread.isAlive()) {
                    waiting = true;
                }
            }
            Sleep.sleep(10);
        }
        LOGGER_GLOBAL.logInfo("Chunks saved.");
    }
}
