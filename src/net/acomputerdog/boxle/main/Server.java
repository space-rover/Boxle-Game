package net.acomputerdog.boxle.main;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import jme3tools.optimize.GeometryBatchFactory;
import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.block.BlockFace;
import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.ChunkNode;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.structure.BlockStorage;
import net.acomputerdog.boxle.world.structure.ChunkTable;
import net.acomputerdog.core.logger.CLogger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Boxle Server instance
 */
public class Server {
    private static final int chunkSize = Chunk.CHUNK_SIZE;
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    private final Set<World> hostedWorlds = new CopyOnWriteArraySet<>();

    private World defaultWorld;

    private final GameConfig config;

    final int renderDistanceH;
    final int renderDistanceV;

    private final Set<Chunk> rebuildChunks = new ConcurrentSkipListSet<>();

    private int numFaces = 0;
    private int numChunks = 0;
    private int numUnload = 0;

    private final CLogger logger;

    private final RenderEngine engine;

    /**
     * Create a new Server instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Server(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
        config = boxle.getGameConfig();
        renderDistanceH = config.renderDistanceHorizontal;
        renderDistanceV = config.renderDistanceVertical;
        logger = new CLogger("Server", false, true);
        engine = boxle.getRenderEngine();
    }

    /**
     * Initializes this server
     */
    public void init() {
        defaultWorld = new World(boxle, "server_default");
        boxle.getWorlds().addWorld(defaultWorld);
        hostedWorlds.add(defaultWorld);
    }

    /**
     * Ticks this server
     */
    public void tick() {
        long oldTime = System.currentTimeMillis();
        numFaces = 0;
        numChunks = 0;
        numUnload = 0;
        rebuildNeighborChunks();
        rebuildChangedChunks();
        unloadExtraChunks();

        if (config.outputRenderDebugInfo && numChunks > 0) {
            long newTime = System.currentTimeMillis();
            logger.logDetail("Built " + numChunks + " chunk meshes from " + numFaces + " faces and unloaded " + numUnload + " chunks in " + ((newTime - oldTime) / 1000f) + " seconds.");
        }

    }

    private void unloadExtraChunks() {
        for (World world : hostedWorlds) {
            Vec3i center = CoordConverter.globalToChunk(VecConverter.vec3iFromVec3f(boxle.getClient().getPlayer().getLocation(), VecPool.createVec3i()));
            ChunkTable chunks = world.getChunks();
            for (Chunk chunk : chunks.getAllChunks()) {
                Vec3i cLoc = chunk.getLocation();
                if (cLoc.x > center.x + renderDistanceH || cLoc.x < center.x - renderDistanceH || cLoc.y > center.y + renderDistanceV || cLoc.y < center.y - renderDistanceV || cLoc.z > center.z + renderDistanceH || cLoc.z < center.z - renderDistanceH) {
                    numUnload++;
                    rebuildChunks.remove(chunk);
                    world.unloadChunk(chunk);
                }
            }
            VecPool.free(center);
        }
    }

    private void rebuildNeighborChunks() {
        for (Chunk chunk : rebuildChunks) {
            rebuildChunks.remove(chunk);
            numChunks++;
            Vec3i cLoc = chunk.getLocation();
            ChunkNode node = new ChunkNode("chunk@" + cLoc.asCoords());
            VecPool.free(cLoc);
            buildChunk(chunk, node, false);
            ChunkNode oldNode = chunk.getChunkNode();
            if (oldNode.getParent() != null) {
                engine.removeNode(oldNode);
            }
            chunk.setChunkNode(node);
            engine.addNode(node);
            if (numChunks > config.maxLoadedChunksPerTick) {
                return;
            }
        }
    }

