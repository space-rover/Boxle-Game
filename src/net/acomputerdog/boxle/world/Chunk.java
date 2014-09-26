package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.structure.BlockStorage;

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
     * Blocks and data for this Chunk
     */
    private final BlockStorage blocks;

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
        blocks = new BlockStorage(Chunk.CHUNK_SIZE);
    }

    /**
     * Gets the block at a location
     * @param x X-location
     * @param y Y-location
     * @param z Z-location
     * @return Return the block at the given location
     */
    public Block getBlockAt(int x, int y, int z) {
        return blocks.getBlock(x, y, z);
    }

    /**
     * Gets the data value at a location
     *
     * @param x X-location
     * @param y Y-location
     * @param z Z-location
     * @return Return the data value
     */
    public byte getDataAt(int x, int y, int z) {
        return blocks.getData(x, y, z);
    }

    /**
     * Sets the block at a location
     *
     * @param x     X-location
     * @param y     Y-location
     * @param z     Z-location
     * @param block The block to set.  Cannot be null.
     */
    public void setBlockAt(int x, int y, int z, Block block) {
        blocks.setBlock(x, y, z, block);
    }

    /**
     * Sets a data value at a location
     *
     * @param x    X-location
     * @param y    Y-location
     * @param z    Z-location
     * @param data The data value to set.
     */
    public void setDataAt(int x, int y, int z, byte data) {
        blocks.setData(x, y, z, data);
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

    /**
     * Gets the BlockStorage for this Chunk
     *
     * @return Return the BlockStorage for this Chunk
     */
    public BlockStorage getBlocks() {
        return blocks;
    }
}
