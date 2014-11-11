package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 3 ints
 */
public class Vec3i {
    public int x;
    public int y;
    public int z;

    Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vec3i(Vec3i other) {
        this(other == null ? 0 : other.x, other == null ? 0 : other.y, other == null ? 0 : other.z); //written this way because java doesn't allow check for null before call to "this()"...
        if (other == null) {
            throw new IllegalArgumentException("Other Vec3i cannot be null!");
        }
    }

    Vec3i(Vec2i vec2) {
        this(vec2 == null ? 0 : vec2.x, vec2 == null ? 0 : vec2.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec2 == null) {
            throw new IllegalArgumentException("Vec2i cannot be null!");
        }
    }

    Vec3i(Vec2f vec2) {
        this(vec2 == null ? 0 : (int) Math.floor(vec2.x), vec2 == null ? 0 : (int) Math.floor(vec2.y)); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec2 == null) {
            throw new IllegalArgumentException("Vec2f cannot be null!");
        }
    }

    Vec3i(Vec3f vec3) {
        this(vec3 == null ? 0 : (int) Math.floor(vec3.x), vec3 == null ? 0 : (int) Math.floor(vec3.y), vec3 == null ? 0 : (int) Math.floor(vec3.z)); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec3 == null) {
            throw new IllegalArgumentException("Vec3f cannot be null!");
        }
    }

    Vec3i(int x, int y) {
        this(x, y, 0);
    }

    Vec3i(int x) {
        this(x, 0, 0);
    }

    Vec3i() {
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

    public Vec3i duplicate() {
        return VecPool.copy(this);
    }

    public String asCoords() {
        return x + "," + y + "," + z;
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
