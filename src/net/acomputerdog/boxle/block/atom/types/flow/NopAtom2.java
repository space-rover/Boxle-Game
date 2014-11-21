package net.acomputerdog.boxle.block.atom.types.flow;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class NopAtom2 extends Atom {
    public NopAtom2(String name) {
        super(null, "FLOW.NOP", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        //does nothing
    }
}
