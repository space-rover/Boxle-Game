package net.acomputerdog.boxle.main;

public class Server {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    /**
     * Create a new Server instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Server(Boxle boxle) {
        this.boxle = boxle;
    }

    /**
     * Initializes this server
     */
    public void init() {

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
}
