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

    public Boxle getBoxle() {
        return boxle;
    }
}
