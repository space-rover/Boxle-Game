package net.acomputerdog.boxle.render.util;

import com.jme3.input.FlyByCamera;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import net.acomputerdog.boxle.entity.types.EntityPlayer;
import net.acomputerdog.boxle.math.vec.Vec3f;

public class BoxleFlyByCamera extends FlyByCamera {
    private EntityPlayer player;

    /**
     * Creates a new FlyByCamera to control the given Camera object.
     *
     * @param cam The camera to control
     */
    public BoxleFlyByCamera(Camera cam) {
        super(cam);
    }

    public void moveCameraStraight(float distance) {
        moveCamera(distance, false);
        updatePlayerPos();
    }

    public void moveCameraVert(float distance) {
        riseCamera(distance);
        updatePlayerPos();
    }

    public void moveCameraSide(float distance) {
        moveCamera(distance, true);
        updatePlayerPos();
    }

    @Override
    protected void rotateCamera(float value, Vector3f axis) {
        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        if (up.y >= 0 && up.y <= 180) {
            cam.setAxes(q);
            updatePlayerRot();
        }
    }

    @Override
    protected void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        pos.addLocal(vel);

        cam.setLocation(pos);
    }

    @Override
    protected void moveCamera(float value, boolean sideways) {
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways) {
            cam.getLeft(vel);
        } else {
            cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        pos.setX(pos.getX() + vel.getX());
        pos.setZ(pos.getZ() + vel.getZ());

        cam.setLocation(pos);
    }

    @Override
    public void setEnabled(boolean enable) {
        enabled = enable;
        inputManager.setCursorVisible(!enable);
    }

    public void toggleMouseGrabbed() {
        setEnabled(!enabled);
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    private void updatePlayerPos() {
        if (player != null) {
            Vector3f pos = cam.getLocation();
            Vec3f pLoc = player.getLocation();
            pLoc.x = pos.x;
            pLoc.y = pos.y;
            pLoc.z = pos.z;
        }
    }

    private void updatePlayerRot() {
        if (player != null) {
            Quaternion rot = cam.getRotation();
            Vec3f pRot = player.getRotation();
            pRot.x = rot.getX();
            pRot.y = rot.getY();
            pRot.z = rot.getZ();
        }
    }

    public Camera getCamera() {
        return super.cam;
    }
}
