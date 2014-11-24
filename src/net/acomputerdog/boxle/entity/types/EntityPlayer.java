package net.acomputerdog.boxle.entity.types;

import com.jme3.renderer.Camera;
import net.acomputerdog.boxle.block.registry.Blocks;
import net.acomputerdog.boxle.entity.Entity;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecConverter;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.render.util.BoxleFlyByCamera;
import net.acomputerdog.boxle.world.World;

public class EntityPlayer extends Entity {

    private BoxleFlyByCamera flyby;
    private Camera cam;

    public EntityPlayer(World world) {
        super("Player", world, VecPool.getVec3f(), VecPool.getVec3f());
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

    @Override
    public void onSpawn() {
        Vec3i pLoc = VecConverter.vec3iFromVec3f(location);
        int origY = pLoc.y;
        while (world.getBlockAt(pLoc) != Blocks.air && pLoc.y - origY <= 100) {
            pLoc.y++;
        }
        location.y = pLoc.y + 1;
        flyby.moveCameraVert(location.y - origY);
        VecPool.free(pLoc);
    }
}
