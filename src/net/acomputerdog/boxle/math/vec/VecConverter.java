package net.acomputerdog.boxle.math.vec;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class VecConverter {
    public static Vector3f vec3fToVector3f(Vec3f vec) {
        return new Vector3f(vec.x, vec.y, vec.z);
    }

    public static Quaternion vec3fToQuaternion(Vec3f vec) {
        return new Quaternion(vec.x, vec.y, vec.z, 1);
    }

    public static Vec3f vector3fToVec3f(Vector3f vec) {
        return VecPool.getVec3f(vec.x, vec.y, vec.z);
    }

    public static Vec3f quaternionToVec3f(Quaternion quat) {
        return VecPool.getVec3f(quat.getX(), quat.getY(), quat.getZ());
    }

    public static Vec3i floorVec3iFromVec3f(Vec3f vec) {
        Vec3i vecI = VecPool.getVec3i();
        vecI.x = (int) Math.floor(vec.x);
        vecI.y = (int) Math.floor(vec.y);
        vecI.z = (int) Math.floor(vec.z);
        return vecI;
    }

    public static Vec3i floorVec3iFromVec3f(Vec3f vec, Vec3i vec3i) {
        vec3i.x = (int) Math.floor(vec.x);
        vec3i.y = (int) Math.floor(vec.y);
        vec3i.z = (int) Math.floor(vec.z);
        return vec3i;
    }

    public static Vec3i roundVec3iFromVec3f(Vec3f vec) {
        Vec3i vecI = VecPool.getVec3i();
        vecI.x = Math.round(vec.x);
        vecI.y = Math.round(vec.y);
        vecI.z = Math.round(vec.z);
        return vecI;
    }

    public static Vec3i roundVec3iFromVec3f(Vec3f vec, Vec3i vec3i) {
        vec3i.x = Math.round(vec.x);
        vec3i.y = Math.round(vec.y);
        vec3i.z = Math.round(vec.z);
        return vec3i;
    }
}
