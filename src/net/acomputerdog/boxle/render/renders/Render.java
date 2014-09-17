package net.acomputerdog.boxle.render.renders;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.util.execption.BoxleException;

/**
 * Represents a type of renderable object.  Can have one or more of the following phases: init, startup, render, shutdown, and cleanup.
 */
public interface Render {
    /**
     * Gets the RenderEngine of this Render.
     *
     * @return return the RenderEngine of this Render
     */
    public RenderEngine getEngine();

    /**
     * Gets the Render that created this Render, if applicable.
     *
     * @return Return the Render that created this Render, or null if none exists.
     */
    public Render getParentRender();

    /**
     * Initializes non-render functions of this Render.  Should be treated like a constructor.
     * Because this method is for non-render code, it may be called by a thread that does not have a GL context.
     */
    public void init();

    /**
     * Sets up render environment.  Calling threads must have a GL context.
     * @throws BoxleException If exception occurs.
     */
    public void startup() throws BoxleException;

    /**
     * Called each frame to trigger a render.  Calling threads must have a GL context.
     * @throws BoxleException If exception occurs.
     */
    public void render() throws BoxleException;

    /**
     * Cleanup render environment. Calling threads must have a GL context.
     * @throws BoxleException If exception occurs.
     */
    public void shutdown() throws BoxleException;

    /**
     * Cleanup non-render functions of this Render.
     * Because this method is for non-render code, it may be called by a thread that does not have a GL context.
     */
    public void cleanup();
}
