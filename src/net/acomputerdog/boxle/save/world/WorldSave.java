package net.acomputerdog.boxle.save.world;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.Region;
import net.acomputerdog.boxle.save.SaveManager;
import net.acomputerdog.boxle.save.WorldMetaFile;
import net.acomputerdog.boxle.world.World;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WorldSave {
    private final String worldName;

    private final List<Region> openRegions = new LinkedList<>();
    private World world;
    private WorldMetaFile worldMeta;

    public WorldSave(String worldName) {
        this.worldName = worldName;
    }

    public void open() throws IOException {
        createWorld();
        getWorldMeta();
        if (SaveManager.worldExists(worldName)) {
            worldMeta.load();
        } else {
            worldMeta.create();
        }
    }

    public void save() throws IOException {
        for (Region region : openRegions) {
            try {
                region.save();
            } catch (IOException e) {
                SaveManager.LOGGER.logError("Could not save region at: " + region.getLoc().asCoords(), e);
            }
        }
        openRegions.clear();
        worldMeta.save();
    }

    public World createWorld() {
        return world = (world == null ? new World(Boxle.instance(), worldName) : world);
    }

    public WorldMetaFile getWorldMeta() {
        return worldMeta = (worldMeta == null ? new WorldMetaFile(world, new File(SaveManager.getWorldDir(worldName), "/meta.dat")) : worldMeta);
    }

    public Region getRegion(int x, int y, int z) {
        File regFile = SaveManager.getRegionFile(worldName, x, y, z);
        Region region = new Region(worldMeta, regFile, VecPool.getVec3i(x, y, z));
        region.open();
        openRegions.add(region);
        return region;
    }
}
