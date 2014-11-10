package net.acomputerdog.boxle.render.engine;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import net.acomputerdog.boxle.BlockFace;
import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;
import net.acomputerdog.core.logger.CLogger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private final Queue<Chunk> changedChunks = new ConcurrentLinkedQueue<>();

    /**
     * Input handler
     */
    private final InputHandler input = new InputHandler(this);

    Material grass;
    Material stone;
    Material dirt;

    /**
     * Creates a new instance of this RenderEngine.
     *
     * @param boxle The Boxle instance that created this RenderEngine.
     */
    public RenderEngine(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
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
        for (Chunk chunk : changedChunks) {
            if (chunk.isChanged()) {
                buildChunk(chunk);
                Node node = chunk.getChunkNode();
                if (node.getParent() == null) {
                    terrainNode.attachChild(node);
                }
                chunk.setChanged(false);
            } else {
                logger.logWarning("An unchanged chunk was marked as changed!");
            }
        }
    }

    public void buildChunk(Chunk chunk) {
        Node node = chunk.getChunkNode();
        BlockStorage storage = chunk.getBlocks();
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    Block block = storage.getBlock(x, y, z);
                    if (block != null && block.isRenderable()) {
                        BlockTex tex = block.getTextures();
                        if (isTransparent(x + 1, y, z, chunk)) {
                            addFace(node, tex, BlockFace.LEFT, x, y, z);
                        }
                        if (isTransparent(x - 1, y, z, chunk)) {
                            addFace(node, tex, BlockFace.RIGHT, x, y, z);
                        }
                        if (isTransparent(x, y + 1, z, chunk)) {
                            addFace(node, tex, BlockFace.TOP, x, y, z);
                        }
                        if (isTransparent(x, y - 1, z, chunk)) {
                            addFace(node, tex, BlockFace.BOTTOM, x, y, z);
                        }
                        if (isTransparent(x, y, z + 1, chunk)) {
                            addFace(node, tex, BlockFace.FRONT, x, y, z);
                        }
                        if (isTransparent(x, y, z - 1, chunk)) {
                            addFace(node, tex, BlockFace.BACK, x, y, z);
                        }
                    }
                }
            }
        }
    }

    private boolean isTransparent(int x, int y, int z, Chunk chunk) {
        if (x < chunkSize && x > 0 && y < chunkSize && y > 0 && z < chunkSize && z > 0) {
            Chunk newChunk = findNeighborChunk(x, y, z, chunk);
            if (newChunk == null) {
                return true;
            }
            Vec3i blockPos = findLocInNeighbor(x, y, z);
            Block newBlock = newChunk.getBlockAt(blockPos.x, blockPos.y, blockPos.z);
            VecPool.freeVec3i(blockPos);
            return newBlock == null || newBlock.isTransparent();
        }
        Block block = chunk.getBlockAt(x, y, z);
        return block == null || block.isTransparent();
    }

    private Chunk findNeighborChunk(int x, int y, int z, Chunk currChunk) {
        Vec3i loc = currChunk.getLocation().duplicate();
        if (x > chunkSize) loc.x += 1; //x - chunkSize;
        if (x < 0) loc.x -= 1; //0 - x;
        if (y > chunkSize) loc.y += 1; //y - chunkSize;
        if (y < 0) loc.y -= 1; //0 - y;
        if (z > chunkSize) loc.z += 1; //z - chunkSize;
        if (z < 0) loc.z -= 1; //0 - z;
        Chunk newChunk = currChunk.getWorld().getChunks().getChunk(loc);
        VecPool.freeVec3i(loc);
        return newChunk;
    }

    private Vec3i findLocInNeighbor(int x, int y, int z) {
        Vec3i vec = VecPool.getVec3i();
        vec.x = (x > chunkSize) ? 1 : x;
        vec.x = (x < 0) ? 15 : x;
        vec.y = (y > chunkSize) ? 1 : y;
        vec.y = (y < 0) ? 15 : y;
        vec.z = (z > chunkSize) ? 1 : z;
        vec.z = (z < 0) ? 15 : z;
        return vec;
    }

    private void addFace(Node node, BlockTex tex, BlockFace face, int x, int y, int z) {
        Geometry geom = new Geometry("face", new Quad(.5f, .5f));
        geom.setMaterial(tex.getFaceMat(face));
        geom.setLocalTranslation(x + face.xPos, y + face.yPos, z + face.zPos);
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
