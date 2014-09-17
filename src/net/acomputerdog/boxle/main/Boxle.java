package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.world.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Boxle main class
 */
public class Boxle {
    /**
     * Central render engine
     */
    private final RenderEngine renderEngine;

    /**
     * Map of world names to instances.
     */
    private final Map<String, World> worldMap;

    /**
     * Creates a new Boxle instance
     */
    private Boxle() {
        this.renderEngine = new RenderEngine(this);
        worldMap = new ConcurrentHashMap<>();
    }

    /**
     * Gets the RenderEngine of this Boxle instance.
     * @return return the RenderEngine of this Boxle instance
     */
    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    /**
     * Starts boxle
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        new Boxle();
    }

    /**
     * Gets the entire world map.
     *
     * @return Return the world map.
     */
    public Map<String, World> getWorldMap() {
        return worldMap;
    }

    /**
     * Gets a world by it's name.
     *
     * @param name The name of the world.
     * @return Returns the instance of the world, or null if none exists or name is null.
     */
    public World getWorld(String name) {
        if (name == null) {
            throw new IllegalArgumentException("World name must not be null!");
        }
        return worldMap.get(name);
    }

    /**
     * Adds a world to the world map.
     *
     * @param world The world to add.
     */
    public void addWorld(World world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null!");
        }
        String name = world.getName();
        if (name == null) {
            throw new IllegalArgumentException("World has a null name!");
        }
        worldMap.put(name, world);
    }
}
