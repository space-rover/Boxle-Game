package net.acomputerdog.boxle.world.structure.block;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.registry.Blocks;
import net.acomputerdog.boxle.world.Chunk;
import net.acomputerdog.boxle.world.structure.BlockStorage;

public class SimpleBlockStorage implements BlockStorage {
    private static final int chunkSize = Chunk.CHUNK_SIZE;

    private final Chunk chunk;

    private Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];

    private Block clearBlock = Blocks.air;

    public SimpleBlockStorage(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        Block block = blocks[x][y][z];
        if (block == null) {
            block = blocks[x][y][z] = clearBlock;
        }
        return block;
    }

    @Override
    public void clear(Block block) {
        clearBlock = (block == null) ? Blocks.air : block;
        blocks = new Block[chunkSize][chunkSize][chunkSize];
        chunk.setNeedsRebuild(true);
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        if (block == null) {
            chunk.getWorld().getLogger().logWarning("Attempted to set a null block, storing air instead!");
            block = Blocks.air;
        }
        blocks[x][y][z] = block;
        chunk.setNeedsRebuild(true);
    }

    public Chunk getChunk() {
        return chunk;
    }
}
