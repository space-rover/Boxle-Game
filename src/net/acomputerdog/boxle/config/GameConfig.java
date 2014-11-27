package net.acomputerdog.boxle.config;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.core.logger.CLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private final File configFile = createConfigFile();

    private boolean isChanged = false;

    /**
     * Max allowed FPS.
     */
    public int maxFPS = 60;

    /**
     * Screen width
     */
    public int screenWidth = 1280;

    /**
     * Screen height
     */
    public int screenHeight = 720;

    /**
     * If true, limit FPS to screen refresh rate.
     */
    public boolean enableVSync = false;

    /**
     * Field of view
     */
    public float fov = 45.0f;

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

    public int maxLoadedChunksPerTick = 200;

    public int notifyNeighborsMode = 0; //0=only adjacent neighbors, 1=adjacent+"edge" neighbors, 2=adjacent+edge+"corner" neighbors, 3=adjacent+edge+corner+another layer of adjacent, -1=NO notifications

    public boolean outputRenderDebugInfo = false;

    public int ticksPerSecond = 50;

    public int lightingMode = 2; //0==off, 1==lighting only, 2==lighting+SSAO

    public int shadowMode = 2048; //0==off, anything else is shadowmap size

    public String cacheDir = "./cache/";

    public String worldName = "World";

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
        if (configFile.exists()) {
            DataTypeProperties properties = new DataTypeProperties();
            try {
                properties.load(new FileInputStream(configFile));
            } catch (IOException e) {
                logger.logError("Unable to load game config!", e);
                return;
            }
            ticksPerSecond = properties.getIntProperty("max_tickrate", ticksPerSecond);
            maxFPS = properties.getIntProperty("max_framerate", maxFPS);
            enableVSync = properties.getBooleanProperty("enable_vsync", enableVSync);
            screenWidth = properties.getIntProperty("screen_width", screenWidth);
            screenHeight = properties.getIntProperty("screen_height", screenHeight);
            fullscreen = properties.getBooleanProperty("enable_fullscreen", fullscreen);
            fov = properties.getFloatProperty("field_of_view", fov);
            renderDistanceHorizontal = properties.getIntProperty("horizontal_render_distance", renderDistanceHorizontal);
            renderDistanceVertical = properties.getIntProperty("vertical_render_distance", renderDistanceVertical);
            maxLoadedChunksPerTick = properties.getIntProperty("max_chunks_loaded_per_tick", maxLoadedChunksPerTick);
            notifyNeighborsMode = properties.getIntProperty("notify_chunk_neighbors_mode", notifyNeighborsMode);
            outputRenderDebugInfo = properties.getBooleanProperty("output_meshing_performance_data", outputRenderDebugInfo);
            cacheDir = properties.getProperty("cache_directory", cacheDir);
            lightingMode = properties.getIntProperty("lighting_mode", lightingMode);
            shadowMode = properties.getIntProperty("shadow_mode", shadowMode);
            worldName = properties.getProperty("world_name", worldName);
            logger.logInfo("Loaded game config.");
        } else {
            logger.logWarning("No config file found, creating new one.");
            isChanged = true;
            save();
        }
    }

    /**
     * Saves this config.
     */
    public void save() {
        if (isChanged) {
            isChanged = false;
            DataTypeProperties properties = new DataTypeProperties();
            properties.setProperty("max_tickrate", String.valueOf(ticksPerSecond));
            properties.setProperty("max_framerate", String.valueOf(maxFPS));
            properties.setProperty("enable_vsync", String.valueOf(enableVSync));
            properties.setProperty("screen_width", String.valueOf(screenWidth));
            properties.setProperty("screen_height", String.valueOf(screenHeight));
            properties.setProperty("enable_fullscreen", String.valueOf(fullscreen));
            properties.setProperty("field_of_view", String.valueOf(fov));
            properties.setProperty("horizontal_render_distance", String.valueOf(renderDistanceHorizontal));
            properties.setProperty("vertical_render_distance", String.valueOf(renderDistanceVertical));
            properties.setProperty("max_chunks_loaded_per_tick", String.valueOf(maxLoadedChunksPerTick));
            properties.setProperty("notify_chunk_neighbors_mode", String.valueOf(notifyNeighborsMode));
            properties.setProperty("output_meshing_performance_data", String.valueOf(outputRenderDebugInfo));
            properties.setProperty("cache_directory", String.valueOf(cacheDir));
            properties.setProperty("lighting_mode", String.valueOf(lightingMode));
            properties.setProperty("shadow_mode", String.valueOf(shadowMode));
            properties.setProperty("world_name", String.valueOf(worldName));
            try {
                properties.store(new FileOutputStream(configFile), "Boxle configuration file.  Make sure any changes remain in the original data type.");
            } catch (java.io.IOException e) {
                logger.logError("Unable to save game configuration!", e);
                return;
            }
            logger.logInfo("Saved game config.");
        }
    }

    private File createConfigFile() {
        File dir = new File("./config/");
        if (!(dir.isDirectory() || dir.mkdirs())) {
            logger.logWarning("Unable to create config directory!");
        }
        return new File(dir, "boxle.cfg");
    }

    /**
     * Gets the boxle that this Config belongs to.
     *
     * @return Return the boxle that owns this config.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void markChanged() {
        this.isChanged = true;
    }
}
