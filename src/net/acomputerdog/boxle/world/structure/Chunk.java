package net.acomputerdog.boxle.world.structure;

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
     * Creates a new chunk.
     *
     * @param world The world that contains the chunk
     */
    public Chunk(World world) {
        this.world = world;
    }

    /**
     * Gets the world that contains this chunk.
     *
     * @return Returns the world that contains this chunk.
     */
    public World getWorld() {
        return world;
    }
}
