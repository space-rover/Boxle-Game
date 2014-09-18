package net.acomputerdog.boxle.config;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.core.logger.CLogger;

/**
 * Game config.
 */
public class GameConfig {
    /**
     * The boxle instance that this config controls.
     */
    private final Boxle boxle;

    /**
     * Logger for GameConfig
     */
    private final CLogger logger = new CLogger("GameConfig", false, true);

    /**
     * Max allowed FPS.
     */
    public int maxFPS = 60;

    /**
     * Screen width
     */
    public int screenWidth = 800;

    /**
     * Screen height
     */
    public int screenHeight = 600;

    /**
     * If true, limit FPS to screen refresh rate.
     */
    public boolean enableVSync = false;

    /**
     * Field of view
     */
    public float fov = 60.0f;

    /**
     * If true, game will run in fullscreen mode
     */
    public boolean fullscreen = false;

    /**
     * Render distance on the X and Z axises
     */
    public int renderDistanceHorizontal = 10;

    /**
     * Render distance on the Y axis
     */
    public int renderDistanceVertical = 5;

    /**
     * Creates a new config for the given boxle instance.
     *
     * @param boxle The boxle instance to create this config for.
     */
    public GameConfig(Boxle boxle) {
        this.boxle = boxle;
    }

    /**
     * Loads this config.
     */
    public void load() {
        logger.logInfo("Loaded game config.");
    }

    /**
     * Saves this config.
     */
    public void save() {
        logger.logInfo("Saved game config.");
    }

    /**
     * Gets the boxle that this Config belongs to.
     *
     * @return Return the boxle that owns this config.
     */
    public Boxle getBoxle() {
        return boxle;
    }
}
