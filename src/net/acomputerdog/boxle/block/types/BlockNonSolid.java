package net.acomputerdog.boxle.block.types;

import net.acomputerdog.boxle.block.Block;

/**
 * Superclass for non-solid blocks
 */
public abstract class BlockNonSolid extends Block {
    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected BlockNonSolid(String name) {
        super(name);
    }

    @Override
    public boolean blocksMovement(byte data) {
        return false;
    }

    @Override
    public boolean isTransparent(byte data) {
        return true;
    }

    @Override
    public byte getLightReduction(byte data) {
        return 0;
    }
}
