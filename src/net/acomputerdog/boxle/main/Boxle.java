package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.logger.CLogger;
import org.lwjgl.opengl.Display;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Boxle main class
 */
public class Boxle {
    /**
     * Logger that logs without date or time.  Useful for high-output debug messages.
     */
    public static final CLogger LOGGER_FAST = new CLogger("Boxle", false, false);
    /**
     * Normal logger that just logs time.
     */
    public static final CLogger LOGGER_MAIN = new CLogger("Boxle", false, true);
    /**
     * Full logger that outputs date and time.  Useful for crash messages.
     */
    public static final CLogger LOGGER_FULL = new CLogger("Boxle", true, true);

    /**
     * Central render engine
     */
    private final RenderEngine renderEngine;

    /**
     * Map of world names to instances.
     */
    private final Map<String, World> worldMap;

    private final GameConfig gameConfig;

    /**
     * If true the game can run, if false, a close has been requested.
     */
    private boolean canRun = true;

    /**
     * Creates a new Boxle instance
     */
    private Boxle() {
        this.renderEngine = new RenderEngine(this);
        worldMap = new ConcurrentHashMap<>();
        gameConfig = new GameConfig(this);
        try {
            init();
        } catch (Throwable t) {
            LOGGER_FULL.logFatal("Caught exception during init phase!", t);
            end(-1);
        }
        try {
            run();
        } catch (Throwable t) {
            LOGGER_FULL.logFatal("Caught exception during run phase!", t);
            end(-1);
        }
    }

    /**
     * Initializes boxle to start.
     */
    private void init() {
        LOGGER_FULL.logInfo("Boxle is initializing.");
    }

    /**
     * Performs actual game loop.
     */
    private void run() {
        LOGGER_FULL.logInfo("Boxle is starting.");
        while (canRun) {
            Display.sync(gameConfig.maxFPS); //limit the tick speed to max FPS
        }
        end(0);
    }

    /**
     * Saves and exits.
     * @param code The error code to return.
     */
    private void end(int code) {
        if (code == 0) {
            LOGGER_FULL.logInfo("Boxle shutting down normally.");
        } else {
            LOGGER_FULL.logWarning("Boxle shutting down abnormally: error code " + code + ".");
        }
        System.exit(code);
    }

    /**
     * Gets the RenderEngine of this Boxle instance.
     *
     * @return return the RenderEngine of this Boxle instance
     */
    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    /**
     * Gets the entire world map.
     *
     * @return Return the world map.
     */
    public Map<String, World> getWorldMap() {
        return worldMap;
    }

    /**
     * Gets a world by it's name.
     *
     * @param name The name of the world.
     * @return Returns the instance of the world, or null if none exists or name is null.
     */
    public World getWorld(String name) {
        if (name == null) {
            throw new IllegalArgumentException("World name must not be null!");
        }
        return worldMap.get(name);
    }

    /**
     * Adds a world to the world map.
     *
     * @param world The world to add.
     */
    public void addWorld(World world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null!");
        }
        String name = world.getName();
        if (name == null) {
            throw new IllegalArgumentException("World has a null name!");
        }
        worldMap.put(name, world);
    }

    /**
     * Requests the game to stop.
     */
    public void stop() {
        canRun = false;
    }

    /**
     * Starts boxle
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        new Boxle();
    }
}
