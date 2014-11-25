package net.acomputerdog.boxle.world.gen.legacy;

import net.acomputerdog.boxle.block.registry.Blocks;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.gen.noise.OpenSimplexNoise;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class CellsWorldGen extends AbstractWorldGen {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private static final int SPLIT_HEIGHT = 0;
    private static final int MIN_SPIRE_HEIGHT = 5;
    private static final int SPIRE_HEIGHT_BOOST = 3;

    private final OpenSimplexNoise simplex;

    public CellsWorldGen(long seed) {
        simplex = new OpenSimplexNoise(seed);
    }

    @Override
    public void generateChunk(Chunk chunk) {
        BlockStorage blocks = chunk.getBlocks();
        chunk.setNeedsRebuild(true);
        Vec3i cLoc = chunk.getLocation();
        int chunkY = cLoc.y * chunkSize;
        if (chunkY + chunkSize < 0) {
            chunk.clear(Blocks.steel);
            return;
        }
        int chunkX = cLoc.x * chunkSize;
        int chunkZ = cLoc.z * chunkSize;
        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                int height = getGroundHeight(chunkX + x, chunkZ + z);
                for (int y = 0; y < chunkSize; y++) {
                    int currY = y + chunkY;
                    if (currY > height) {
                        blocks.setBlock(x, y, z, Blocks.air);
                    } else if (currY == height) {
                        blocks.setBlock(x, y, z, Blocks.grassySteel);
                    } else {
                        blocks.setBlock(x, y, z, Blocks.steel);
                    }
                }
            }
        }
    }

    @Override
    public int getGroundHeight(int x, int y) {
        int simplexHeight = getSimplexAt(x, y);
        int spireHeight = getSpireHeight(x, y);
        return simplexHeight >= spireHeight && simplexHeight > MIN_SPIRE_HEIGHT ? spireHeight : SPLIT_HEIGHT;
    }

    private int getSimplexAt(int x, int y) {
        return (int) Math.floor(simplex.eval(x / 20d, y / 20d) * 20d);
    }

    private int getSpireHeight(int x, int y) {
        x = (int) Math.floor(x / 8f);
        y = (int) Math.floor(y / 8f);
        return Math.max((int) Math.floor(simplex.eval(x / 20d, y / 20d) * 20d) + SPIRE_HEIGHT_BOOST, MIN_SPIRE_HEIGHT);
    }
}
