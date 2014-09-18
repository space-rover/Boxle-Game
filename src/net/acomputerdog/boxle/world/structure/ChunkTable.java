package net.acomputerdog.boxle.world.structure;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Holds chunks for a world
 */
public class ChunkTable {
    private final Map<Vec3i, Chunk> chunkLocMap;
    private final List<Chunk> chunkList;

    private final World world;

    public ChunkTable(World world) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        this.world = world;
        chunkLocMap = new ConcurrentHashMap<>();
        chunkList = new CopyOnWriteArrayList<>();
    }

    public Chunk addChunk(Chunk chunk) {
        if (chunk == null) throw new IllegalArgumentException("Chunk cannot be null!");
        return chunkLocMap.put(chunk.getLocation(), chunk);
    }

    public Chunk getChuck(Vec3i loc) {
        return chunkLocMap.get(loc);
    }

    public Chunk getChunk(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Chunk chunk = getChuck(vec);
        VecPool.freeVec3i(vec); //don't forget to free the vec!
        return chunk;
    }

    public List<Chunk> getAllChunks() {
        return Collections.unmodifiableList(chunkList);
    }

    public World getWorld() {
        return world;
    }
}
