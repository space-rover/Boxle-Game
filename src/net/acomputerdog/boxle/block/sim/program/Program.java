package net.acomputerdog.boxle.block.sim.program;

import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;

public class Program {
    private final InstructionTree instructions = new InstructionTree();

    public InstructionTree getInstructions() {
        return instructions;
    }

    public InstructionBranch createBranch(InstructionBranch branch, Instruction... instructions) {
        for (Instruction ins : instructions) {
            branch = branch.addOutput(ins);
        }
        return branch;
    }
}
