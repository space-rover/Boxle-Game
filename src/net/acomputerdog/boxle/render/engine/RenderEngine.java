package net.acomputerdog.boxle.render.engine;

import com.jme3.scene.Node;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.core.logger.CLogger;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Central boxle render engine.
 */
public class RenderEngine {

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

    private final GameConfig config;

    /**
     * Input handler
     */
    private final InputHandler input = new InputHandler(this);

    private final Set<Chunk> addChunks = new ConcurrentSkipListSet<>();
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
        for (Chunk chunk : addChunks) {
            Node node = chunk.getChunkNode();
            terrainNode.attachChild(node);
            chunk.setChanged(false);
            addChunks.remove(chunk);
        }
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

    public Node getWorldNode() {
        return worldNode;
    }

    public InputHandler getInputHandler() {
        return input;
    }

    public void addRenderChunk(Chunk chunk) {
        if (chunk == null) {
            logger.logWarning("Null chunk passed to render engine!");
            return;
        }
        addChunks.add(chunk);
    }

    public Node getTerrainNode() {
        return terrainNode;
    }
}
