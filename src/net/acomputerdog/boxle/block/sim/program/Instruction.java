package net.acomputerdog.boxle.block.sim.program;

import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public abstract class Instruction {
    private final String name;

    public Instruction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(Sim sim, Stack stack) throws SimException;
}
