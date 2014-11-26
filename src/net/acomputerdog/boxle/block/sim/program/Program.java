package net.acomputerdog.boxle.block.sim.program;

import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;

import java.io.*;
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
        setVariable("$name", name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setVariable("$id", id);
    }

    public void setVariable(String var, String value) {
        variables.put(var, value);
    }

    public String getVariable(String var) {
        return variables.get(var);
    }


    public void saveToScript(File scriptPath) throws IOException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(scriptPath));
            for (String str : variables.keySet()) {
                writer.write(str);
                writer.write("=");
                writer.write(variables.get(str));
                writer.write("\n");
            }
            writer.write("\n");
            writeBranch(writer, instructions.root(), 0, 0);
            writer.close();
        } catch (IOException e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private void writeBranch(Writer out, InstructionBranch branch, int initialDepth, int currDepth) throws IOException {
        for (InstructionBranch currBranch : branch.getOutputs()) {
            out.write(currBranch.getInstruction().getId());
            if (currBranch.getOutputs().isEmpty()) {
                for (int count = 0; count <= currDepth - initialDepth; count++) {
                    out.write(";");
                }
                out.write("\n");
            } else {
                out.write(":");
                out.write("\n");
                writeBranch(out, currBranch, initialDepth, currDepth + 1);
            }
        }
    }
}
