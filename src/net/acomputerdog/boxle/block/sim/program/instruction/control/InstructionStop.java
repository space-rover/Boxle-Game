package net.acomputerdog.boxle.block.sim.program.instruction.control;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class InstructionStop extends Instruction {
    public InstructionStop() {
        super("STOP");
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        sim.stopCurrBranch();
    }
}
