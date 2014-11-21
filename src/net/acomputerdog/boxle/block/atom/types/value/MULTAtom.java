package net.acomputerdog.boxle.block.atom.types.value;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;

public class MultAtom extends Atom {

    public MultAtom(String name) {
        super(null, "MATH.MULT", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_INT, StackItem.TYPE_INT);
        int item1 = (Integer) (stack.pop().getObj());
        int item2 = (Integer) (stack.pop().getObj());
        stack.push(new StackItem(item1 * item2, StackItem.TYPE_INT));
    }
}
