package net.acomputerdog.boxle.world.structure.block;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.legacy.Blocks;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;

@Deprecated
public class SingleArrayBlockStorage implements BlockStorage {
    private static final int chunkSize = Chunk.CHUNK_SIZE;
    private static final int chunkSlice = chunkSize * chunkSize;

    private Block[] blocks = new Block[Chunk.CHUNK_VOLUME];

    private int calcIndex(int x, int y, int z) {
        return (chunkSlice * x) + (chunkSize * y) + z;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        int index = calcIndex(x, y, z);
        Block block = blocks[index];
        if (block == null) {
            block = blocks[index] = Blocks.air;
        }
        return block;
    }

    @Override
    public void clear(Block block) {

    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        int index = calcIndex(x, y, z);
        blocks[index] = block;
    }
}
