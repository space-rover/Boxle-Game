package net.acomputerdog.boxle.block.legacy.types.solid;

import net.acomputerdog.boxle.block.legacy.BlockTex;
import net.acomputerdog.boxle.block.legacy.types.BlockSolid;
import net.acomputerdog.boxle.main.Boxle;

public class BlockGrassySteel extends BlockSolid {
    private BlockTex tex;

    public BlockGrassySteel(Boxle boxle) {
        super("grassy_steel", boxle);
    }

    @Override
    public BlockTex getTextures() {
        if (tex == null) {
            tex = new BlockTex(this);
            tex.loadTopTex("tex/block/grass_top.png");
            tex.loadFrontTex("tex/block/grassy_steel.png");
            tex.setBackTex(tex.getFrontTex());
            tex.setLeftTex(tex.getFrontTex());
            tex.setRightTex(tex.getFrontTex());
            tex.loadBottomTex("tex/block/steel.png");
        }
        return tex;
    }
}
