package net.acomputerdog.boxle.main;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.block.sim.loader.SimLoader;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.BasicSSAO;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
import net.acomputerdog.boxle.save.IOThread;
import net.acomputerdog.boxle.world.structure.WorldList;
import net.acomputerdog.core.java.Sleep;
import net.acomputerdog.core.logger.CLogger;

import java.io.File;

/**
 * Boxle main class
 */
public class Boxle extends SimpleApplication {
    /**
     * Normal logger that just logs time.
     */
    public static final CLogger LOGGER = new CLogger("Boxle", false, true);

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

    private boolean canRunIO = true;

    private static Boxle instance;

    private BoxleFlyByCamera boxleFlyCam;

    private boolean hasStarted = false;


    /**
     * Creates a new Boxle instance
     */
    private Boxle() {
        if (instance != null) {
            throw new IllegalStateException("Multiple Boxles cannot be created!");
        }
        instance = this;
        gameConfig = new GameConfig(this);
        this.renderEngine = new RenderEngine(this);
        worlds = new WorldList(this);
        client = new Client(this);
        server = new Server(this);
        try {
            init();
        } catch (Throwable t) {
            LOGGER.logFatal("Caught exception during init phase!", t);
            end(-1);
        }
        try {
            run();
        } catch (Throwable t) {
            LOGGER.logFatal("Caught exception during run phase!", t);
            end(-2);
        }
        try {
            LOGGER.logError("Reached invalid area of code!  Shutting down!");
            cleanup();
        } catch (Throwable t) {
            LOGGER.logFatal("Caught exception in invalid area of code!", t);
        }
        end(-3);
    }

    @Override
    public void simpleInitApp() {
        VecPool.init();

        SimLoader.loadExternalSims();
        Blocks.initBlockTextures();

        //must be in order render -> server -> client
        server.init();
        client.init();

        cam.setFrustumPerspective(gameConfig.fov, (float) gameConfig.screenWidth / (float) gameConfig.screenHeight, .2f, 1000f);
        cam.update();

        boxleFlyCam = new BoxleFlyByCamera(cam);
        flyCam = boxleFlyCam;
        boxleFlyCam.registerWithInput(inputManager);

        EntityPlayer player = client.getPlayer();
        boxleFlyCam.setPlayer(player);
        player.setFlyby(boxleFlyCam);

        super.setDisplayStatView(false);

        super.setPauseOnLostFocus(false);

        viewPort.setBackgroundColor(new ColorRGBA(.25f, .5f, 1f, 1f));

        renderEngine.init();

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        if (gameConfig.lightingMode >= 2) {
            BasicSSAO ssao = new BasicSSAO(1f, 2f, 0.2f, 0.1f);
            ssao.setUseDetailPass(true);
            ssao.setUseSmoothing(true);
            fpp.addFilter(ssao);
        }
        if (gameConfig.shadowMode > 0) {
            DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, gameConfig.shadowMode, 1);
            dlsf.setLight(renderEngine.getSun());
            dlsf.setEnabled(true);
            fpp.addFilter(dlsf);
        }
        viewPort.addProcessor(fpp);

        hasStarted = true;
    }

    /**
     * Initializes boxle to start.
     */
    private void init() {
        LOGGER.logInfo("Boxle is initializing.");
        gameConfig.load();

        File tempDir = new File(gameConfig.cacheDir);
        if (!(tempDir.isDirectory() || tempDir.mkdirs())) {
            LOGGER.logError("Could not create temporary directory!");
            LOGGER.logError("Make sure boxle has write access to the current directory, or run boxle from a directory with write access!");
        }

        super.showSettings = false;

        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(gameConfig.maxFPS);
        settings.setWidth(gameConfig.screenWidth);
        settings.setHeight(gameConfig.screenHeight);
        settings.setVSync(gameConfig.enableVSync);
        settings.setUseInput(true);
        settings.setFullscreen(gameConfig.fullscreen);
        settings.setTitle("Boxle");
        super.settings = settings;

        super.start();
    }

    /**
     * Performs actual game loop.
     */
    private void run() {
        LOGGER.logInfo("Boxle is starting.");
        while (canRun) {
            long time = System.currentTimeMillis();
            if (hasStarted) {
                server.tick(); //todo separate thread
                client.tick(); //todo separate thread
            }
            Sleep.sync(time, 1000 / gameConfig.ticksPerSecond);
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
        gameConfig.save();
        canRunIO = false;
        IOThread.waitForEnd();
    }

    /**
     * Saves and exits.  Should not do any game actions, and should be safe to call without try-catch blocks.
     *
     * @param code The error code to return.  Non-zero codes print out abnormal shutdown.
     */
    private void end(int code) {
        if (code == 0) {
            LOGGER.logInfo("Boxle shutting down normally.");
        } else {
            LOGGER.logWarning("Boxle shutting down abnormally: error code " + code + ".");
        }
        System.exit(code);
    }

    /**
     * Requests the game to stop.
     */
    public void stop() {
        LOGGER.logInfo("Stopping.");
        canRun = false;
    }

    /**
     * Checks if the game is running, or if game components should begin shutting down.
     *
     * @return return true if the game is running
     */
    public boolean canRun() {
        return canRun;
    }

    public boolean canRunIO() {
        return canRunIO;
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

    @Override
    public void simpleUpdate(float tpf) {
        renderEngine.render();
    }

    @Override
    public void destroy() {
        super.destroy();
        stop();
    }

    /**
     * Starts boxle
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        new Boxle();
    }

    public static Boxle instance() {
        return instance;
    }

    public BoxleFlyByCamera getBoxleFlyCam() {
        return boxleFlyCam;
    }

    @Override
    public void gainFocus() {
        super.gainFocus();
        boxleFlyCam.setEnabled(true);
    }

    @Override
    public void loseFocus() {
        super.loseFocus();
        boxleFlyCam.setEnabled(false);
    }

    public BitmapFont getFont() {
        return guiFont;
    }
}
