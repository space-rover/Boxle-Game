package net.acomputerdog.boxle.input;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.main.Client;
import net.acomputerdog.boxle.util.ThreadUtils;
import net.acomputerdog.core.logger.CLogger;

/**
 * Handles input for the game client.
 */
public class InputHandler implements Runnable {
    /**
     * Logger for InputHandler
     */
    private final CLogger logger = new CLogger("InputHandler", false, true);

    /**
     * The owning Client instance;
     */
    private final Client client;

    /**
     * Set to false to request InputHandler to stop.
     */
    private boolean canRun = true;

    /**
     * Create a new InputHandler instance.
     *
     * @param client The parent client instance.
     */
    public InputHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        init();
        try {
            while (getBoxle().canRun() && canRun) {
                long startTime = System.currentTimeMillis();

                ThreadUtils.sync(startTime, 1);
            }
        } catch (Exception e) {
            logger.logFatal("Exception while ticking InputHandler!", e);
            getBoxle().stop();
        }
        shutdown();
    }

    /**
     * Initializes this InputHandler
     */
    public void init() {
        logger.logInfo("Started.");
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
        logger.logInfo("Stopping!");
    }

    /**
     * Requests the input handler to stop.
     */
    public void requestStop() {
        canRun = false;
    }

    /**
     * Returns true if the InputHandler is running
     *
     * @return return true if the InputHandler is running
     */
    public boolean canRun() {
        return canRun;
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
