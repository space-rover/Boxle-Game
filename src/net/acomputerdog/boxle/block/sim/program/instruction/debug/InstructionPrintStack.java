package net.acomputerdog.boxle.block.sim.program.instruction.debug;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class InstructionPrintStack extends Instruction {
    public InstructionPrintStack() {
        super("DEBUG_PRINT_STACK");
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        System.out.println(stack.toString());
    }
}
