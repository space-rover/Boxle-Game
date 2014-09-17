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

    public RenderEngine(Boxle boxle) {
        this.boxle = boxle;
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
