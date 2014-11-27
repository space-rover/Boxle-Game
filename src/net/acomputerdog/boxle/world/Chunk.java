package net.acomputerdog.boxle.world;

import com.jme3.scene.Node;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.main.Boxle;
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

    private boolean needsRebuild = true;

    private ChunkNode chunkNode;

    private boolean isGenerated = false;
    private boolean isDecorated = false;

    private boolean isModifiedFromLoad = false;

    private Node collisionNode;

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
        collisionNode = new Node("chunkC@" + location.asCoords());
        world.getWorldCollisionNode().attachChild(collisionNode);
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
        setBlockAt(x, y, z, block, false);
    }

    /**
     * Sets the block at a location
     *
     * @param x     X-location
     * @param y     Y-location
     * @param z     Z-location
     * @param block The block to set.  Cannot be null.
     */
    public void setBlockAt(int x, int y, int z, Block block, boolean instant) {
        blocks.setBlock(x, y, z, block);
        if (!instant) {
            setNeedsRebuild(true);
        } else {
            Boxle.instance().getRenderEngine().addUpdateChunk(this);
        }
        chunkNode.detachChildNamed("blockC@" + x + "_" + y + "_" + z);
        if (block.isCollidable()) {
            chunkNode.attachChild(new Node("blockC@" + x + "_" + y + "_" + z));
        }
        setModifiedFromLoad(true);
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

    public boolean needsRebuild() {
        return needsRebuild;
    }

    public void setNeedsRebuild(boolean needsRebuild) {
        this.needsRebuild = needsRebuild;
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
        setBlockAt(loc, block, false);
    }

    public void setBlockAt(Vec3i loc, Block block, boolean instant) {
        setBlockAt(loc.x, loc.y, loc.z, block, instant);
    }

    public void clear(Block block) {
        clear(block, false);
    }

    public void clear(Block block, boolean instant) {
        chunkNode.detachAllChildren();
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    setBlockAt(x, y, z, block, instant);
                }
            }
        }
        /*
        blocks.clear(block);
        if (!instant) {
            setNeedsRebuild(true);
        } else {
            Boxle.instance().getRenderEngine().addUpdateChunk(this);
        }
        if (block.isCollidable()) {
            chunkNode.attachChild(new Node("blockC@" + x + "_" + y + "_" + z));
        }
        setModifiedFromLoad(true);
        */
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void markGenerated() {
        this.isGenerated = true;
    }

    public boolean isDecorated() {
        return isDecorated;
    }

    public void markDecorated() {
        this.isDecorated = true;
    }

    public int getXLoc() {
        return location.x;
    }

    public int getYLoc() {
        return location.y;
    }

    public int getZLoc() {
        return location.z;
    }

    public int getGroundHeight(int x, int z) {
        if (x >= CHUNK_SIZE || z >= CHUNK_SIZE || x < 0 || z < 0) {
            throw new IllegalArgumentException("Cannot get a ground height out of chunk bounds!");
        }
        for (int y = CHUNK_SIZE - 1; y >= 0; y--) {
            if (!blocks.getBlock(x, y, z).isTransparent()) {
                return y;
            }
        }
        return -1;
    }

    public String getCoords() {
        return location.asCoords();
    }


    public boolean isModifiedFromLoad() {
        return isModifiedFromLoad;
    }

    public void setModifiedFromLoad(boolean isModifiedFromLoad) {
        this.isModifiedFromLoad = isModifiedFromLoad;
    }

    public Node getCollisionNode() {
        return collisionNode;
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
