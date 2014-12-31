package net.acomputerdog.boxle.block.sim.program.tree;

import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.core.tree.Branch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstructionBranch extends Branch<Instruction> {
    private final Instruction instruction;
    private final InstructionTree tree;

    public InstructionBranch(InstructionTree tree, InstructionBranch parent, Instruction instruction) {
        super(tree, parent);
        this.instruction = instruction;
        this.tree = tree;
    }

    protected InstructionBranch(InstructionTree tree, Instruction instruction) {
        super(tree);
        this.instruction = instruction;
        this.tree = tree;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public List<InstructionBranch> getOutputs() {
        List<InstructionBranch> branches = new ArrayList<InstructionBranch>();
        for (Branch<Instruction> branch : super.getBranches()) {
            branches.add((InstructionBranch) branch);
        }
        return Collections.unmodifiableList(branches);
    }

    @Override
    public void addBranch(Branch<Instruction> branch) {
        if (branch != null && branch instanceof InstructionBranch) {
            super.addBranch(branch);
        }
    }

    public InstructionBranch addOutput(Instruction ins) {
        return addOutput(new InstructionBranch(tree, this, ins));
    }

    public InstructionBranch addOutput(InstructionBranch ins) {
        //System.out.println("Adding " + ins.getInstruction().getId() + " to " + instruction.getId());
        addBranch(ins);
        return ins;
    }
}
