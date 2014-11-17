package net.acomputerdog.boxle.block.sim.program.tree;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.program.instruction.control.InstructionStart;
import net.acomputerdog.core.tree.Branch;
import net.acomputerdog.core.tree.Tree;

public class InstructionTree extends Tree<Instruction> {
    private final InstructionBranch root = new InstructionBranch(this, null, "root", new InstructionStart());

    @Override
    public Branch<Instruction> root() {
        return root;
    }

    public InstructionBranch getStartInstruction() {
        return root;
    }
}
