package net.acomputerdog.boxle.entity.types;

import com.jme3.renderer.Camera;
import net.acomputerdog.boxle.entity.Entity;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
import net.acomputerdog.boxle.world.World;

public class EntityPlayer extends Entity {

    private BoxleFlyByCamera flyby;
    private Camera cam;

    public EntityPlayer(World world) {
        super("Player", world, VecPool.getVec3f(0, 0, 0), VecPool.getVec3f(0, 0, 0));
    }

    public BoxleFlyByCamera getFlyby() {
        return flyby;
    }

    public Camera getCam() {
        return cam;
    }

    public void setFlyby(BoxleFlyByCamera flyby) {
        this.flyby = flyby;
        this.cam = flyby.getCamera();
    }
}
