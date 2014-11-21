package net.acomputerdog.boxle.block.atom.types.value;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.InvalidStackDataException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class DupeAtom2 extends Atom {
    public DupeAtom2(String name) {
        super(null, "MATH.DUPE", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        if (stack.isEmpty()) {
            throw new InvalidStackDataException(sim, this, "Invalid stack items!");
        }
        StackItem item = stack.peek();
        stack.push(new StackItem(item.getObj(), item.getType()));
    }
}
