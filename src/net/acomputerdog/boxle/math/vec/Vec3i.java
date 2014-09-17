package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 3 ints
 */
public class Vec3i {
    public int x;
    public int y;
    public int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(int x, int y) {
        this(x, y, 0);
    }

    public Vec3i(int x) {
        this(x, 0, 0);
    }

    public Vec3i() {
        this(0, 0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec3i)) return false;

        Vec3i vec3i = (Vec3i) o;
        return x == vec3i.x && y == vec3i.y && z == vec3i.z;
    }

    @Override
    public int hashCode() {
        int result = Hash.SEED;
        result = Hash.hash(result, x);
        result = Hash.hash(result, y);
        result = Hash.hash(result, z);
        return result;
    }

    @Override
    public String toString() {
        return "Vec3i{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
