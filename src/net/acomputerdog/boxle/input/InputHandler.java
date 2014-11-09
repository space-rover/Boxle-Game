package net.acomputerdog.boxle.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.core.logger.CLogger;

/**
 * Handles input for the game client.
 */
public class InputHandler implements ActionListener, AnalogListener {
    /**
     * Logger for InputHandler
     */
    private final CLogger logger = new CLogger("InputHandler", false, true);

    /**
     * The RenderEngine owning this InputHandler
     */
    private final RenderEngine engine;

    /**
     * Create a new InputHandler instance.
     *
     * @param engine The parent client instance.
     */
    public InputHandler(RenderEngine engine) {
        if (engine == null) throw new IllegalArgumentException("Engine instance must not be null!");
        this.engine = engine;
    }

    /**
     * Initializes this InputHandler
     */
    public void init() {
        InputManager inputManager = engine.getBoxle().getInputManager();
        inputManager.clearMappings();
        inputManager.clearRawInputListeners();
        inputManager.reset();
        inputManager.addMapping("Move Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Move Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Move Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Move Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Move Up", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Move Down", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Look Left", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("Look Right", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("Look Up", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("Look Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addListener(this, "Move Left", "Move Right", "Move Forward", "Move Back", "Move Up", "Move Down", "Exit");
        inputManager.addListener(this, "Look Left", "Look Right", "Look Up", "Look Down");
        logger.logInfo("Started.");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("Exit".equals(name)) {
            engine.getBoxle().stop();
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

    }
}
