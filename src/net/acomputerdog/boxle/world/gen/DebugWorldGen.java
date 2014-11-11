package net.acomputerdog.boxle.world.gen;

import net.acomputerdog.boxle.block.Blocks;
import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.World;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class DebugWorldGen implements WorldGen {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final World world;

    public DebugWorldGen(World world) {
        this.world = world;
    }

    @Override
    public void generateChunk(Chunk chunk) {
        BlockStorage blocks = chunk.getBlocks();
        chunk.setChanged(true);
        int chunkY = chunk.getLocation().y * chunkSize;
        //cPos.x = (cPos.x * chunkSize) + chunkSize;//cpos contains the block location of the LAST block in the chunk
        //cPos.y = (cPos.y * chunkSize) + chunkSize;
        //cPos.z = (cPos.z * chunkSize) + chunkSize;
        for (int x = 0; x < chunkSize; x++) { //start at the the first block in the chunk (cPos - chunkSize), go to the last block in the chunk (cPos)
            for (int z = 0; z < chunkSize; z++) {
                int height = getGroundHeight(x, z);
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
        return 10;
    }

    @Override
    public int getGroundHeight(Vec2i loc) {
        return 10;
    }

    @Override
    public Vec3i getGroundHeight(Vec3i loc) {
        loc.y = 10;
        return loc;
    }
}
