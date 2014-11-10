package net.acomputerdog.boxle.input;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.core.logger.CLogger;

/**
 * Handles input for the game client.
 */
public class InputHandler implements ActionListener, AnalogListener, MotionAllowedListener {
    /**
     * Logger for InputHandler
     */
    private final CLogger logger = new CLogger("InputHandler", false, true);

    /**
     * The RenderEngine owning this InputHandler
     */
    private final RenderEngine engine;

    private Camera camera;
    private FlyByCamera flyby;
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
        this.flyby = engine.getBoxle().getFlyByCamera();
        this.camera = engine.getBoxle().getCamera();

        flyby.setMotionAllowedListener(this);

        flyby.setMoveSpeed(3f);
        flyby.setRotationSpeed(1.5f);
        flyby.setZoomSpeed(0f);
        //flyby.setEnabled(false);
        //stateManager.detach(stateManager.getState(FlyCamAppState.class));

        //inputManager.clearMappings();
        //inputManager.clearRawInputListeners();
        //inputManager.reset();
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
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(this, "Move Left", "Move Right", "Move Forward", "Move Back", "Move Up", "Move Down", "Exit", "Pause");
        inputManager.addListener(this, "Look Left", "Look Right", "Look Up", "Look Down");

        //inputManager.setCursorVisible(false);

        logger.logInfo("Started.");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("Exit".equals(name)) {
            engine.getBoxle().stop();
        } else if ("Pause".equals(name) && !isPressed) {
            //inputManager.setCursorVisible(!inputManager.isCursorVisible());
            flyby.setEnabled(!flyby.isEnabled());
            if (flyby.isEnabled()) {
                inputManager.setCursorVisible(false); //missing from flyby.setEnabled, apparently.
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

        //Quaternion rot = camera.getRotation();
        switch (name) {
            case "Look Left":
                //rot.set(rot.getX(), rot.getY(), rot.getZ() - .1f, rot.getW());
                break;
            case "Look Right":
                //rot.set(rot.getX(), rot.getY(), rot.getZ() + .1f, rot.getW());
                break;
            case "Look Up":
                //rot.set(rot.getX(), rot.getY() + .1f, rot.getZ(), rot.getW());
                break;
            case "Look Down":
                //rot.set(rot.getX(), rot.getY() - .1f, rot.getZ(), rot.getW());
                break;
            case "Move Left":
                //camera.setLocation(camera.getLocation().add(-.1f, 0f, 0f));
                break;
            case "Move Right":
                //camera.setLocation(camera.getLocation().add(.1f, 0f, 0f));
                break;
            case "Move Up":

                //camera.setLocation(camera.getLocation().add(0f, .1f, 0f));
                break;
            case "Move Down":
                //camera.setLocation(camera.getLocation().add(0f, -.1f, 0f));
                break;
            case "Move Forward":
                //camera.setLocation(camera.getLocation().add(0f, 0f, -.1f));
                break;
            case "Move Back":
                //camera.setLocation(camera.getLocation().add(0f, 0f, .1f));
                break;
            default:
        }

        //camera.update();
        //camera.updateViewProjection();
    }

    @Override
    public void checkMotionAllowed(Vector3f position, Vector3f velocity) {
        position.addLocal(velocity.x, 0, velocity.z);
        //velocity.setY(0f);
    }
}
