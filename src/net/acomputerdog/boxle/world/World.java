package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.legacy.Blocks;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.physics.PhysicsEngine;
import net.acomputerdog.boxle.render.util.ChunkNode;
import net.acomputerdog.boxle.world.gen.CachingWorldGen;
import net.acomputerdog.boxle.world.gen.CellsWorldGen;
import net.acomputerdog.boxle.world.gen.WorldGen;
import net.acomputerdog.boxle.world.structure.ChunkTable;

/**
 * A world, made of blocks :)
 */
public class World {
    /**
     * Boxle instance.
     */
    private final Boxle boxle;

    /**
     * Name of this world.
     */
    private final String name;

    /**
     * The physics engine for this world.
     */
    private final PhysicsEngine physicsEngine;

    /**
     * ChunkTable containing all loaded chunks in this world.
     */
    private final ChunkTable chunks;

    private WorldGen generator;

    /**
     * Creates a new instance of this World.
     *
     * @param boxle The Boxle instance that created this World.
     * @param name  The name of this world.
     */
    public World(Boxle boxle, String name) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null!");
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
        this.name = name;
        physicsEngine = new PhysicsEngine(this);
        chunks = new ChunkTable(this);
        //generator = new DebugWorldGen(this);
        //generator = new AngleWorldGen();
        //generator = new CachingWorldGen(new SinWorldGen());
        //generator = new CachingWorldGen(new SimplexWorldGen(1234L));
        generator = new CachingWorldGen(new CellsWorldGen(name.hashCode()));
    }

    /**
     * Gets the Boxle that this World belongs to.
     *
     * @return Return the Boxle that created this World.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    /**
     * Gets the name of this world.
     *
     * @return Return the name of this world.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the physics engine for this world.
     *
     * @return Return the physics engine for this world.
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * Gets the ChunkTable containing all chunks loaded by this World.
     *
     * @return Return the ChunkTable of this World
     */
    public ChunkTable getChunks() {
        return chunks;
    }

    public Chunk loadOrGenerateChunk(Vec3i loc) {
        Chunk chunk = chunks.getChunk(loc);
        if (chunk == null) {
            chunk = new Chunk(this, loc);
            generator.generateChunk(chunk);
            chunks.addChunk(chunk);
        }
        return chunk;
    }

    public WorldGen getGenerator() {
        return generator;
    }

    public void setGenerator(WorldGen generator) {
        this.generator = generator;
    }

    public void unloadChunk(Chunk chunk) {
        chunks.removeChunk(chunk);
        ChunkNode oldNode = chunk.getChunkNode();
        if (oldNode.getParent() != null) {
            boxle.getRenderEngine().removeNode(oldNode);
        }
    }

    public Block getBlockAt(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        Block block = getBlockAt(vec);
        VecPool.free(vec);
        return block;
    }

    public Block getBlockAt(Vec3i loc) {
        Vec3i cLoc = CoordConverter.globalToChunk(loc.duplicate());
        Chunk chunk = chunks.getChunk(cLoc);
        Block block = null;
        if (chunk != null) {
            Vec3i bLoc = CoordConverter.globalToBlock(loc.duplicate());
            block = chunk.getBlockAt(bLoc);
            VecPool.free(bLoc);
        }
        VecPool.free(cLoc);
        return block == null ? Blocks.air : block;
    }

    public void setBlockAt(int x, int y, int z, Block block) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        setBlockAt(vec, block);
        VecPool.free(vec);
    }

    public void setBlockAt(Vec3i loc, Block block) {
        Vec3i cLoc = CoordConverter.globalToChunk(loc.duplicate());
        Chunk chunk = chunks.getChunk(cLoc);
        if (chunk != null) {
            Vec3i bLoc = CoordConverter.globalToBlock(loc.duplicate());
            chunk.setBlockAt(bLoc, block);
            VecPool.free(bLoc);
        }
        VecPool.free(cLoc);
    }
}
