package net.acomputerdog.boxle.block.sim.program;

import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;

import java.util.HashMap;
import java.util.Map;

public class Program {
    private String name;

    private String id;

    private final InstructionTree instructions = new InstructionTree();

    private final Map<String, String> variables = new HashMap<>();

    public Program() {
        this("");
    }

    public Program(String id) {
        this(id, "");
    }

    public Program(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public InstructionTree getInstructions() {
        return instructions;
    }

    public InstructionBranch createBranch(InstructionBranch branch, Instruction... instructions) {
        for (Instruction ins : instructions) {
            branch = branch.addOutput(ins);
        }
        return branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVariable(String var, String value) {
        variables.put(var, value);
    }

    public String getVariable(String var) {
        return variables.get(var);
    }
}
