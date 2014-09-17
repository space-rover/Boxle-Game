package net.acomputerdog.boxle.input;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.main.Client;

/**
 * Handles input for the game client.
 */
public class InputHandler {
    /**
     * The owning Client instance;
     */
    private final Client client;

    /**
     * Create a new InputHandler instance.
     *
     * @param client The parent client instance.
     */
    public InputHandler(Client client) {
        this.client = client;
    }

    /**
     * Initializes this InputHandler
     */
    public void init() {

    }

    /**
     * Ticks this handler
     */
    public void tick() {

    }

    /**
     * Shuts down this handler
     */
    public void shutdown() {

    }

    /**
     * Get the client instance of this handler.
     *
     * @return Return the client instance of this handler.
     */
    public Client getClient() {
        return client;
    }

    public Boxle getBoxle() {
        return client.getBoxle();
    }
}
