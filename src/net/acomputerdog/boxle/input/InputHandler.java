package net.acomputerdog.boxle.input;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.java.Patterns;
import net.acomputerdog.core.logger.CLogger;

/**
 * Handles input for the game client.
 */
public class InputHandler implements ActionListener, AnalogListener {

    private static final String UNDERSCORE = Patterns.quote("_");
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
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("Break Block", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Place Block", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addMapping("Debug", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(this, "Debug");

        inputManager.addListener(this, "Move Left", "Move Right", "Move Forward", "Move Back", "Move Up", "Move Down", "Exit", "Pause", "Sprint", "Break Block", "Place Block");

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
        } else if ("Debug".equals(name) && !isPressed) {
            EntityPlayer player = engine.getBoxle().getClient().getPlayer();
            Vec3i pLoc = VecConverter.vec3iFromVec3f(player.getLocation());
            logger.logDebug("Player position: " + pLoc.asCoords() + " OR " + CoordConverter.globalToChunk(pLoc.duplicate()).asCoords() + "/" + CoordConverter.globalToBlock(pLoc.duplicate()));
            World world = player.getWorld();
            pLoc.y++;
            world.setBlockAt(pLoc, Blocks.acomputerdog, true);
            pLoc.y -= 2;
            world.setBlockAt(pLoc, Blocks.seamusFD, true);
            VecPool.free(pLoc);
        } else if ("Sprint".equals(name)) {
            flyby.setMoveSpeed(isPressed ? 2f : 1f);
        } else if ("Break Block".equals(name) && isPressed) {
            Vec3i loc = findClickedLoc(true);
            if (loc != null) {
                World world = Boxle.instance().getClient().getPlayer().getWorld();
                Block block = world.getBlockAt(loc);
                if (block != null && block.isBreakable()) {
                    world.setBlockAt(loc, Blocks.air, true);
                }
                VecPool.free(loc);
            }
        } else if ("Place Block".equals(name) && isPressed) {
            Vec3i loc = findClickedLoc(false);
            if (loc != null) {
                Boxle.instance().getClient().getPlayer().getWorld().setBlockAt(loc, Blocks.steel, true);
                VecPool.free(loc);
            }
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

    private Vec3i findClickedLoc(boolean inside) {
        CollisionResults results = new CollisionResults();
        Camera cam = Boxle.instance().getCamera();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        Node collision = Boxle.instance().getClient().getPlayer().getWorld().getWorldCollisionNode();
        collision.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        Vec3i loc;
        if (result == null) {
            loc = null;
        } else if (inside) {
            loc = VecConverter.vec3iFromVec3f(VecConverter.vector3fToVec3f(result.getGeometry().getWorldTranslation()));
        } else {
            loc = VecConverter.vec3iFromVec3f(VecConverter.vector3fToVec3f(result.getContactPoint()));
        }
        return loc;
    }

    /*
    private Vec3i findClickedLoc(boolean inside) {
        CollisionResults results = new CollisionResults();
        Camera cam = Boxle.instance().getCamera();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        Node terrain = Boxle.instance().getRenderEngine().getTerrainNode();
        terrain.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        if (inside) {
            Vector3f contactPoint = result.getContactPoint();
            Ray insideRay = new Ray(contactPoint, cam.getDirection());
            insideRay.setLimit(1f);
            CollisionResults rewResults = new CollisionResults();
            terrain.collideWith(ray, rewResults);
            CollisionResult newResult = rewResults.getFarthestCollision();
            return VecConverter.vec3iFromVec3f(VecConverter.vector3fToVec3f(newResult.getContactPoint()));
        } else {
            return VecConverter.vec3iFromVec3f(VecConverter.vector3fToVec3f(result.getContactPoint()));
        }
        /*
        String geomName = result.getGeometry().getName();
        if (true || result.getDistance() <= 5f) {
            if (geomName.startsWith("face@")) {
                String[] parts = geomName.substring(5).split(UNDERSCORE);
                if (parts.length == 3) {
                    try {
                        return VecPool.getVec3i(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    } catch (NumberFormatException e) {
                        logger.logWarning("Collided with invalidly named face: " + geomName);
                    }
                } else {
                    logger.logWarning("Collided with face with not enough coords: " + geomName);
                }
            } else {
                logger.logWarning("Collided with non-block face: " + geomName);
            }
        } else {
            logger.logWarning("Collided with too far face: " + geomName);
        }
        return null;

    }*/

}
