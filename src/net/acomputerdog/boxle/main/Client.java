package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.entity.types.EntityPlayer;

/**
 * Boxle client instance
 */
public class Client {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    private EntityPlayer player;

    /**
     * Create a new Client instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Client(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
    }

    /**
     * Initializes this client
     */
    public void init() {
        player = new EntityPlayer(boxle.getServer().getDefaultWorld());
    }

    /**
     * Ticks this client
     */
    public void tick() {
    }

    /**
     * Shuts down this client
     */
    public void shutdown() {

    }

    /**
     * Get the boxle instance of this client.
     *
     * @return Return the boxle instance of this client.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
