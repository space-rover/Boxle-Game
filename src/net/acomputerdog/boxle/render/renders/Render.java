package net.acomputerdog.boxle.render.renders;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.util.execption.BoxleException;

public interface Render {
    public RenderEngine getEngine();

    public void init();

    public void startup() throws BoxleException;

    public void render() throws BoxleException;

    public void shutdown() throws BoxleException;

    public void cleanup();
}
