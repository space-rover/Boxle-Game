package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 3 floats
 */
public class Vec2f {
    public float x;
    public float y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f(Vec2f other) {
        this(other == null ? 0 : other.x, other == null ? 0 : other.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (other == null) {
            throw new IllegalArgumentException("Other Vec2f cannot be null!");
        }
    }

    public Vec2f(float x) {
        this(x, 0);
    }

    public Vec2f() {
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
        return new Vec2f(this);
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
