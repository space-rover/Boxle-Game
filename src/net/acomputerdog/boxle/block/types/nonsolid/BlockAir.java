package net.acomputerdog.boxle.block.types.nonsolid;

import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.block.types.BlockNonSolid;
import net.acomputerdog.boxle.main.Boxle;

/**
 * Air block, used to represent empty space
 */
public class BlockAir extends BlockNonSolid {
    public BlockAir(Boxle boxle) {
        super("air", boxle);
        super.setBreakable(false);
        super.setResistance(0f);
        super.setExplosionResistance(0f);
        super.setStrength(9999f);
        super.setRenderable(false);
    }

    @Override
    public BlockTex getTextures() {
        Boxle.instance().LOGGER_MAIN.logWarning("Attempted to get texture for an air block!");
        return super.getTextures();
    }
}
