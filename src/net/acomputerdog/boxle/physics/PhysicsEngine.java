package net.acomputerdog.boxle.physics;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.world.structure.World;

/**
 * Physics engine.
 */
public class PhysicsEngine {
    /**
     * Boxle instance
     */
    private final Boxle boxle;

    /**
     * World that this PhysicsEngine is handling physics for.
     */
    private final World world;

    /**
     * Creates a new instance of this PhysicsEngine.
     *
     * @param world The World that this PhysicsEngine is handling physics for.
     */
    public PhysicsEngine(World world) {
        this.world = world;
        if (world == null) {
            throw new IllegalArgumentException("World instance must not be null!");
        }
        this.boxle = world.getBoxle();
        if (boxle == null) {
            throw new IllegalArgumentException("Boxle instance must not be null!");
        }
    }

    /**
     * Gets the Boxle that this RenderEngine belongs to.
     *
     * @return Return the Boxle that created this RenderEngine.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    /**
     * The world that this PhysicsEngine is providing physics for.
     *
     * @return Return the world that this PhysicsEngine is providing physics for.
     */
    public World getWorld() {
        return world;
    }
}
