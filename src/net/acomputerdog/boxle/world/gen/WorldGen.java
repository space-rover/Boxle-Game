package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;

public interface WorldGen {
    public void generateTerrain(Chunk chunk);

    public void generateDecorations(Chunk chunk);

    public float getRainfall(Vec3i loc);

    public float getTemperature(Vec3i loc);

    public long getSeed();
}
