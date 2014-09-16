package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.render.engine.RenderEngine;

/**
 * Boxle main class
 */
public class Boxle {
    /**
     * Central render engine
     */
    private final RenderEngine renderEngine;

    /**
     * Creates a new Boxle instance
     */
    private Boxle() {
        this.renderEngine = new RenderEngine(this);
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
