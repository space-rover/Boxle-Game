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
}
