package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.entity.Entity;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.physics.PhysicsEngine;
import net.acomputerdog.boxle.render.util.ChunkNode;
import net.acomputerdog.boxle.save.Region;
import net.acomputerdog.boxle.save.Regions;
import net.acomputerdog.boxle.save.SaveManager;
import net.acomputerdog.boxle.world.gen.CellsWorldGen;
import net.acomputerdog.boxle.world.gen.WorldGen;
import net.acomputerdog.boxle.world.gen.structures.Structures;
import net.acomputerdog.boxle.world.structure.ChunkTable;
import net.acomputerdog.core.logger.CLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private final CLogger logger;

    private WorldGen generator;

    private final Queue<Chunk> decorateChunks = new ConcurrentLinkedQueue<>();

    private final Map<Integer, Entity> entities = new HashMap<>();
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
        logger = new CLogger("World_" + name, false, true);

        generator = new CellsWorldGen(name.hashCode());
        //generator = new SimplexWorldGen(name.hashCode());
        generator.addDecoration(Structures.tree);
        //System.out.println("Creating world: " + name);
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
            Region region = Regions.getRegion(this, loc.x, loc.y, loc.z);
            if (region == null || region.hasChunkGlobal(loc)) {
                //System.out.println("Attempting to load chunk at " + loc.asCoords());
                SaveManager.loadChunkDelayed(this, loc);
            } else {
                return createNewChunk(loc);
            }
        }
        return chunk;
    }

    //todo make into a threaded queue
    public Chunk createNewChunk(Vec3i loc) {
        //System.out.println("Creating new chunk at " + loc.asCoords());
        Chunk chunk = new Chunk(this, loc);
        generator.generateTerrain(chunk);
        decorateChunks.add(chunk);
        chunks.addChunk(chunk);
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
        SaveManager.saveChunkDelayed(chunk);
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
        setBlockAt(x, y, z, block, false);
    }

    public void setBlockAt(Vec3i loc, Block block) {
        setBlockAt(loc, block, false);
    }

    public void setBlockAt(int x, int y, int z, Block block, boolean instant) {
        Vec3i vec = VecPool.getVec3i(x, y, z);
        setBlockAt(vec, block, instant);
        VecPool.free(vec);
    }

    public void setBlockAt(Vec3i loc, Block block, boolean instant) {
        Vec3i cLoc = CoordConverter.globalToChunk(loc.duplicate());
        Chunk chunk = chunks.getChunk(cLoc);
        if (chunk != null) {
            Vec3i bLoc = CoordConverter.globalToBlock(loc.duplicate());
            chunk.setBlockAt(bLoc, block, instant);
            VecPool.free(bLoc);
        }
        VecPool.free(cLoc);
    }

    public CLogger getLogger() {
        return logger;
    }

    public void spawnEntity(Entity entity) {
        entities.put(entity.getEntityID(), entity);
        entity.onSpawn();
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public boolean containsEntity(int id) {
        return entities.containsKey(id);
    }

    public boolean containsEntity(Entity entity) {
        return entities.containsKey(entity.getEntityID());
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public Queue<Chunk> getDecorateChunks() {
        return decorateChunks;
    }

    public void addNewChunk(Chunk chunk) {
        //System.out.println("Chunk at " + chunk.asCoords() + " has been loaded.");
        chunk.setModifiedFromLoad(false);
        chunk.setNeedsRebuild(true);
        chunks.addChunk(chunk);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World)) return false;

        World world = (World) o;

        return name.equals(world.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "World{" +
                "name='" + name + '\'' +
                '}';
    }
}
