package net.acomputerdog.boxle.block.types;

import net.acomputerdog.boxle.block.Block;

/**
 * Superclass for blocks that are solid 1m cubes
 */
public abstract class BlockSolid extends Block {
    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected BlockSolid(String name) {
        super(name);
    }

    @Override
    public boolean blocksMovement(byte data) {
        return true;
    }

    @Override
    public boolean isTransparent(byte data) {
        return false;
    }

    @Override
    public byte getLightReduction(byte data) {
        return (byte) 255;
    }
}
