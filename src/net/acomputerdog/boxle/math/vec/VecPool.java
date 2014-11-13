package net.acomputerdog.boxle.math.vec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pool of temporary Vec classes.
 * Should be used by calling getVec() to get a vector, and calling freeVec() once the Vec is no longer needed AND THERE ARE NO REMAINING REFERENCES TO IT!
 * VecPool is thread-safe.
 */
public class VecPool {
    private static final Map<Thread, ThreadVecPool> threadPools = new ConcurrentHashMap<>();

    private static ThreadVecPool getThreadPool() {
        Thread thread = Thread.currentThread();
        ThreadVecPool pool = threadPools.get(thread);
        if (pool == null) {
            threadPools.put(thread, pool = new ThreadVecPool());
        }
        return pool;
    }

    /**
     * Initializes the VecPool with 5 of each type of vec.
     */
    public static void init() {
        int loops = 0;
        while (loops < 5) {
            getThreadPool().addVec2f(new Vec2f());
            getThreadPool().addVec3f(new Vec3f());
            getThreadPool().addVec2i(new Vec2i());
            getThreadPool().addVec3i(new Vec3i());
            loops++;
        }
    }

    public static Vec3i getVec3i() {
        return getThreadPool().getVec3i();
    }

    public static Vec3i getVec3i(int x, int y, int z) {
        Vec3i vec = getVec3i();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec3f getVec3f() {
        return getThreadPool().getVec3f();
    }

    public static Vec3f getVec3f(float x, float y, float z) {
        Vec3f vec = getVec3f();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec2i getVec2i() {
        return getThreadPool().getVec2i();
    }

    public static Vec2i getVec2i(int x, int y) {
        Vec2i vec = getVec2i();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static Vec2f getVec2f() {
        return getThreadPool().getVec2f();
    }

    public static Vec2f getVec2f(float x, float y) {
        Vec2f vec = getVec2f();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static void free(Vec3i vec) {
        if (vec != null) {
            getThreadPool().addVec3i(vec);
        }
    }

    public static void free(Vec3f vec) {
        if (vec != null) {
            getThreadPool().addVec3f(vec);
        }
    }

    public static void free(Vec2i vec) {
        if (vec != null) {
            getThreadPool().addVec2i(vec);
        }
    }

    public static void free(Vec2f vec) {
        if (vec != null) {
            getThreadPool().addVec2f(vec);
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

    public static Vec3i createVec3i() {
        return new Vec3i();
    }

    public static Vec3f createVec3f() {
        return new Vec3f();
    }

    public static Vec2i createVec2i() {
        return new Vec2i();
    }

    public static Vec2f createVec2f() {
        return new Vec2f();
    }

    public static Vec3i createVec3i(Vec3i other) {
        return new Vec3i(other);
    }

    public static Vec3f createVec3f(Vec3f other) {
        return new Vec3f(other);
    }

    public static Vec2i createVec2i(Vec2i other) {
        return new Vec2i(other);
    }

    public static Vec2f createVec2f(Vec2f other) {
        return new Vec2f(other);
    }
}
