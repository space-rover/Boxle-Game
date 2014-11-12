package net.acomputerdog.boxle.render.engine;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.block.BlockFace;
import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;
import net.acomputerdog.boxle.world.structure.ChunkTable;
import net.acomputerdog.core.logger.CLogger;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Central boxle render engine.
 * //"temp" render stuff IS temporary.  It exists just to provide a basic rendered object for testing.
 */
public class RenderEngine {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    /**
     * Boxle instance
     */
    private final Boxle boxle;

    /**
     * Logger for RenderEngine
     */
    private final CLogger logger = new CLogger("RenderEngine", false, true);

    /**
     * The root node for the JME render
     */
    private Node worldNode;

    /**
     * Base node for terrain data.
     */
    private Node terrainNode;

    private final Set<Chunk> changedChunks = new ConcurrentSkipListSet<>();

    private final Set<Chunk> rebuildChunks = new ConcurrentSkipListSet<>();

    private final GameConfig config;

    private int numFaces = 0;

    /**
     * Input handler
     */
    private final InputHandler input = new InputHandler(this);

    //Material grass;
    //Material stone;
    //Material dirt;

    /**
     * Creates a new instance of this RenderEngine.
     *
     * @param boxle The Boxle instance that created this RenderEngine.
     */
    public RenderEngine(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
        config = boxle.getGameConfig();
    }

    /**
     * Initializes this RenderEngine
     */
    public void init() {
        worldNode = boxle.getRootNode();
        terrainNode = new Node("terrain");
        worldNode.attachChild(terrainNode);

        input.init();

        //AssetManager manager = boxle.getAssetManager();

        //grass = Blocks.grass.getTextures().getTopMat();
        //dirt = Blocks.dirt.getTextures().getFrontMat();
        //stone = Blocks.stone.getTextures().getFrontMat();

        //tempLoadLevel();
    }

    public void render() {
        int numChunks = 0;
        numFaces = 0;
        for (Chunk chunk : rebuildChunks) {
            if (numChunks > config.maxRenderedChunksPerFrame) {
                return;
            }
            numChunks++;
            buildChunk(chunk, false);
            Node node = chunk.getChunkNode();
            terrainNode.attachChild(node);
            chunk.setChanged(false);
            rebuildChunks.remove(chunk);
            changedChunks.remove(chunk);
        }
        for (Chunk chunk : changedChunks) {
            if (numChunks > config.maxRenderedChunksPerFrame) {
                return;
            }
            if (chunk.isChanged()) {
                numChunks++;
                buildChunk(chunk, true);
                Node node = chunk.getChunkNode();
                terrainNode.attachChild(node);
                chunk.setChanged(false);
            } else {
                logger.logWarning("An unchanged chunk was marked as changed at " + chunk.getLocation().asCoords());
            }
            changedChunks.remove(chunk);
        }
        if (numChunks > 0) {
            logger.logDetail("Rendered " + numChunks + " chunks and " + numFaces + " faces.");
        }
    }

    public void buildChunk(Chunk chunk, boolean notifyNeighbors) {
        Node node = chunk.getChunkNode();
        terrainNode.detachChild(node);
        node.detachAllChildren();
        Vec3i cLoc = chunk.getLocation();
        //System.out.println("Building chunk at " + cLoc.asCoords());
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
        if (notifyNeighbors) {
            ChunkTable chunks = chunk.getWorld().getChunks();
            notifyNeighbor(cLoc, 1, 0, 0, chunks);
            notifyNeighbor(cLoc, -1, 0, 0, chunks);
            notifyNeighbor(cLoc, 0, 1, 0, chunks);
            notifyNeighbor(cLoc, 0, -1, 0, chunks);
            notifyNeighbor(cLoc, 0, 0, 1, chunks);
            notifyNeighbor(cLoc, 0, 0, -1, chunks);
            /*
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (!(x == 0 && y == 0 && z == 0)) {
                            Chunk nChunk = chunks.getChunk(x + cLoc.x, y + cLoc.y, z + cLoc.z);
                            if (nChunk != null) {
                                if (!changedChunks.contains(nChunk)) {
                                    buildChunk(nChunk, false);
                                    nChunk.setChanged(false);
                                    changedChunks.remove(nChunk);
                                }
                            }
                        }
                    }
                }
            }
            */
        }
        VecPool.free(cLoc);
        VecPool.free(gLoc);
    }

