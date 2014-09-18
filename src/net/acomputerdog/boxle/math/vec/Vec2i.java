package net.acomputerdog.boxle.math.vec;

import net.acomputerdog.core.hash.Hash;

/**
 * A class containing a vector of 3 ints
 */
public class Vec2i {
    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i other) {
        this(other == null ? 0 : other.x, other == null ? 0 : other.y); //written this way because java doesn't allow check for null before call to "this()"...
        if (other == null) {
            throw new IllegalArgumentException("Other Vec2i cannot be null!");
        }
    }

    public Vec2i(int x) {
        this(x, 0);
    }

    public Vec2i() {
        this(0, 0);
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

    public Vec2i duplicate() {
        return new Vec2i(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec2i)) return false;

        Vec2i vec3i = (Vec2i) o;
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