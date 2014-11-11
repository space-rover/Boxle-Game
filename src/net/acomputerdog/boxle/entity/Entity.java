package net.acomputerdog.boxle.entity;

import net.acomputerdog.boxle.math.vec.Vec3f;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.World;

/**
 * Entity superclass.  Unlike Block and Item, there is one instance per entity in the world.
 */
public abstract class Entity {
    /**
     * Next available entity ID
     */
    private static int freeID = 0;

    /**
     * The type of this entity
     */
    private final String type;

    /**
     * The universal ID of this entity
     */
    private final int entityID;

    /**
     * The world this entity is in
     */
    private World world;

    /**
     * Gets the location of this entity
     */
    private Vec3f location;

    /**
     * Gets the rotation of this entity
     */
    private Vec3f rotation;

    /**
     * Creates a new Entity
     *
     * @param type     The type of this entity
     * @param world    The world this entity is in
     * @param location The location of this entity
     * @param rotation The rotation of this entity
     */
    protected Entity(String type, World world, Vec3f location, Vec3f rotation) {
        if (type == null) throw new IllegalArgumentException("Entity type cannot be null!");
        if (world == null) throw new IllegalArgumentException("Entity world cannot be null!");
        if (location == null) throw new IllegalArgumentException("Entity location cannot be null!");
        if (rotation == null) throw new IllegalArgumentException("Entity rotation cannot be null!");
        this.type = type;
        this.world = world;
        this.location = location;
        this.rotation = rotation;
        entityID = generateEntityID();
    }

    /**
     * Creates a new Entity
     *
     * @param type     The type of this entity
     * @param world    The world this entity is in
     * @param location The location of this entity
     */
    public Entity(String type, World world, Vec3f location) {
        this(type, world, location, VecPool.createVec3f()); //not using pool, because this is a unique permanent vec.
    }

    /**
     * Called when the entity is ticked
     */
    public void onTick() {

    }

    /**
     * Gets the type of this entity
     *
     * @return return the type of this entity
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the world this entity is in
     *
     * @return return the world this entity is in
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the location of this entity
     *
     * @return return the location of this entity
     */
    public Vec3f getLocation() {
        return location;
    }

    /**
     * Gets the rotation of this entity
     *
     * @return return the rotation of this entity
     */
    public Vec3f getRotation() {
        return rotation;
    }

    /**
     * Gets the ID of this entity
     *
     * @return return the ID of this entity
     */
    public int getEntityID() {
        return entityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return entityID == entity.entityID;

    }

    @Override
    public int hashCode() {
        return entityID;
    }

    /**
     * Generates a valid entity ID.  Must be synchronized in case multiple threads try to use the same ID for different entities.
     *
     * @return Return an integer to be used as an entity ID
     */
    private static synchronized int generateEntityID() {
        int id = freeID;
        freeID++;
        return id;
    }
}
