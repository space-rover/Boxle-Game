package net.acomputerdog.boxle.render.engine;

import net.acomputerdog.boxle.main.Boxle;

/**
 * Central boxle render engine.
 */
public class RenderEngine {
    /**
     * Boxle instance
     */
    private final Boxle boxle;

    /**
     * Creates a new instance of this RenderEngine.
     *
     * @param boxle The Boxle instance that created this RenderEngine.
     */
    public RenderEngine(Boxle boxle) {
        if (boxle == null) {
            throw new IllegalArgumentException("Boxle instance must not be null!");
        }
        this.boxle = boxle;
    }

    /**
     * Initializes this RenderEngine
     */
    public void init() {

    }

    /**
     * Render a single frame.
     */
    public void render() {

    }

    /**
     * Cleanup and prepare for shutdown.
     */
    public void cleanup() {

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
