package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.math.vec.Vec3i;

/**
 * A 16 by 16 chunk of a world
 */
public class Chunk {

    /**
     * The axis-size of a chunk.
     */
    public static final int CHUNK_SIZE = 16;

    /**
     * The total volume of a chunk.
     */
    public static final int CHUNK_VOLUME = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;

    /**
     * The world that contains this Chunk.
     */
    private final World world;

    /**
     * The x,y,z location of this chunk
     */
    private final Vec3i location;

    /**
     * Creates a new chunk.
     *
     * @param world The world that contains the chunk
     * @param location The x,y,z location of this Chunk
     */
    public Chunk(World world, Vec3i location) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        if (location == null) throw new IllegalArgumentException("Location cannot be null!");
        this.world = world;
        this.location = location;
    }

    /**
     * Gets the world that contains this chunk.
     *
     * @return Returns the world that contains this chunk.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the x,y,z location of this chunk
     *
     * @return Return a Vec3i defining the location of this Chunk
     */
    public Vec3i getLocation() {
        return location.duplicate();
    }
}