    private void notifyNeighbor(Vec3i cLoc, int x, int y, int z, ChunkTable chunks) {
        Chunk nChunk = chunks.getChunk(x + cLoc.x, y + cLoc.y, z + cLoc.z);
        if (nChunk != null && !rebuildChunks.contains(nChunk) && !changedChunks.contains(nChunk)) {
            //buildChunk(nChunk, false);
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
        if (x >= chunkSize) loc.x += 1; //x - chunkSize;
        if (x < 0) loc.x -= 1; //0 - x;
        if (y >= chunkSize) loc.y += 1; //y - chunkSize;
        if (y < 0) loc.y -= 1; //0 - y;
        if (z >= chunkSize) loc.z += 1; //z - chunkSize;
        if (z < 0) loc.z -= 1; //0 - z;
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
        /*
        vec.x = (x >= chunkSize) ? 0 : x;
        vec.x = (x < 0) ? 15 : x;
        vec.y = (y >= chunkSize) ? 0 : y;
        vec.y = (y < 0) ? 15 : y;
        vec.z = (z >= chunkSize) ? 0 : z;
        vec.z = (z < 0) ? 15 : z;
        */
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
     * Cleanup and prepare for shutdown.
     */
    public void cleanup() {
        logger.logInfo("Stopping!");
    }

    /**
     * Gets the Boxle that this RenderEngine belongs to.
     *
     * @return Return the Boxle that created this RenderEngine.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    /*
    private void tempLoadLevel() {
        Geometry[][][] chunk = new Geometry[16][16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    Geometry geom = null;
                    if (y < 6) {
                        geom = new Geometry("voxel_stone@" + x + "," + y + "," + z, new Box(.5f, .5f, .5f));
                        geom.setMaterial(stone);
                    } else if (y < 10) {
                        geom = new Geometry("voxel_dirt@" + x + "," + y + "," + z, new Box(.5f, .5f, .5f));
                        geom.setMaterial(dirt);
                    } else if (y == 10) {
                        geom = new Geometry("voxel_grass@" + x + "," + y + "," + z, new Box(.5f, .5f, .5f));
                        geom.setMaterial(grass);
                    }
                    if (geom != null) {
                        geom.setLocalTranslation(new Vector3f(x, y, z));
                        geom.updateModelBound();
                        chunk[x][y][z] = geom;
                    }
                }
            }
        }
        processChunk(chunk);
    }

    private void processChunk(Geometry[][][] chunk) {
        for (int x = 0; x < chunk.length; x++) {
            for (int y = 0; y < chunk[0].length; y++) {
                for (int z = 0; z < chunk[0][0].length; z++) {
                    if (chunk[x][y][z] != null && hasOpenSpace(chunk, x, y, z)) {
                        terrainNode.attachChild(chunk[x][y][z]);
                    }
                }
            }
        }
    }

    private boolean hasOpenSpace(Geometry[][][] chunk, int x, int y, int z) {
        int lengthX = chunk.length;
        int lengthY = chunk[0].length;
        int lengthZ = chunk[0][0].length;
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x + 1, y, z)) {
            return true;
        }
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x - 1, y, z)) {
            return true;
        }
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x, y + 1, z)) {
            return true;
        }
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x, y - 1, z)) {
            return true;
        }
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x, y, z + 1)) {
            return true;
        }
        if (isEmpty(chunk, lengthX, lengthY, lengthZ, x, y, z - 1)) {
            return true;
        }
        return false;
    }

    private boolean isEmpty(Geometry[][][] chunk, int maxX, int maxY, int maxZ, int x, int y, int z) {
        return x < 0 || x >= maxX || y < 0 || y >= maxY || z < 0 || z >= maxZ || chunk[x][y][z] == null;
    }
    */

    public Node getWorldNode() {
        return worldNode;
    }

    public InputHandler getInputHandler() {
        return input;
    }

    public void addChangedChunk(Chunk chunk) {
        if (chunk == null) {
            logger.logWarning("Null chunk passed to render engine!");
            return;
        }
        changedChunks.add(chunk);
    }
}
