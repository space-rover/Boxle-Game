package net.acomputerdog.boxle.math.vec;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Pool of temporary Vec classes.
 * Should be used by calling getVec() to get a vector, and calling freeVec() once the Vec is no longer needed AND THERE ARE NO REMAINING REFERENCES TO IT!
 * VecPool is thread-safe.
 * TODO replace Lists with Queues
 */
public class VecPool {
    private static final List<Vec3i> vec3is = new CopyOnWriteArrayList<>();
    private static final List<Vec2i> vec2is = new CopyOnWriteArrayList<>();
    private static final List<Vec3f> vec3fs = new CopyOnWriteArrayList<>();
    private static final List<Vec2f> vec2fs = new CopyOnWriteArrayList<>();

    /**
     * Initializes the VecPool with 5 of each type of vec.
     */
    public static void init() {
        int loops = 0;
        while (loops < 5) {
            vec2fs.add(new Vec2f());
            vec3fs.add(new Vec3f());
            vec2is.add(new Vec2i());
            vec3is.add(new Vec3i());
            loops++;
        }
    }

    public static Vec3i getVec3i() {
        if (vec3is.size() >= 0) {
            return vec3is.remove(0);
        }
        return new Vec3i();
    }

    public static Vec3i getVec3i(int x, int y, int z) {
        Vec3i vec = getVec3i();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec3f getVec3f() {
        if (vec3fs.size() >= 0) {
            return vec3fs.remove(0);
        }
        return new Vec3f();
    }

    public static Vec3f getVec3f(float x, float y, float z) {
        Vec3f vec = getVec3f();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec2i getVec2i() {
        if (vec2is.size() >= 0) {
            return vec2is.remove(0);
        }
        return new Vec2i();
    }

    public static Vec2i getVec2i(int x, int y) {
        Vec2i vec = getVec2i();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static Vec2f getVec2f() {
        if (vec2fs.size() >= 0) {
            return vec2fs.remove(0);
        }
        return new Vec2f();
    }

    public static Vec2f getVec2f(float x, float y) {
        Vec2f vec = getVec2f();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static void freeVec3i(Vec3i vec) {
        if (vec != null) {
            vec3is.add(vec);
        }
    }

    public static void freeVec3f(Vec3f vec) {
        if (vec != null) {
            vec3fs.add(vec);
        }
    }

    public static void freeVec2i(Vec2i vec) {
        if (vec != null) {
            vec2is.add(vec);
        }
    }

    public static void freeVec2f(Vec2f vec) {
        if (vec != null) {
            vec2fs.add(vec);
        }
    }

    public static Vec3i copy(Vec3i existing) {
        if (existing == null) {
            return null;
        }
        Vec3i newVec = getVec3i();
        newVec.x = existing.x;
        newVec.y = existing.y;
        newVec.z = existing.z;
        return newVec;
    }

    public static Vec2i copy(Vec2i existing) {
        if (existing == null) {
            return null;
        }
        Vec2i newVec = getVec2i();
        newVec.x = existing.x;
        newVec.y = existing.y;
        return newVec;
    }

    public static Vec3f copy(Vec3f existing) {
        if (existing == null) {
            return null;
        }
        Vec3f newVec = getVec3f();
        newVec.x = existing.x;
        newVec.y = existing.y;
        newVec.z = existing.z;
        return newVec;
    }

    public static Vec2f copy(Vec2f existing) {
        if (existing == null) {
            return null;
        }
        Vec2f newVec = getVec2f();
        newVec.x = existing.x;
        newVec.y = existing.y;
        return newVec;
    }
}
