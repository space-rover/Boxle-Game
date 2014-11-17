package net.acomputerdog.boxle.block.legacy.types;

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
    public BlockSolid(String name, Boxle boxle) {
        super(name, boxle);
        super.setCollidable(true);
        super.setTransparent(false);
        super.setLightReduction((byte) 255);
    }
}
