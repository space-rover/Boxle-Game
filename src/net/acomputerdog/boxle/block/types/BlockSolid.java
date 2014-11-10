package net.acomputerdog.boxle.block.types;

import net.acomputerdog.boxle.main.Boxle;

/**
 * Superclass for blocks that are solid 1m cubes
 */
public abstract class BlockSolid extends BlockConfigurable {
    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected BlockSolid(String name, Boxle boxle) {
        super(name, boxle);
        super.setBlocksMovement(true);
        super.setTransparent(false);
        super.setLightReduction((byte) 255);
    }
}
