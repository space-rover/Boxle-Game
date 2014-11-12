package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.block.Blocks;
import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class AngleWorldGen implements WorldGen {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    @Override
    public void generateChunk(Chunk chunk) {
        BlockStorage blocks = chunk.getBlocks();
        chunk.setChanged(true);
        Vec3i cLoc = chunk.getLocation();
        int chunkY = cLoc.y * chunkSize;
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
                        blocks.setBlock(x, y, z, Blocks.grass);
                    } else if (currY - height >= -3) {
                        blocks.setBlock(x, y, z, Blocks.dirt);
                    } else {
                        blocks.setBlock(x, y, z, Blocks.stone);
                    }
                }
            }
        }
    }

    @Override
    public int getGroundHeight(int x, int y) {
        if (x >= 0) {
            return x / 2;
        } else {
            return y / 2;
        }
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
}
