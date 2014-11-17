package net.acomputerdog.boxle.block.atom.types.value;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.InvalidStackDataException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;

public class MULTAtom extends Atom {

    public MULTAtom() {
        super("MULT", "MULT");
    }

    @Override
    public void execute(Sim sim, Stack stack) throws SimException {
        if (!SimUtils.verifyStack(stack, StackItem.TYPE_INT, StackItem.TYPE_INT)) {
            throw new InvalidStackDataException(sim, this, "Invalid stack items!");
        }
        int item1 = (Integer) (stack.pop().getObj());
        int item2 = (Integer) (stack.pop().getObj());
        stack.push(new StackItem(item1 * item2, StackItem.TYPE_INT));
    }
}
