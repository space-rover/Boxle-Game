package net.acomputerdog.boxle.block.atom.types.value;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class PushStringAtom extends Atom {
    private final String str;

    public PushStringAtom(String name, String str) {
        super(null, "VALUE.PUSH_STRING_" + str, name);
        this.str = str;
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        stack.push(new StackItem(str, StackItem.TYPE_STRING));
    }
}
