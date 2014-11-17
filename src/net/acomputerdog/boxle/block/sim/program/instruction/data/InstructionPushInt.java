package net.acomputerdog.boxle.block.sim.program.instruction.data;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class InstructionPushInt extends Instruction {
    private final int amount;

    public InstructionPushInt(int amount) {
        super("PUSH_INT");
        this.amount = amount;
    }

    @Override
    public void execute(Sim sim, Stack stack) throws SimException {
        stack.push(new StackItem(amount, StackItem.TYPE_INT));
    }
}
