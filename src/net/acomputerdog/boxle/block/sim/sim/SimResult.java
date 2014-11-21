package net.acomputerdog.boxle.block.sim.sim;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class SimResult {
    private final Stack stack;
    private final SimState endState;
    private final Block block;

    public SimResult(Stack stack, SimState endState, Block block) {
        this.stack = stack;
        this.endState = endState;
        this.block = block;
    }

    public Stack getStack() {
        return stack;
    }

    public SimState getEndState() {
        return endState;
    }

    public Block getBlock() {
        return block;
    }
}
