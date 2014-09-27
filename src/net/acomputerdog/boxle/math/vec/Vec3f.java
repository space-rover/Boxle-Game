package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 3 floats
 */
public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f other) {
        this(other == null ? 0 : other.x, other == null ? 0 : other.y, other == null ? 0 : other.z); //written this way because java doesn't allow check for null before call to "this()"...
        if (other == null) {
            throw new IllegalArgumentException("Other Vec3f cannot be null!");
        }
    }

    public Vec3f(Vec2i vec2) {
        this(vec2 == null ? 0 : vec2.x, vec2 == null ? 0 : vec2.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec2 == null) {
            throw new IllegalArgumentException("Vec2i cannot be null!");
        }
    }

    public Vec3f(Vec2f vec2) {
        this(vec2 == null ? 0 : (int)Math.floor(vec2.x), vec2 == null ? 0 : (int)Math.floor(vec2.y)); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec2 == null) {
            throw new IllegalArgumentException("Vec2f cannot be null!");
        }
    }

    public Vec3f(Vec3i vec3) {
        this(vec3 == null ? 0 : vec3.x, vec3 == null ? 0 : vec3.y, vec3 == null ? 0 : vec3.z); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec3 == null) {
            throw new IllegalArgumentException("Vec3i cannot be null!");
        }
    }

    public Vec3f(float x, float y) {
        this(x, y, 0);
    }

    public Vec3f(float x) {
        this(x, 0, 0);
    }

    public Vec3f() {
        this(0, 0, 0);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vec3f duplicate() {
        return new Vec3f(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec3f)) return false;

        Vec3f vec3i = (Vec3f) o;
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
        return "Vec3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
