package net.acomputerdog.boxle.config;

import net.acomputerdog.boxle.main.Boxle;

/**
 * Game config.
 */
public class GameConfig {
    /**
     * The boxle instance that this config controls.
     */
    private final Boxle boxle;

    /**
     * Max allowed FPS.
     */
    public int maxFPS = 60;

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

    }

    /**
     * Saves this config.
     */
    public void save() {

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
