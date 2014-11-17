package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.util.ChunkNode;
import net.acomputerdog.boxle.world.structure.BlockStorage;
import net.acomputerdog.boxle.world.structure.block.SimpleBlockStorage;

/**
 * A 16 by 16 chunk of a world
 */
public class Chunk implements Comparable<Chunk> {

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

    private boolean isChanged = true;

    private ChunkNode chunkNode;

    /**
     * Creates a new chunk.
     *
     * @param world    The world that contains the chunk
     * @param location The x,y,z location of this Chunk
     */
    public Chunk(World world, Vec3i location) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        if (location == null) throw new IllegalArgumentException("Location cannot be null!");
        this.world = world;
        this.location = VecPool.createVec3i(location); //new one needed for hashing stuff
        blocks = new SimpleBlockStorage(this);
        chunkNode = new ChunkNode("chunk@" + location.asCoords());
    }

    /**
     * Gets the block at a location
     *
     * @param x X-location
     * @param y Y-location
     * @param z Z-location
     * @return Return the block at the given location
     */
    public Block getBlockAt(int x, int y, int z) {
        return blocks.getBlock(x, y, z);
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
        setChanged(true);
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

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        this.isChanged = changed;
    }

    public ChunkNode getChunkNode() {
        return chunkNode;
    }

    public void setChunkNode(ChunkNode node) {
        chunkNode = node;
    }

    public Block getBlockAt(Vec3i loc) {
        return getBlockAt(loc.x, loc.y, loc.z);
    }

    public void setBlockAt(Vec3i loc, Block block) {
        setBlockAt(loc.x, loc.y, loc.z, block);
    }

    public void clear(Block block) {
        blocks.clear(block);
        setChanged(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk)) return false;

        Chunk chunk = (Chunk) o;

        return location.equals(chunk.location);

    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @Override
    public int compareTo(Chunk o) {
        return this.hashCode() - o.hashCode();
    }
}
