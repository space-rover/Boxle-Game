package net.acomputerdog.boxle.main;

import net.acomputerdog.boxle.input.InputHandler;

public class Client {
    /**
     * The owning Boxle instance;
     */
    private final Boxle boxle;

    /**
     * Input handler for this client instance
     */
    private final InputHandler input;

    /**
     * Create a new Client instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Client(Boxle boxle) {
        this.boxle = boxle;
        input = new InputHandler(this);
    }

    /**
     * Initializes this client
     */
    public void init() {
        input.init();
    }

    /**
     * Ticks this client
     */
    public void tick() {
        input.tick();
    }

    /**
     * Shuts down this client
     */
    public void shutdown() {
        input.shutdown();
    }

    /**
     * Get the boxle instance of this client.
     *
     * @return Return the boxle instance of this client.
     */
    public Boxle getBoxle() {
        return boxle;
    }

    /**
     * Gets the InputHandler for this Client
     *
     * @return return the Input Handler for this client.
     */
    public InputHandler getInput() {
        return input;
    }
}
