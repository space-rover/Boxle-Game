package net.acomputerdog.boxle.input;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3f;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.core.java.Patterns;
import net.acomputerdog.core.logger.CLogger;

import java.util.Collection;

/**
 * Handles input for the game client.
 */
public class InputHandler implements ActionListener, AnalogListener {

    private static final String UNDERSCORE = Patterns.quote("_");
    private static final float COLLISION_TOLERANCE = 1f / 1024f;
    private static final float MAX_REACH_DISTANCE = 10f;
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


    private Block[] blockArr;
    private int blockIndex;
    private Block currBlock;
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

        inputManager.addMapping("Select Next Block", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("Select Prev Block", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

        inputManager.addMapping("Debug", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(this, "Debug");

        inputManager.addListener(this, "Move Left", "Move Right", "Move Forward", "Move Back", "Move Up", "Move Down", "Exit", "Pause", "Sprint", "Break Block", "Place Block", "Select Next Block", "Select Prev Block");

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
        } else if (!isPressed && "Pause".equals(name)) {
            flyby.toggleMouseGrabbed();
        } else if (!isPressed && "Debug".equals(name)) {
            EntityPlayer player = engine.getBoxle().getClient().getPlayer();
            Vec3i pLoc = VecConverter.floorVec3iFromVec3f(player.getLocation());
            logger.logDebug("Player position: " + pLoc.asCoords() + " OR " + CoordConverter.globalToChunk(pLoc.duplicate()).asCoords() + "/" + CoordConverter.globalToBlock(pLoc.duplicate()));
            World world = player.getWorld();
            pLoc.y++;
            world.setBlockAt(pLoc, Blocks.acomputerdog, true);
            pLoc.y -= 2;
            world.setBlockAt(pLoc, Blocks.seamusFD, true);
            VecPool.free(pLoc);
        } else if ("Sprint".equals(name)) {
            flyby.setMoveSpeed(isPressed ? 2f : 1f);
        } else if (isPressed && "Break Block".equals(name)) {
            Vec3i loc = findClickedLoc(true);
            if (loc != null) {
                World world = Boxle.instance().getClient().getPlayer().getWorld();
                Block block = world.getBlockAt(loc);
                if (block != null && block.isBreakable()) {
                    world.setBlockAt(loc, Blocks.air, true);
                }
                VecPool.free(loc);
            }
        } else if (isPressed && "Place Block".equals(name)) {
            Vec3i loc = findClickedLoc(false);
            if (loc != null) {
                if (currBlock == null) {
                    findNextBlock();
                }
                Boxle.instance().getClient().getPlayer().getWorld().setBlockAt(loc, currBlock, true);
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
            case "Select Next Block":
                findNextBlock();
                break;
            case "Select Prev Block":
                findPrevBlock();
                break;
            default:
        }
    }

    private Vec3i findClickedLoc(boolean inside) {
        CollisionResults results = new CollisionResults();
        Camera cam = Boxle.instance().getCamera();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        Node collision = Boxle.instance().getRenderEngine().getTerrainNode();
        collision.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        if (result == null) {
            return null;
        }
        if (result.getDistance() > MAX_REACH_DISTANCE) {
            return null;
        }
        Vec3f loc = VecConverter.vector3fToVec3f(result.getContactPoint());
        Vec3i locI = VecConverter.floorVec3iFromVec3f(loc);
        World world = Boxle.instance().getClient().getPlayer().getWorld();
        float distanceX = Math.abs(loc.x - Math.round(loc.x));
        float distanceY = Math.abs(loc.y - Math.round(loc.y));
        float distanceZ = Math.abs(loc.z - Math.round(loc.z));
        if (distanceX <= COLLISION_TOLERANCE) {
            if (distanceX <= distanceY && distanceX <= distanceZ) {
                if (checkBlock(Math.round(loc.x), locI.y, locI.z, locI, world, inside) != null) return locI;
                if (checkBlock(Math.round(loc.x - 1), locI.y, locI.z, locI, world, inside) != null) return locI;
            }
        }
        if (distanceY <= COLLISION_TOLERANCE) {
            if (distanceY <= distanceX && distanceY <= distanceZ) {
                if (checkBlock(locI.x, Math.round(loc.y), locI.z, locI, world, inside) != null) return locI;
                if (checkBlock(locI.x, Math.round(loc.y - 1), locI.z, locI, world, inside) != null) return locI;
            }
        }
        if (distanceZ <= COLLISION_TOLERANCE) {
            if (distanceZ <= distanceX && distanceZ <= distanceY) {
                if (checkBlock(locI.x, locI.y, Math.round(loc.z), locI, world, inside) != null) return locI;
                if (checkBlock(locI.x, locI.y, Math.round(loc.z - 1), locI, world, inside) != null) return locI;
            }
        }
        VecPool.free(loc);
        VecPool.free(locI);
        return null;
    }

    private Vec3i checkBlock(int x, int y, int z, Vec3i out, World world, boolean inside) {
        if (world.getBlockAt(x, y, z).isCollidable() == inside) {
            out.x = x;
            out.y = y;
            out.z = z;
            return out;
        }
        return null;
    }

    private void findNextBlock() {
        if (blockArr == null || blockIndex >= blockArr.length || blockIndex < 0) {
            Collection<Block> blockCollection = Blocks.BLOCKS.getItems();
            blockArr = blockCollection.toArray(new Block[blockCollection.size()]);
            blockIndex = 0;
        }
        currBlock = blockArr[blockIndex];
        blockIndex++;
        if (currBlock == Blocks.air) {
            findNextBlock();
        }
    }

    private void findPrevBlock() {
        if (blockArr == null || blockIndex < 0 || blockIndex >= blockArr.length) {
            Collection<Block> blockCollection = Blocks.BLOCKS.getItems();
            blockArr = blockCollection.toArray(new Block[blockCollection.size()]);
            blockIndex = blockArr.length - 1;
        }
        currBlock = blockArr[blockIndex];
        blockIndex--;
        if (currBlock == Blocks.air) {
            findPrevBlock();
        }
    }

}
