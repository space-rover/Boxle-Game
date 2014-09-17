package net.acomputerdog.boxle.world;

import net.acomputerdog.boxle.main.Boxle;

/**
 * A world, made of blocks :)
 */
public class World {
    /**
     * Boxle instance
     */
    private final Boxle boxle;

    /**
     * Name of this world.
     */
    private final String name;

    /**
     * Creates a new instance of this World.
     *
     * @param boxle The Boxle instance that created this World.
     * @param name  The name of this world.
     */
    public World(Boxle boxle, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if (boxle == null) {
            throw new IllegalArgumentException("Boxle instance must not be null!");
        }
        this.boxle = boxle;
        this.name = name;
    }

    /**
     * Gets the Boxle that this World belongs to.
     *
     * @return Return the Boxle that created this World.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    /**
     * Gets the name of this world.
     *
     * @return Return the name of this world.
     */
    public String getName() {
        return name;
    }
}
