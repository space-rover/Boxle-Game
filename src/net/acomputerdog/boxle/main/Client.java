package net.acomputerdog.boxle.main;

public class Client {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    /**
     * Create a new Client instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Client(Boxle boxle) {
        this.boxle = boxle;
    }

    /**
     * Initializes this client
     */
    public void init() {

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
}
