package net.acomputerdog.boxle.save.world;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.SaveManager;
import net.acomputerdog.boxle.save.world.files.Region;
import net.acomputerdog.boxle.save.world.files.WorldMetaFile;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class WorldSave {
    private final String worldName;

    private final Set<Region> openRegions = new ConcurrentSkipListSet<>();
    private World world;
    private WorldMetaFile worldMeta;

    public WorldSave(String worldName) {
        this.worldName = worldName;
    }

    public void open() throws IOException {
        createWorld();
        getWorldMeta();
        if (SaveManager.worldExists(worldName)) {
            getWorldMeta().load();
        } else {
            getWorldMeta().create();
        }
    }

    public void save() throws IOException {
        for (Chunk chunk : world.getChunks().getAllChunks()) {
            if (chunk.isModifiedFromLoad()) {
                SaveManager.saveChunkDelayed(chunk);
            }
        }
        for (Region region : openRegions) {
            SaveManager.unloadRegion(region);
        }
        openRegions.clear();
    }

    public World createWorld() {
        return world = (world == null ? new World(Boxle.instance(), worldName, this) : world);
    }

    public WorldMetaFile getWorldMeta() {
        return worldMeta = (worldMeta == null ? new WorldMetaFile(world, new File(SaveManager.getWorldDir(worldName), "/meta.dat")) : worldMeta);
    }

    public Region getRegion(int x, int y, int z) {
        File regFile = SaveManager.getRegionFile(worldName, x, y, z);
        Region region = new Region(getWorldMeta(), regFile, VecPool.getVec3i(x, y, z));
        openRegions.add(region);
        return region;
    }
}
