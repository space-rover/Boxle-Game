package net.acomputerdog.boxle.block.types.solid;

import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.block.types.BlockSolid;
import net.acomputerdog.boxle.main.Boxle;

/**
 * A dirt block
 */
public class BlockDirt extends BlockSolid {
    private BlockTex tex;
    public BlockDirt(Boxle boxle) {
        super("dirt", boxle);
    }

    /*
    @Override
    public BlockTex getTextures() {
        if (tex == null) {
            tex = new BlockTex(this);
            tex.loadTopTex("tex/debug/top.png");
            tex.loadBottomTex("tex/debug/bottom.png");
            tex.loadLeftTex("tex/debug/left.png");
            tex.loadRightTex("tex/debug/right.png");
            tex.loadFrontTex("tex/debug/front.png");
            tex.loadBackTex("tex/debug/back.png");
        }
        return tex;
    }
    */
}
