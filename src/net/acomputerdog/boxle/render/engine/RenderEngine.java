package net.acomputerdog.boxle.render.engine;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import net.acomputerdog.boxle.input.InputHandler;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.core.logger.CLogger;

/**
 * Central boxle render engine.
 * //"temp" render stuff IS temporary.  It exists just to provide a basic rendered object for testing.
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

        AssetManager manager = boxle.getAssetManager();

        grass = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        grass.setColor("Color", ColorRGBA.Green);
        stone = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        stone.setColor("Color", ColorRGBA.Gray);
        dirt = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        dirt.setColor("Color", ColorRGBA.Brown);

        tempLoadLevel();
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
}
