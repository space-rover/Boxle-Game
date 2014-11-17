package net.acomputerdog.boxle.block.sim.sim;

import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class SimResult {
    private final Stack stack;
    private final SimState endState;

    public SimResult(Stack stack, SimState endState) {
        this.stack = stack;
        this.endState = endState;
    }

    public Stack getStack() {
        return stack;
    }

    public SimState getEndState() {
        return endState;
    }
}
