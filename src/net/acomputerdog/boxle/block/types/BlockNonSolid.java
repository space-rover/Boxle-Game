package net.acomputerdog.boxle.block.types;

/**
 * Superclass for non-solid blocks
 */
public abstract class BlockNonSolid extends BlockConfigurable {
    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected BlockNonSolid(String name) {
        super(name);
        super.setBlocksMovement(false);
        super.setTransparent(true);
        super.setLightReduction((byte) 0);
    }
}
