package net.acomputerdog.boxle.block.types.nonsolid;

import net.acomputerdog.boxle.block.types.BlockNonSolid;

/**
 * Air block, used to represent empty space
 */
public class BlockAir extends BlockNonSolid {
    public BlockAir() {
        super("air");
    }

    @Override
    public boolean canBeDestroyed(byte data) {
        return false;
    }

    @Override
    public float getResistance(byte data) {
        return 0f;
    }

    @Override
    public float getExplosionResistance(byte data) {
        return 0f;
    }

    @Override
    public float getStrength(byte data) {
        return 9999f;
    }
}
