package net.acomputerdog.boxle.world.structure;

import net.acomputerdog.boxle.block.Block;

public interface BlockStorage {
    public void setBlock(int x, int y, int z, Block block);

    public Block getBlock(int x, int y, int z);

    public void clear(Block block);
}
