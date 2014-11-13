package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;

public abstract class AbstractWorldGen implements WorldGen {
    @Override
    public int getGroundHeight(Vec2i loc) {
        return getGroundHeight(loc.x, loc.y);
    }

    @Override
    public Vec3i getGroundHeight(Vec3i loc) {
        loc.y = getGroundHeight(loc.x, loc.z);
        return loc;
    }
}
