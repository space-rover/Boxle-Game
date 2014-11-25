package net.acomputerdog.boxle.world.gen.simple;

import net.acomputerdog.boxle.block.registry.Blocks;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.noise.OpenSimplexNoise;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class Simplex3DWorldGen extends AbstractWorldGen {
    private static final double coordinateScale = 50d;
    private static final double groundPercentage = .3d;
    private static final double dirtDepth = .1d;

    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final OpenSimplexNoise simplex;

    public Simplex3DWorldGen(long seed) {
        simplex = new OpenSimplexNoise(seed);
    }

    @Override
    public void generateChunk(Chunk chunk) {
        BlockStorage blocks = chunk.getBlocks();
        chunk.setNeedsRebuild(true);
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
                    if (val < groundPercentage) {
                        blocks.setBlock(x, y, z, Blocks.air);
                    } else if (val < groundPercentage + dirtDepth) {
                        if (getSimplex(currX, currY + 1, currZ) < groundPercentage) {
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

    private double getSimplex(int x, int y, int z) {
        return simplex.eval(x / coordinateScale, y / coordinateScale, z / coordinateScale);
    }

    @Override
    public int getGroundHeight(int x, int y) {
        throw new UnsupportedOperationException("Simplex3DWorldGen does not support ground height!");
    }
}
