package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;
import net.acomputerdog.boxle.math.aabb.AABBF;

public class BoundsAtom extends Atom {
    public BoundsAtom(String name) {
        super(null, "PROPERTY.BOUNDS", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_FLOAT, StackItem.TYPE_FLOAT, StackItem.TYPE_FLOAT, StackItem.TYPE_FLOAT, StackItem.TYPE_FLOAT, StackItem.TYPE_FLOAT);
        float z2 = (Float) stack.pop().getObj();
        float y2 = (Float) stack.pop().getObj();
        float x2 = (Float) stack.pop().getObj();
        float z1 = (Float) stack.pop().getObj();
        float y1 = (Float) stack.pop().getObj();
        float x1 = (Float) stack.pop().getObj();
        block.setBounds(new AABBF(x1, y1, z1, x2, y2, z2));
    }
}
