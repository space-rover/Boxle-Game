package net.acomputerdog.boxle.render.engine;

import com.jme3.scene.Node;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.render.util.ChunkNode;
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

    private final Set<ChunkNode> addNodes = new ConcurrentSkipListSet<>();
    private final Set<ChunkNode> removeNodes = new ConcurrentSkipListSet<>();

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
        terrainNode = new ChunkNode("terrain");
        worldNode.attachChild(terrainNode);
        input.init();
    }

    public void render() {
        for (ChunkNode node : removeNodes) {
            removeNodes.remove(node);
            terrainNode.detachChild(node);
        }
        for (ChunkNode node : addNodes) {
            addNodes.remove(node);
            terrainNode.attachChild(node);
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

    public void addNode(ChunkNode node) {
        if (node == null) {
            logger.logWarning("Null node passed to render engine!");
            return;
        }
        addNodes.add(node);
    }

    public void removeNode(ChunkNode node) {
        if (node == null) {
            logger.logWarning("Null node passed to render engine!");
            return;
        }
        removeNodes.add(node);
    }

    public Node getTerrainNode() {
        return terrainNode;
    }
}
