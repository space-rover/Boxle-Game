package net.acomputerdog.boxle.block.atom.types.value;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class PushBooleanAtom extends Atom {
    private final float val;

    public PushBooleanAtom(float val) {
        super("VALUE.PUSH_FLOAT");
        this.val = val;
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        stack.push(new StackItem(val, StackItem.TYPE_FLOAT));
    }
}
