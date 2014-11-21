package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;

public class CollidableAtom extends Atom {
    public CollidableAtom(String name) {
        super(null, "PROPERTY.COLLIDABLE", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_BOOLEAN);
        block.setCollidable((Boolean) stack.pop().getObj());
    }
}
