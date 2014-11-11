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
 * Holds chunks for a world.  Thread-safe.
 */
public class ChunkTable {
    /**
     * Map of chunk locations to chunks
     */
    private final Map<Vec3i, Chunk> chunkLocMap;
    /**
     * List of all loaded chunks
     */
    private final List<Chunk> chunkList;

    /**
     * World that these chunks belong to
     */
    private final World world;

    /**
     * Creates a new ChunkTable
     *
     * @param world The world that this ChunkTable's chunks belong to
     */
    public ChunkTable(World world) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        this.world = world;
        chunkLocMap = new ConcurrentHashMap<>(); //concurrent HashMap for thread safety
        chunkList = new CopyOnWriteArrayList<>(); //thread-safe list implementation
    }

    /**
     * Adds a chunk to the table.  Cannot be null.
     *
     * @param chunk The chunk to add
     * @return Return the chunk already defined at this location, if present.
     */
    public Chunk addChunk(Chunk chunk) {
        if (chunk == null) throw new IllegalArgumentException("Chunk cannot be null!");
        chunkList.add(chunk); //add chunk to list of all chunks
        Chunk oldChunk = chunkLocMap.put(chunk.getLocation(), chunk); //get existing chunk, or null
        if (oldChunk != null) {
            chunkList.remove(oldChunk); //if chunk exists, remove from chunkList
        }
        return oldChunk;
    }

    /**
     * Removed a chunk from the table
     *
     * @param loc The location of the chunk
     * @return Return the chunk that was removed.
     */
    public Chunk removeChunkAt(Vec3i loc) {
        Chunk chunk = chunkLocMap.remove(loc); //get existing chunk, or null
        if (chunk != null) {
            chunkList.remove(chunk); //if chunk exists, remove from chunkList
        }
        return chunk;
    }

    /**
     * Removes a chunk from the table
     *
     * @param x x-loc of the chunk
     * @param y y-loc of the chunk
     * @param z z-loc of the chunk
     * @return Return the chunk that was removed.
     */
    public Chunk removeChunkAt(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Chunk chunk = removeChunkAt(vec);
        VecPool.free(vec); //don't forget to free the vec!
        return chunk;
    }

    /**
     * Gets a chunk at a given location
     *
     * @param loc The location of the chunk
     * @return Return the chunk, or null if none exists
     */
    public Chunk getChunk(Vec3i loc) {
        return chunkLocMap.get(loc);
    }

    /**
     * Gets a chunk at a given location
     *
     * @param x x-loc of the chunk
     * @param y y-loc of the chunk
     * @param z z-loc of the chunk
     * @return return the chunk
     */
    public Chunk getChunk(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Chunk chunk = getChunk(vec);
        VecPool.free(vec); //don't forget to free the vec!
        return chunk;
    }

    /**
     * Get a list of all loaded chunks
     *
     * @return Return a list of all loaded chunks
     */
    public List<Chunk> getAllChunks() {
        return Collections.unmodifiableList(chunkList);
    }

    /**
     * Gets the world that these chunks belong to
     *
     * @return Return the world that the chunks belong to
     */
    public World getWorld() {
        return world;
    }
}
