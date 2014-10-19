package net.acomputerdog.boxle.input;

import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.main.Client;
import net.acomputerdog.core.java.ThreadUtils;
import net.acomputerdog.core.logger.CLogger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

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
        if (client == null) throw new IllegalArgumentException("Client instance must not be null!");
        this.client = client;
    }

    @Override
    public void run() {
        init();
        try {
            while (getBoxle().canRun() && canRun) {
                long startTime = System.currentTimeMillis();
                tick();
                ThreadUtils.sync(startTime, 1); //ensure method takes at least 1 ms to complete
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
        try {
            Keyboard.create();
        } catch (LWJGLException e) {
            logger.logFatal("Exception creating keyboard!", e);
            getBoxle().stop();
            return;
        }
        try {
            Mouse.create();
        } catch (LWJGLException e) {
            logger.logFatal("Exception creating mouse!", e);
            getBoxle().stop();
            return;
        }
        logger.logInfo("Started.");
    }

    /**
     * Ticks this handler
     */
    public void tick() {
        if (Display.isCloseRequested()) {
            getBoxle().stop();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            getBoxle().stop();
        }
    }

    /**
     * Shuts down this handler
     */
    public void shutdown() {
        try {
            if (Keyboard.isCreated()) Keyboard.destroy(); //only destroy keyboard if created
            if (Mouse.isCreated()) Mouse.destroy(); //only destroy mouse if created
        } catch (Exception ignored) {
        }
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

    /**
     * Gets the boxle instance of this InputHandler
     *
     * @return return the Boxle instance of this input handler
     */
    public Boxle getBoxle() {
        return client.getBoxle();
    }

}
