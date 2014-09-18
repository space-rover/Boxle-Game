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

    private final Thread inputThread;

    /**
     * Create a new Client instance.
     *
     * @param boxle The parent boxle instance.
     */
    public Client(Boxle boxle) {
        if (boxle == null) throw new IllegalArgumentException("Boxle instance must not be null!");
        this.boxle = boxle;
        input = new InputHandler(this);
        inputThread = new Thread(input);
        inputThread.setName("Client-Input");
    }

    /**
     * Initializes this client
     */
    public void init() {
        inputThread.start();
    }

    /**
     * Ticks this client
     */
    public void tick() {
        //todo read data from input handler
    }

    /**
     * Shuts down this client
     */
    public void shutdown() {
        input.requestStop();
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
