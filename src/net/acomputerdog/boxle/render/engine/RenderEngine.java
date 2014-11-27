package net.acomputerdog.boxle.render.engine;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.gui.types.GuiCrossHair;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.util.ChunkNode;
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
    private Node rootNode;

    /**
     * Base node for terrain data.
     */
    private Node terrainNode;

    private Node guiNode;

    private final GameConfig config;

    /**
     * Input handler
     */
    private final InputHandler input = new InputHandler(this);

    private final Set<ChunkNode> addNodes = new ConcurrentSkipListSet<>();
    private final Set<ChunkNode> removeNodes = new ConcurrentSkipListSet<>();

    private final Set<Chunk> updateChunks = new ConcurrentSkipListSet<>();

    private AmbientLight ambience;
    private DirectionalLight sun;

    private GuiCrossHair crossHairs;

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
        rootNode = boxle.getRootNode();
        terrainNode = new ChunkNode("terrain");
        rootNode.attachChild(terrainNode);

        guiNode = boxle.getGuiNode();
        crossHairs = new GuiCrossHair();
        crossHairs.render(guiNode);

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(0.5f, -1f, .5f).normalizeLocal());
        sun.setName("Sun");
        ambience = new AmbientLight();
        ambience.setColor(ColorRGBA.White.mult(2f));
        ambience.setName("Ambiance");
        if (config.lightingMode >= 1) {
            rootNode.addLight(sun);
            rootNode.addLight(ambience);
        }
        rootNode.setShadowMode(config.shadowMode > 0 ? RenderQueue.ShadowMode.CastAndReceive : RenderQueue.ShadowMode.Off);
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
        for (Chunk chunk : updateChunks) {
            updateChunks.remove(chunk);
            ChunkNode oldNode = chunk.getChunkNode();
            terrainNode.detachChild(oldNode);
            removeNodes.remove(oldNode);
            addNodes.remove(oldNode);

            Vec3i cLoc = chunk.getLocation();
            Vec3i gLoc = CoordConverter.chunkToGlobal(cLoc.duplicate());
            ChunkNode node = new ChunkNode("chunk@" + cLoc.asCoords());
            ChunkRenderer.buildChunkMesh(gLoc, chunk, node);
            chunk.setChunkNode(node);
            terrainNode.attachChild(node);

            VecPool.free(gLoc);
            VecPool.free(cLoc);
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

    public Node getRootNode() {
        return rootNode;
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

    public void addUpdateChunk(Chunk chunk) {
        if (chunk == null) {
            logger.logWarning("Null chunk passed to RenderEngine!");
            return;
        }
        updateChunks.add(chunk);
    }

    public Node getTerrainNode() {
        return terrainNode;
    }

    public AmbientLight getAmbience() {
        return ambience;
    }

    public DirectionalLight getSun() {
        return sun;
    }
}
