package net.acomputerdog.boxle.math.aabb;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;

public class AABBI {
    private final Vec3i corner1;
    private final Vec3i corner2;

    private Vec3i center;
    private int length; //z length
    private int width; //x length
    private int height; //y length

    public AABBI() {
        this(VecPool.getVec3i(0, 0, 0), VecPool.getVec3i(0, 0, 0));
    }

    public AABBI(Vec3i corner1, Vec3i corner2) {
        this.corner1 = corner1.copy();
        this.corner2 = corner2.copy();
        center = VecPool.createVec3i();
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
        int corner1X = corner1.x;
        int corner2X = corner2.x;
        if (corner1X > corner2X) {
            corner1.x = corner2X;
            corner2.x = corner1X;
        }
        int corner1Y = corner1.y;
        int corner2Y = corner2.y;
        if (corner1Y > corner2Y) {
            corner1.y = corner2Y;
            corner2.y = corner1Y;
        }
        int corner1Z = corner1.z;
        int corner2Z = corner2.z;
        if (corner1Z > corner2Z) {
            corner1.z = corner2Z;
            corner2.z = corner1Z;
        }
    }

    public void setCorner1(Vec3i corner1) {
        if (corner1 != null) {
            this.corner1.x = corner1.x;
            this.corner1.y = corner1.y;
            this.corner1.z = corner1.z;
        }
        sortCorners();
        calculateDims();
    }

    public void setCorner2(Vec3i corner2) {
        if (corner2 != null) {
            this.corner2.x = corner2.x;
            this.corner2.y = corner2.y;
            this.corner2.z = corner2.z;
        }
        sortCorners();
        calculateDims();
    }

    public void setCorners(Vec3i corner1, Vec3i corner2) {
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

    public Vec3i getCorner1() {
        return corner1.duplicate();
    }

    public Vec3i getCorner2() {
        return corner2.duplicate();
    }

    public Vec3i getCenter() {
        return center.duplicate();
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean collidesWith(AABBI other) {
        if (other == null) return false;
        if (other.equals(this)) return true;
        Vec3i other1 = other.corner1;
        Vec3i other2 = other.corner2;
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
        if (!(o instanceof AABBI)) return false;

        AABBI aabbi = (AABBI) o;

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
