package net.acomputerdog.boxle.world.structure.block;

import net.acomputerdog.boxle.block.Block;
import net.acomputerdog.boxle.block.Blocks;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class SimpleBlockStorage implements BlockStorage {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];

    @Override
    public Block getBlock(int x, int y, int z) {
        Block block = blocks[x][y][z];
        if (block == null) {
            block = blocks[x][y][z] = Blocks.air;
        }
        return block;
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        if (block == null) {
            Boxle.instance().LOGGER_MAIN.logWarning("Attempted to set a null block, storing air instead!");
            block = Blocks.air;
        }
        blocks[x][y][z] = block;
    }
}