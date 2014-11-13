package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.world.Chunk;

import java.util.HashMap;
import java.util.Map;

public class CachingWorldGen implements WorldGen {

    private final WorldGen realGen;
    private final Map<Vec2i, Integer> heightCache = new HashMap<>();

    public CachingWorldGen(WorldGen realGen) {
        this.realGen = realGen;
    }

    @Override
    public void generateChunk(Chunk chunk) {
        realGen.generateChunk(chunk);
    }

    @Override
    public int getGroundHeight(int x, int y) {
        Vec2i temp = VecPool.getVec2i(x, y);
        Integer val = heightCache.get(temp);
        if (val == null) {
            heightCache.put(VecPool.createVec2i(temp), val = realGen.getGroundHeight(x, y));
        }
        VecPool.free(temp);
        return val;
    }

    @Override
    public int getGroundHeight(Vec2i loc) {
        return getGroundHeight(loc.x, loc.y);
    }

    @Override
    public Vec3i getGroundHeight(Vec3i loc) {
        loc.y = getGroundHeight(loc.x, loc.z);
        return loc;
    }

    public WorldGen getRealGen() {
        return realGen;
    }
}
