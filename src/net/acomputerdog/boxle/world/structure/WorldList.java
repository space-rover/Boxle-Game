package net.acomputerdog.boxle.world.structure;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Holds a list of worlds for a Boxle instance.  Thread-safe.
 */
public class WorldList {
    /**
     * Map of world names to instances.
     */
    private final Map<String, World> nameToWorldMap;

    /**
     * Maps of world instances to names.
     */
    private final Map<World, String> worldToNameMap;

    /**
     * Set of all world instances
     */
    private final Set<World> instanceSet;

    /**
     * Set of all world names
     */
    private final Set<String> nameSet;

    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    /**
     * Create a new world list
     *
     * @param boxle The parent boxle instance.
     */
    public WorldList(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance cannot be null!");
        this.boxle = boxle;
        nameToWorldMap = new ConcurrentHashMap<>();
        worldToNameMap = new ConcurrentHashMap<>();
        instanceSet = new CopyOnWriteArraySet<>();
        nameSet = new CopyOnWriteArraySet<>();
    }

    /**
     * Gets a world by it's name.
     *
     * @param name The name of the world.
     * @return Returns the instance of the world, or null if none exists or name is null.
     */
    public World getWorld(String name) {
        if (name == null) throw new IllegalArgumentException("World name must not be null!");
        return nameToWorldMap.get(name);
    }

    /**
     * Adds a world to the world map.
     *
     * @param world The world to add.
     */
    public void addWorld(World world) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        String name = world.getName();
        if (name == null) throw new IllegalArgumentException("World has a null name!");
        nameToWorldMap.put(name, world);
        worldToNameMap.put(world, name);
        instanceSet.add(world);
        nameSet.add(name);
    }

    /**
     * Removes a specified world.
     *
     * @param name The name of the world.
     */
    public void removeWorld(String name) {
        if (name == null) throw new IllegalArgumentException("name cannot be null!");
        World world = nameToWorldMap.remove(name);
        if (world != null) { //if world exists, remove from other mappings.
            worldToNameMap.remove(world);
            instanceSet.remove(world);
            nameSet.remove(name);
        }
    }

    /**
     * Removes a specified world.
     *
     * @param world The world to remove.
     */
    public void removeWorld(World world) {
        if (world == null) throw new IllegalArgumentException("World cannot be null!");
        String name = worldToNameMap.remove(world);
        if (name != null) { //if world exists, remove from other mappings.
            nameToWorldMap.remove(name);
            instanceSet.remove(world);
            nameSet.remove(name);
        }
    }

    /**
     * Get a set of all worlds
     *
     * @return Return a set of all worlds.
     */
    public Set<World> getWorlds() {
        return Collections.unmodifiableSet(instanceSet);
    }

    /**
     * Get a set of all world names
     *
     * @return Return a set of all world names.
     */
    public Set<String> getWorldNames() {
        return Collections.unmodifiableSet(nameSet);
    }

    /**
     * Get the boxle instance of this world list.
     *
     * @return Return the boxle instance of this world list.
     */
    public Boxle getBoxle() {
        return boxle;
    }
}
