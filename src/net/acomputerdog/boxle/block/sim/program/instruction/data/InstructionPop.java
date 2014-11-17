package net.acomputerdog.boxle.block.sim.program.instruction.data;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class InstructionPop extends Instruction {
    public InstructionPop() {
        super("POP");
    }

    @Override
    public void execute(Sim sim, Stack stack) throws SimException {
        stack.pop();
    }
}
