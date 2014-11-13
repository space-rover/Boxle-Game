package net.acomputerdog.boxle.math.aabb;

import net.acomputerdog.boxle.math.vec.Vec3f;
import net.acomputerdog.boxle.math.vec.VecPool;

public class AABBF {
    private final Vec3f corner1;
    private final Vec3f corner2;

    private Vec3f center;
    private float length; //z length
    private float width; //x length
    private float height; //y length

    public AABBF() {
        this(VecPool.getVec3f(0, 0, 0), VecPool.getVec3f(0, 0, 0));
    }

    public AABBF(Vec3f corner1, Vec3f corner2) {
        this.corner1 = corner1.copy();
        this.corner2 = corner2.copy();
        center = VecPool.createVec3f();
        sortCorners();
        calculateDims();
    }

    private void calculateDims() {
        length = Math.abs(corner2.z - corner1.z);
        width = Math.abs(corner2.x - corner1.x);
        height = Math.abs(corner2.y - corner1.y);
        center.x = (corner2.x + corner1.x) / 2;
        center.y = (corner2.y + corner1.y) / 2;
        center.z = (corner2.z + corner1.z) / 2;
    }

    private void sortCorners() {
        float corner1X = corner1.x;
        float corner2X = corner2.x;
        if (corner1X > corner2X) {
            corner1.x = corner2X;
            corner2.x = corner1X;
        }
        float corner1Y = corner1.y;
        float corner2Y = corner2.y;
        if (corner1Y > corner2Y) {
            corner1.y = corner2Y;
            corner2.y = corner1Y;
        }
        float corner1Z = corner1.z;
        float corner2Z = corner2.z;
        if (corner1Z > corner2Z) {
            corner1.z = corner2Z;
            corner2.z = corner1Z;
        }
    }

    public void setCorner1(Vec3f corner1) {
        if (corner1 != null) {
            this.corner1.x = corner1.x;
            this.corner1.y = corner1.y;
            this.corner1.z = corner1.z;
        }
        sortCorners();
        calculateDims();
    }

    public void setCorner2(Vec3f corner2) {
        if (corner2 != null) {
            this.corner2.x = corner2.x;
            this.corner2.y = corner2.y;
            this.corner2.z = corner2.z;
        }
        sortCorners();
        calculateDims();
    }

    public void setCorners(Vec3f corner1, Vec3f corner2) {
        if (corner1 != null) {
            this.corner1.x = corner1.x;
            this.corner1.y = corner1.y;
            this.corner1.z = corner1.z;
        }
        if (corner2 != null) {
            this.corner2.x = corner2.x;
            this.corner2.y = corner2.y;
            this.corner2.z = corner2.z;
        }
        sortCorners();
        calculateDims();
    }

    public Vec3f getCorner1() {
        return corner1.duplicate();
    }

    public Vec3f getCorner2() {
        return corner2.duplicate();
    }

    public Vec3f getCenter() {
        return center.duplicate();
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean collidesWith(AABBF other) {
        if (other == null) return false;
        if (other.equals(this)) return true;
        Vec3f other1 = other.corner1;
        Vec3f other2 = other.corner2;
        return !((corner2.x < other1.x) ||
                (corner2.y < other1.y) ||
                (corner2.z < other1.z) ||
                (corner1.x > other2.x) ||
                (corner1.y > other2.y) ||
                (corner1.z > other2.z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AABBF)) return false;

        AABBF aabbi = (AABBF) o;

        return corner1.equals(aabbi.corner1) && corner2.equals(aabbi.corner2);

    }

    @Override
    public int hashCode() {
        int result = corner1.hashCode();
        result = 31 * result + corner2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AABBI{" +
                "corner2=" + corner2 +
                ", corner1=" + corner1 +
                '}';
    }
}
