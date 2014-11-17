package net.acomputerdog.boxle.block.sim.program.instruction.math;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.InvalidStackDataException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;

public class InstructionAdd extends Instruction {
    public InstructionAdd() {
        super("ADD");
    }

    @Override
    public void execute(Sim sim, Stack stack) throws SimException {
        if (stack.size() < 2) {
            throw new InvalidStackDataException(sim, this, "Not enough items in stack!");
        }
        StackItem firstItem = stack.pop();
        StackItem secondItem = stack.pop();
        if (!firstItem.isType(StackItem.TYPE_INT) || !secondItem.isType(StackItem.TYPE_INT)) {
            throw new InvalidStackDataException(sim, this, "Incorrect data type in stack!");
        }
        stack.push(add(firstItem, secondItem));
    }

    private StackItem add(StackItem item1, StackItem item2) {
        int val1 = (Integer) item1.getObj();
        int val2 = (Integer) item2.getObj();
        return new StackItem(val1 + val2, StackItem.TYPE_INT);
    }
}
