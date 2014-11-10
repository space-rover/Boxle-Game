package net.acomputerdog.boxle.input;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.Camera;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
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

    private Camera camera;
    private BoxleFlyByCamera flyby;
    private InputManager inputManager;
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
        this.inputManager = engine.getBoxle().getInputManager();
        this.flyby = (BoxleFlyByCamera) engine.getBoxle().getFlyByCamera();
        this.camera = engine.getBoxle().getCamera();

        flyby.setMoveSpeed(1f);
        flyby.setRotationSpeed(1.5f);
        flyby.setZoomSpeed(0f);

        inputManager.addMapping("Move Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Move Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Move Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Move Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Move Up", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Move Down", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(this, "Move Left", "Move Right", "Move Forward", "Move Back", "Move Up", "Move Down", "Exit", "Pause");

        AppStateManager stateManager = engine.getBoxle().getStateManager();
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        //remove un-needed flycam mappings
        inputManager.deleteMapping("FLYCAM_ZoomIn");
        inputManager.deleteMapping("FLYCAM_ZoomOut");
        inputManager.deleteMapping("FLYCAM_RotateDrag");
        inputManager.deleteMapping("FLYCAM_Rise");
        inputManager.deleteMapping("FLYCAM_Lower");
        inputManager.deleteMapping("FLYCAM_StrafeLeft");
        inputManager.deleteMapping("FLYCAM_StrafeRight");
        inputManager.deleteMapping("FLYCAM_Forward");
        inputManager.deleteMapping("FLYCAM_Backward");

        logger.logInfo("Started.");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("Exit".equals(name)) {
            engine.getBoxle().stop();
        } else if ("Pause".equals(name) && !isPressed) {
            flyby.toggleMouseGrabbed();
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        switch (name) {
            case "Move Left":
                flyby.moveCameraSide(.1f);
                break;
            case "Move Right":
                flyby.moveCameraSide(-.1f);
                break;
            case "Move Up":
                flyby.moveCameraVert(.1f);
                break;
            case "Move Down":
                flyby.moveCameraVert(-.1f);
                break;
            case "Move Forward":
                flyby.moveCameraStraight(.1f);
                break;
            case "Move Back":
                flyby.moveCameraStraight(-.1f);
                break;
            default:
        }
    }
}
