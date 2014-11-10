package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.world.World;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Boxle Server instance
 */
public class Server {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    private final Set<World> hostedWorlds = new CopyOnWriteArraySet<>();

    private World defaultWorld;

    /**
     * Create a new Server instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Server(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
    }

    /**
     * Initializes this server
     */
    public void init() {
        defaultWorld = new World(boxle, "server_default");
        boxle.getWorlds().addWorld(defaultWorld);
        hostedWorlds.add(defaultWorld);
    }

    /**
     * Ticks this server
     */
    public void tick() {

    }

    /**
     * Shuts down this server
     */
    public void shutdown() {

    }

    /**
     * Get the boxle instance of this Server.
     *
     * @return Return the boxle instance of this Server.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    public World getDefaultWorld() {
        return defaultWorld;
    }

    public Set<World> getHostedWorlds() {
        return Collections.unmodifiableSet(hostedWorlds);
    }
}
