package net.acomputerdog.boxle.block.atom.types.property;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;

public class ExplosionResistanceAtom extends Atom {
    public ExplosionResistanceAtom(String name) {
        super(null, "PROPERTY.EXPLOSION_RESISTANCE", name);
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_FLOAT);
        block.setExplosionResistance((Float) stack.pop().getObj());
    }
}
