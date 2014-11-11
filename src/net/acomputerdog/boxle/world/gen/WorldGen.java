package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;

public interface WorldGen {
    public void generateChunk(Chunk chunk);

    public int getGroundHeight(int x, int y);

    public int getGroundHeight(Vec2i loc);

    public Vec3i getGroundHeight(Vec3i loc);

}
