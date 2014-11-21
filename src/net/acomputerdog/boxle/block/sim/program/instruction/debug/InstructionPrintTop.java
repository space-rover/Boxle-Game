package net.acomputerdog.boxle.block.sim.program.instruction.debug;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class InstructionPrintTop extends Instruction {
    private final boolean pop;

    public InstructionPrintTop(boolean doPop) {
        super("DEBUG_PRINT_TOP");
        this.pop = doPop;
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        System.out.println((pop ? stack.pop() : stack.peek()).getObj());

    }
}