    private void rebuildChangedChunks() {
        EntityPlayer player = boxle.getClient().getPlayer();
        World world = player.getWorld();
        ChunkTable chunks = world.getChunks();
        Vec3i center = CoordConverter.globalToChunk(VecConverter.vec3iFromVec3f(player.getLocation(), VecPool.createVec3i()));
        GameConfig config = boxle.getGameConfig();
        chunkLimitReached:
        for (int y = -renderDistanceV; y <= renderDistanceV; y++) {
            for (int x = -renderDistanceH; x <= renderDistanceH; x++) {
                for (int z = -renderDistanceH; z <= renderDistanceH; z++) {
                    if (numChunks > config.maxLoadedChunksPerTick) {
                        break chunkLimitReached;
                    }
                    Vec3i newLoc = VecPool.getVec3i(center.x + x, center.y + y, center.z + z);
                    Chunk chunk = chunks.getChunk(newLoc);
                    if (chunk == null) {
                        chunk = world.loadOrGenerateChunk(newLoc);
                    }
                    if (chunk.isChanged()) {
                        numChunks++;
                        rebuildChunks.remove(chunk); //make sure the chunk is not rendered twice
                        ChunkNode node = new ChunkNode("chunk@" + newLoc.asCoords());
                        buildChunk(chunk, node, true);
                        ChunkNode oldNode = chunk.getChunkNode();
                        if (oldNode.getParent() != null) {
                            engine.removeNode(chunk.getChunkNode());
                        }
                        chunk.setChunkNode(node);
                        engine.addNode(node);
                    }
                    VecPool.free(newLoc);
                }
            }
        }
        VecPool.free(center);
    }


