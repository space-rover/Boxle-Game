package net.acomputerdog.boxle.block.sim.program;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public abstract class Instruction {
    private final String id;

    public Instruction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void execute(Sim sim, Stack stack, Block block) throws SimException; //Block horribly backported
}
