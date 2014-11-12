package net.acomputerdog.boxle.math.vec;

import java.util.LinkedList;
import java.util.List;

public class ThreadVecPool {
    private final List<Vec3i> vec3is = new LinkedList<>();
    private final List<Vec2i> vec2is = new LinkedList<>();
    private final List<Vec3f> vec3fs = new LinkedList<>();
    private final List<Vec2f> vec2fs = new LinkedList<>();

    Vec3i getVec3i() {
        //if (vec3is.size() > 0) {
        //    return vec3is.remove(0);
        //}
        return new Vec3i();
    }

    void addVec3i(Vec3i vec) {
        if (vec != null) {
            //vec3is.add(vec);
        }
    }

    Vec2i getVec2i() {
        if (vec2is.size() > 0) {
            return vec2is.remove(0);
        }
        return new Vec2i();
    }

    void addVec2i(Vec2i vec) {
        if (vec != null) {
            vec2is.add(vec);
        }
    }

    Vec3f getVec3f() {
        if (vec3fs.size() > 0) {
            return vec3fs.remove(0);
        }
        return new Vec3f();
    }

    void addVec3f(Vec3f vec) {
        if (vec != null) {
            vec3fs.add(vec);
        }
    }

    Vec2f getVec2f() {
        if (vec2fs.size() > 0) {
            return vec2fs.remove(0);
        }
        return new Vec2f();
    }

    void addVec2f(Vec2f vec) {
        if (vec != null) {
            vec2fs.add(vec);
        }
    }
}
