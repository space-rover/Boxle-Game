package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.BlockTex;
import net.acomputerdog.boxle.block.util.SimUtils;

public class TexAtom extends Atom {
    public TexAtom(String name) {
        super(null, "PROPERTY.TEX", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_STRING, StackItem.TYPE_STRING, StackItem.TYPE_STRING, StackItem.TYPE_STRING, StackItem.TYPE_STRING, StackItem.TYPE_STRING);

        String bottom = (String) stack.pop().getObj();
        String right = (String) stack.pop().getObj();
        String left = (String) stack.pop().getObj();
        String back = (String) stack.pop().getObj();
        String front = (String) stack.pop().getObj();
        String top = (String) stack.pop().getObj();
        //TODO: if textures are the same do not reload each time.

        BlockTex tex = new BlockTex(block);
        tex.loadBottomTex(bottom);
        tex.loadRightTex(right);
        tex.loadLeftTex(left);
        tex.loadBackTex(back);
        tex.loadFrontTex(front);
        tex.loadTopTex(top);
        block.setTextures(tex);
    }
}
