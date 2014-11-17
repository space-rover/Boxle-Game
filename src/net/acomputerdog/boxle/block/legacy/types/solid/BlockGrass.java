package net.acomputerdog.boxle.block.legacy.types.solid;

import net.acomputerdog.boxle.block.legacy.BlockTex;
import net.acomputerdog.boxle.block.legacy.types.BlockSolid;
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
            tex.loadTopTex("tex/block/grass_top.png");
            tex.loadFrontTex("tex/block/grass_side.png");
            tex.setBackTex(tex.getFrontTex());
            tex.setLeftTex(tex.getFrontTex());
            tex.setRightTex(tex.getFrontTex());
            tex.loadBottomTex("tex/block/dirt.png");
        }
        return tex;
    }
}
