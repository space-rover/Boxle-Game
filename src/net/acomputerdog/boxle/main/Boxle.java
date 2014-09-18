package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.world.structure.WorldList;
import net.acomputerdog.core.logger.CLogger;
import org.lwjgl.opengl.Display;

/**
 * Boxle main class
 */
public class Boxle {
    /**
     * Logger that logs without date or time.  Useful for high-output debug messages.
     */
    public final CLogger LOGGER_FAST = new CLogger("Boxle", false, false);
    /**
     * Normal logger that just logs time.
     */
    public final CLogger LOGGER_MAIN = new CLogger("Boxle", false, true);
    /**
     * Full logger that outputs date and time.  Useful for crash messages.
     */
    public final CLogger LOGGER_FULL = new CLogger("Boxle", true, true);

    /**
     * Central render engine
     */
    private final RenderEngine renderEngine;

    /**
     * All loaded worlds
     */
    private final WorldList worlds;

    /**
     * Global game config.
     */
    private final GameConfig gameConfig;

    /**
     * The instance of the game client
     */
    private final Client client;

    /**
     * The instance of the game server;
     */
    private final Server server;

    /**
     * If true the game can run, if false, a close has been requested.
     */
    private boolean canRun = true;

    /**
     * Creates a new Boxle instance
     */
    private Boxle() {
        this.renderEngine = new RenderEngine(this);
        gameConfig = new GameConfig(this);
        worlds = new WorldList(this);
        client = new Client(this);
        server = new Server(this);
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
            end(-2);
        }
        try {
            LOGGER_FULL.logError("Reached invalid area of code!  Shutting down!");
            cleanup();
        } catch (Throwable t) {
            LOGGER_FULL.logFatal("Caught excpetion in invalid area of code!", t);
        }
        end(-3);
    }

    /**
     * Initializes boxle to start.
     */
    private void init() {
        LOGGER_FULL.logInfo("Boxle is initializing.");
        //must be in order render -> server -> client
        renderEngine.init();
        server.init();
        client.init();
    }

    /**
     * Performs actual game loop.
     */
    private void run() {
        LOGGER_FULL.logInfo("Boxle is starting.");
        while (canRun) {
            server.tick(); //todo separate thread
            client.tick(); //todo separate thread
            renderEngine.render(); //todo separate thread
            Display.sync(gameConfig.maxFPS); //limit the tick speed to max FPS
        }
        cleanup();
        end(0);
    }

    /**
     * Cleanup and prepare to close.
     */
    private void cleanup() {
        //must be in order client -> server -> render
        client.shutdown();
        server.shutdown();
        renderEngine.cleanup();
    }

    /**
     * Saves and exits.  Should not do any game actions, and should be safe to call without try-catch blocks.
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
     * Requests the game to stop.
     */
    public void stop() {
        canRun = false;
    }

    /**
     * Checks if the game is running, or if game components should begin shutting down.
     * @return return true if the game is running
     */
    public boolean canRun() {
        return canRun;
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
     * Gets the game config.
     *
     * @return Return the GameConfig controlling this game.
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Gets the client of this game.
     *
     * @return Return the client of this game.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the server of this game.
     *
     * @return Return the server of this game.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the all worlds loaded by this Boxle instance
     *
     * @return Return a WorldList containing all worlds loaded by this boxle instance.
     */
    public WorldList getWorlds() {
        return worlds;
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
