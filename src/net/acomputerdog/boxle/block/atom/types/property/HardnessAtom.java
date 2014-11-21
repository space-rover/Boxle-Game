package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;

public class HardnessAtom extends Atom {
    public HardnessAtom(String name) {
        super(null, "PROPERTY.HARDNESS", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_FLOAT);
        block.setHardness((Float) stack.pop().getObj());
    }
}
