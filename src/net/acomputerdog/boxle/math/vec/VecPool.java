package net.acomputerdog.boxle.math.vec;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Pool of temporary Vec classes.
 * Should be used by calling getVec() to get a vector, and calling freeVec() once the Vec is no longer needed AND THERE ARE NO REMAINING REFERENCES TO IT!
 * VecPool is thread-safe.
 */
public class VecPool {
    private static int numVec3is = 0;
    private static final Queue<Vec3i> vec3is = new ConcurrentLinkedQueue<>();
    private static int numVec2is = 0;
    private static final Queue<Vec2i> vec2is = new ConcurrentLinkedQueue<>();
    private static int numVec3fs = 0;
    private static final Queue<Vec3f> vec3fs = new ConcurrentLinkedQueue<>();
    private static int numVec2fs = 0;
    private static final Queue<Vec2f> vec2fs = new ConcurrentLinkedQueue<>();

    public static void printSizes() {
        System.out.println(numVec3is + "/" + vec3is.size() + " -- " + numVec3fs + "/" + vec3fs.size() + " -- " + numVec2is + "/" + vec2is.size() + " -- " + numVec2fs + "/" + vec2fs.size());
    }

    private static void addVec3i(Vec3i vec) {
        vec3is.add(vec);
        numVec3is++;
    }

    private static Vec3i removeVec3i() {
        if (numVec3is > 2) { //intentionally set as 21 to leave a buffer for race conditions
            numVec3is--;
            Vec3i vec = vec3is.poll();
            return vec == null ? new Vec3i() : vec;
        }
        return new Vec3i();
    }

    private static void addVec3f(Vec3f vec) {
        vec3fs.add(vec);
        numVec3fs++;
    }

    private static Vec3f removeVec3f() {
        if (numVec3fs > 2) { //intentionally set as 2 to leave a buffer for race conditions
            numVec3fs--;
            Vec3f vec = vec3fs.poll();
            return vec == null ? new Vec3f() : vec;
        }
        return new Vec3f();
    }

    private static void addVec2i(Vec2i vec) {
        vec2is.add(vec);
        numVec2is++;
    }

    private static Vec2i removeVec2i() {
        if (numVec2is > 2) { //intentionally set as 2 to leave a buffer for race conditions
            numVec2is--;
            Vec2i vec = vec2is.poll();
            return vec == null ? new Vec2i() : vec;
        }
        return new Vec2i();
    }

    private static void addVec2f(Vec2f vec) {
        vec2fs.add(vec);
        numVec2fs++;
    }

    private static Vec2f removeVec2f() {
        if (numVec2fs > 2) { //intentionally set as 2 to leave a buffer for race conditions
            numVec2fs--;
            Vec2f vec = vec2fs.poll();
            return vec == null ? new Vec2f() : vec;
        }
        return new Vec2f();
    }

    /**
     * Initializes the VecPool with 5 of each type of vec.
     */
    public static void init() {
        int loops = 0;
        while (loops < 5) {
            addVec2f(new Vec2f());
            addVec3f(new Vec3f());
            addVec2i(new Vec2i());
            addVec3i(new Vec3i());
            loops++;
        }
    }

    public static Vec3i getVec3i() {
        return removeVec3i();
    }

    public static Vec3i getVec3i(int x, int y, int z) {
        Vec3i vec = getVec3i();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec3f getVec3f() {
        return removeVec3f();
    }

    public static Vec3f getVec3f(float x, float y, float z) {
        Vec3f vec = getVec3f();
        vec.x = x;
        vec.y = y;
        vec.z = z;
        return vec;
    }

    public static Vec2i getVec2i() {
        return removeVec2i();
    }

    public static Vec2i getVec2i(int x, int y) {
        Vec2i vec = getVec2i();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static Vec2f getVec2f() {
        return removeVec2f();
    }

    public static Vec2f getVec2f(float x, float y) {
        Vec2f vec = getVec2f();
        vec.x = x;
        vec.y = y;
        return vec;
    }

    public static void free(Vec3i vec) {
        if (vec != null) {
            addVec3i(vec);
        }
    }

    public static void free(Vec3f vec) {
        if (vec != null) {
            addVec3f(vec);
        }
    }

    public static void free(Vec2i vec) {
        if (vec != null) {
            addVec2i(vec);
        }
    }

    public static void free(Vec2f vec) {
        if (vec != null) {
            addVec2f(vec);
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
