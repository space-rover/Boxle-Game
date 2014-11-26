package net.acomputerdog.boxle.block.sim.program.instruction;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;

public class InstructionStart extends Instruction {
    public InstructionStart() {
        super("START");
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {

    }
}
