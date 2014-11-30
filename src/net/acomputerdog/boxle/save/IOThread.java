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

    private final CLogger logger;

    private final World world;

    private final Queue<Vec3i> loadQueue = new ConcurrentLinkedQueue<>();
    private final Set<Vec3i> loadSet = new ConcurrentSkipListSet<>();
    private final Queue<Chunk> saveQueue = new ConcurrentLinkedQueue<>();
    private final Set<Chunk> saveSet = new ConcurrentSkipListSet<>();

    private IOThread(World world) {
        super();
        this.world = world;
        super.setName("IOThread_" + world.getName());
        super.setDaemon(false);
        logger = new CLogger("IOThread_" + world.getName(), false, true);
    }

    @Override
    public void run() {
        logger.logInfo("Starting.");
        boolean canRun = true;
        while (canRun) {
            boolean shouldRun = Boxle.instance().canRun();
            boolean performedAction = false;
            if (shouldRun) {
                Vec3i loc = loadQueue.poll();
                if (loc != null) {
                    loadSet.remove(loc);
                    performedAction = true;
                    try {
                        world.addNewChunk(SaveManager.loadChunk(world, loc));
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
                    SaveManager.saveChunk(chunk);
                } catch (IOException e) {
                    logger.logWarning("Unable to save chunk at " + chunk.asCoords(), e);
                }
            }
            if (!performedAction) {
                Sleep.sleep(50);
                canRun = shouldRun;
            }
        }
        logger.logInfo("Stopping.");
    }

    public World getWorld() {
        return world;
    }

    public void addLoad(Vec3i loc) {
        if (loc != null && !loadSet.contains(loc)) {
            //System.out.println("Adding load at " + loc.asCoords());
            loadSet.add(loc);
            loadQueue.add(loc);
        }
    }

    public void addSave(Chunk chunk) {
        if (chunk != null && !saveSet.contains(chunk)) {
            saveSet.add(chunk);
            saveQueue.add(chunk);
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
        Boxle.LOGGER.logInfo("Saving chunks...");
        Collection<IOThread> threads = threadMap.values();
        boolean waiting = true;
        while (waiting) {
            waiting = false;
            for (IOThread thread : threads) {
                if (thread.isAlive()) {
                    waiting = true;
                }
            }
        }
        Boxle.LOGGER.logInfo("Done.");
    }
}
