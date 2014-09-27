package net.acomputerdog.boxle.block.types.nonsolid;

import net.acomputerdog.boxle.block.types.BlockNonSolid;

/**
 * Air block, used to represent empty space
 */
public class BlockAir extends BlockNonSolid {
    public BlockAir() {
        super("air");
        super.setBreakable(false);
        super.setResistance(0f);
        super.setExplosionResistance(0f);
        super.setStrength(9999f);
    }
}