    public void buildChunk(Chunk chunk, Node node, boolean notifyNeighbors) {
        chunk.setChanged(false);
        Vec3i cLoc = chunk.getLocation();
        Vec3i gLoc = CoordConverter.chunkToGlobal(cLoc.duplicate());
        BlockStorage storage = chunk.getBlocks();
        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                for (int z = 0; z < chunkSize; z++) {
                    Block block = storage.getBlock(x, y, z);
                    if (block != null && block.isRenderable()) {
                        BlockTex tex = block.getTextures();
                        if (isTransparent(x + 1, y, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.RIGHT, x, y, z);
                        }
                        if (isTransparent(x - 1, y, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.LEFT, x, y, z);
                        }
                        if (isTransparent(x, y + 1, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.TOP, x, y, z);
                        }
                        if (isTransparent(x, y - 1, z, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.BOTTOM, x, y, z);
                        }
                        if (isTransparent(x, y, z + 1, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.FRONT, x, y, z);
                        }
                        if (isTransparent(x, y, z - 1, chunk)) {
                            addFace(node, tex, gLoc, BlockFace.BACK, x, y, z);
                        }
                    }
                }
            }
        }
        GeometryBatchFactory.optimize(node); //enhance!
        if (notifyNeighbors) {
            if (config.notifyNeighborsMode >= 0) {
                ChunkTable chunks = chunk.getWorld().getChunks();
                //immediate neighbors
                notifyNeighbor(cLoc, 1, 0, 0, chunks);
                notifyNeighbor(cLoc, -1, 0, 0, chunks);
                notifyNeighbor(cLoc, 0, 1, 0, chunks);
                notifyNeighbor(cLoc, 0, -1, 0, chunks);
                notifyNeighbor(cLoc, 0, 0, 1, chunks);
                notifyNeighbor(cLoc, 0, 0, -1, chunks);
                if (config.notifyNeighborsMode >= 1) {
                    //edges
                    notifyNeighbor(cLoc, 1, 1, 0, chunks);
                    notifyNeighbor(cLoc, 1, -1, 0, chunks);
                    notifyNeighbor(cLoc, -1, 1, 0, chunks);
                    notifyNeighbor(cLoc, -1, -1, 0, chunks);
                    notifyNeighbor(cLoc, 1, 0, 1, chunks);
                    notifyNeighbor(cLoc, 1, 0, -1, chunks);
                    notifyNeighbor(cLoc, -1, 0, 1, chunks);
                    notifyNeighbor(cLoc, 1, 0, -1, chunks);
                    notifyNeighbor(cLoc, 0, 1, 1, chunks);
                    notifyNeighbor(cLoc, 0, -1, 1, chunks);
                    notifyNeighbor(cLoc, 0, 1, -1, chunks);
                    notifyNeighbor(cLoc, 0, -1, -1, chunks);
                    if (config.notifyNeighborsMode >= 2) {
                        //corners
                        notifyNeighbor(cLoc, 1, 1, 1, chunks);
                        notifyNeighbor(cLoc, 1, 1, -1, chunks);
                        notifyNeighbor(cLoc, -1, 1, 1, chunks);
                        notifyNeighbor(cLoc, -1, 1, -1, chunks);
                        notifyNeighbor(cLoc, 1, -1, 1, chunks);
                        notifyNeighbor(cLoc, 1, -1, -1, chunks);
                        notifyNeighbor(cLoc, -1, -1, 1, chunks);
                        notifyNeighbor(cLoc, -1, -1, -1, chunks);
                        if (config.notifyNeighborsMode >= 3) {
                            //2nd layer immediates
                            notifyNeighbor(cLoc, 2, 0, 0, chunks);
                            notifyNeighbor(cLoc, -2, 0, 0, chunks);
                            notifyNeighbor(cLoc, 0, 2, 0, chunks);
                            notifyNeighbor(cLoc, 0, -2, 0, chunks);
                            notifyNeighbor(cLoc, 0, 0, 2, chunks);
                            notifyNeighbor(cLoc, 0, 0, -2, chunks);
                        }
                    }
                }
            }
        }
        VecPool.free(cLoc);
        VecPool.free(gLoc);
    }

    private void notifyNeighbor(Vec3i cLoc, int x, int y, int z, ChunkTable chunks) {
        Chunk nChunk = chunks.getChunk(x + cLoc.x, y + cLoc.y, z + cLoc.z);
        if (nChunk != null && !rebuildChunks.contains(nChunk)) {
            rebuildChunks.add(nChunk);
            nChunk.setChanged(false);
        }
    }

    private boolean isTransparent(int x, int y, int z, Chunk chunk) {
        if (x >= chunkSize || x < 0 || y >= chunkSize || y < 0 || z >= chunkSize || z < 0) {
            Chunk newChunk = findNeighborChunk(x, y, z, chunk);
            if (newChunk == null) {
                return true;
            }
            Vec3i blockPos = findLocInNeighbor(x, y, z);
            Block newBlock = newChunk.getBlockAt(blockPos.x, blockPos.y, blockPos.z);
            VecPool.free(blockPos);
            return newBlock == null || newBlock.isTransparent();
        }
        Block block = chunk.getBlockAt(x, y, z);
        return block == null || block.isTransparent();
    }

    private Chunk findNeighborChunk(int x, int y, int z, Chunk currChunk) {
        Vec3i loc = currChunk.getLocation();
        if (x >= chunkSize) loc.x += 1;
        if (x < 0) loc.x -= 1;
        if (y >= chunkSize) loc.y += 1;
        if (y < 0) loc.y -= 1;
        if (z >= chunkSize) loc.z += 1;
        if (z < 0) loc.z -= 1;
        Chunk newChunk = currChunk.getWorld().getChunks().getChunk(loc);
        VecPool.free(loc);
        return newChunk;
    }

    private Vec3i findLocInNeighbor(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i();
        if (x >= chunkSize) {
            vec.x = 0;
        } else if (x < 0) {
            vec.x = chunkSize - 1;
        } else {
            vec.x = x;
        }
        if (y >= chunkSize) {
            vec.y = 0;
        } else if (y < 0) {
            vec.y = chunkSize - 1;
        } else {
            vec.y = y;
        }
        if (z >= chunkSize) {
            vec.z = 0;
        } else if (z < 0) {
            vec.z = chunkSize - 1;
        } else {
            vec.z = z;
        }
        return vec;
    }

    private void addFace(Node node, BlockTex tex, Vec3i cLoc, BlockFace face, int x, int y, int z) {
        numFaces++;
        Geometry geom = new Geometry("face", new Quad(1f, 1f));
        geom.setMaterial(tex.getFaceMat(face));
        geom.setLocalTranslation(cLoc.x + x + face.xPos, cLoc.y + y + face.yPos, cLoc.z + z + face.zPos);
        geom.rotate(face.xRot, face.yRot, face.zRot);
        geom.updateModelBound();
        node.attachChild(geom);
    }

    /**
     * Shuts down this server
     */
    public void shutdown() {

    }

    /**
     * Get the boxle instance of this Server.
     *
     * @return Return the boxle instance of this Server.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    public World getDefaultWorld() {
        return defaultWorld;
    }

    public Set<World> getHostedWorlds() {
        return Collections.unmodifiableSet(hostedWorlds);
    }
}
