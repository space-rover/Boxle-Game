package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.block.block.Blocks;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.noise.OpenSimplexNoise;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class SimplexWorldGen extends AbstractWorldGen {
    private static final double COORD_SCALE = 50d;
    private static final double TERRAIN_PERCENTAGE = .3d;
    private static final double DIRT_THICKNESS = .1d;

    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final OpenSimplexNoise simplex;

    public SimplexWorldGen(long seed) {
        super(seed);
        simplex = new OpenSimplexNoise(seed);
    }

    @Override
    public void generateTerrain(Chunk chunk) {
        if (chunk.isGenerated()) {
            chunk.getWorld().getLogger().logWarning("Attempted to generate a chunk twice at " + chunk.getCoords());
        } else {
            BlockStorage blocks = chunk.getBlocks();
            Vec3i cLoc = chunk.getLocation();
            int chunkY = cLoc.y * chunkSize;
            int chunkX = cLoc.x * chunkSize;
            int chunkZ = cLoc.z * chunkSize;
            for (int x = 0; x < chunkSize; x++) {
                for (int z = 0; z < chunkSize; z++) {
                    for (int y = 0; y < chunkSize; y++) {
                        int currX = x + chunkX;
                        int currY = y + chunkY;
                        int currZ = z + chunkZ;
                        double val = getSimplex(currX, currY, currZ);
                        if (val < TERRAIN_PERCENTAGE) {
                            blocks.setBlock(x, y, z, Blocks.air);
                        } else if (val < TERRAIN_PERCENTAGE + DIRT_THICKNESS) {
                            if (getSimplex(currX, currY + 1, currZ) < TERRAIN_PERCENTAGE) {
                                blocks.setBlock(x, y, z, Blocks.grass);
                            } else {
                                blocks.setBlock(x, y, z, Blocks.dirt);
                            }
                        } else {
                            blocks.setBlock(x, y, z, Blocks.stone);
                        }
                    }
                }
            }
        }
        chunk.markGenerated();
        chunk.setNeedsRebuild(true);
    }

    private double getSimplex(int x, int y, int z) {
        return simplex.eval(x / COORD_SCALE, y / COORD_SCALE, z / COORD_SCALE);
    }

    @Override
    public float getRainfall(Vec3i loc) {
        return (float) simplex.eval(loc.x / COORD_SCALE, loc.z / COORD_SCALE);
    }

    @Override
    public float getTemperature(Vec3i loc) {
        return (float) simplex.eval(loc.x / COORD_SCALE, loc.z / COORD_SCALE);
    }
}
