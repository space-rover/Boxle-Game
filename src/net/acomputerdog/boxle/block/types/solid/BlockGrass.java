package net.acomputerdog.boxle.block.types.solid;

import net.acomputerdog.boxle.block.BlockTex;
import net.acomputerdog.boxle.block.types.BlockSolid;
import net.acomputerdog.boxle.main.Boxle;

public class BlockGrass extends BlockSolid {
    private BlockTex tex;

    public BlockGrass(Boxle boxle) {
        super("grass", boxle);
    }

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
            //tex.loadTopTex("tex/block/grass_top.png");
            //tex.loadFrontTex("tex/block/grass_side.png");
            //tex.setBackTex(tex.getFrontTex());
            //tex.setLeftTex(tex.getFrontTex());
            //tex.setRightTex(tex.getFrontTex());
            //tex.loadBottomTex("tex/block/dirt.png");
        }
        return tex;
    }
}
