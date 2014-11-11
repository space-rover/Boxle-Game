package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 2 floats
 */
public class Vec2f {
    public float x;
    public float y;

    Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vec2f(Vec2f other) {
        this(other == null ? 0 : other.x, other == null ? 0 : other.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (other == null) {
            throw new IllegalArgumentException("Other Vec2f cannot be null!");
        }
    }

    Vec2f(Vec2i vec2) {
        this(vec2 == null ? 0 : vec2.x, vec2 == null ? 0 : vec2.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec2 == null) {
            throw new IllegalArgumentException("Vec2i cannot be null!");
        }
    }

    Vec2f(Vec3i vec3) {
        this(vec3 == null ? 0 : vec3.x, vec3 == null ? 0 : vec3.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec3 == null) {
            throw new IllegalArgumentException("Vec3i cannot be null!");
        }
    }

    Vec2f(Vec3f vec3) {
        this(vec3 == null ? 0 : vec3.x, vec3 == null ? 0 : vec3.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (vec3 == null) {
            throw new IllegalArgumentException("Vec3f cannot be null!");
        }
    }

    Vec2f(float x) {
        this(x, 0);
    }

    Vec2f() {
        this(0, 0);
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

    public Vec2f duplicate() {
        return VecPool.copy(this);
    }

    public String asCoords() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec2f)) return false;

        Vec2f vec3i = (Vec2f) o;
        return x == vec3i.x && y == vec3i.y;
    }

    @Override
    public int hashCode() {
        int result = Hash.SEED;
        result = Hash.hash(result, x);
        result = Hash.hash(result, y);
        return result;
    }

    @Override
    public String toString() {
        return "Vec2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
