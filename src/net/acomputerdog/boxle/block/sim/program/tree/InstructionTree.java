package net.acomputerdog.boxle.block.sim.program.tree;

import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.boxle.block.sim.program.instruction.InstructionStart;
import net.acomputerdog.core.tree.Tree;

public class InstructionTree extends Tree<Instruction> {
    private final InstructionBranch root = new InstructionBranch(this, new InstructionStart());

    @Override
    public InstructionBranch getRoot() {
        return root;
    }

    public InstructionBranch getStartInstruction() {
        return root;
    }
}
