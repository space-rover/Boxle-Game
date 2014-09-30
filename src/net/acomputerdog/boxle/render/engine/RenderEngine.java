package net.acomputerdog.boxle.render.engine;

import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.core.logger.CLogger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

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
        logger.logInfo("Initializing.");
        GameConfig config = boxle.getGameConfig();
        try {
            Display.setTitle("Boxle");
            Display.setFullscreen(config.fullscreen);
            Display.setResizable(true);
            Display.setDisplayMode(new DisplayMode(config.screenWidth, config.screenHeight));
            Display.setVSyncEnabled(config.enableVSync);
            Display.create();

            glClearColor(0.4f, 0.6f, 0.9f, 0f); //light blue background color
            GLU.gluPerspective(config.fov, (float) config.screenWidth / (float) config.screenHeight, 0.1f, (float) (Math.max(config.renderDistanceHorizontal, config.renderDistanceVertical) * Chunk.CHUNK_SIZE) + 1f);
            glLoadIdentity();
        } catch (LWJGLException e) {
            logger.logFatal("Exception initializing render engine!", e);
            getBoxle().stop();
        }
    }

    /**
     * Render a single frame.
     */
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear buffers
        glBegin(GL_TRIANGLES); //start update

        glEnd(); //end update
        Display.update(); //trigger render
        checkErrors(); //check for and print out any GL errors
    }

    /**
     * Checks for and prints out any openGL errors
     */
    private void checkErrors() {
        int error;
        while ((error = glGetError()) != 0) {
            logger.logWarning("GL Error " + error + ": " + Util.translateGLErrorString(error) + "!");
        }
    }

    /**
     * Cleanup and prepare for shutdown.
     */
    public void cleanup() {
        try {
            if (Display.isCreated()) Display.destroy(); //only destroy the display if it has been created
        } catch (Exception ignored) {
        }
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
}
