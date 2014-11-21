package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class RenderableAtom extends Atom {
    public RenderableAtom() {
        super("SET_BREAKABLE");
    }

    @Override
    public void execute(Sim sim, Stack stack) throws SimException {

    }
}
