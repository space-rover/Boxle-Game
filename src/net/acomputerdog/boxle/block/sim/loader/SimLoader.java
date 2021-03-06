package net.acomputerdog.boxle.block.sim.loader;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.atom.Atoms;
import net.acomputerdog.boxle.block.atom.types.value.PushStringAtom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Program;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.SimResult;
import net.acomputerdog.boxle.block.sim.sim.exec.IllegalSimFormat;
import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.core.java.Patterns;

import java.io.*;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class SimLoader {

    private static Block loadInternalSim(String name) {
        try {
            Block block = loadSim(SimLoader.class.getResourceAsStream("/sim/block/" + name + ".sim"));
            Sim.LOGGER.logDetail("Loaded block from internal sim: " + block.getId());
            return block;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Exception creating block!");
        }
    }

    public static void loadExternalSims() {
        File userDir = new File("./sims/user/");
        if (!(userDir.isDirectory() || userDir.mkdirs())) {
            Sim.LOGGER.logWarning("Unable to create user sims directory!");
        }
        File saveDir = new File("./sims/save/");
        if (!(saveDir.isDirectory() || saveDir.mkdirs())) {
            Sim.LOGGER.logWarning("Unable to create save sims directory!");
        }
        int loadedFiles = 0;
        loadedFiles += loadSimsInDir(userDir);
        loadedFiles += loadSimsInDir(saveDir);
        Sim.LOGGER.logDetail("Loaded " + loadedFiles + " external sims.");

    }

    private static int loadSimsInDir(File dir) {
        File[] files = dir.listFiles();
        int loadedFiles = 0;
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".sim")) {
                    try {
                        Block block = loadSim(new FileInputStream(file));
                        Sim.LOGGER.logDetail("Loaded block from external sim: " + block.getId());
                        loadedFiles++;
                    } catch (IOException e) {
                        Sim.LOGGER.logWarning("Unable to read sim: " + file.getPath());
                        e.printStackTrace();
                    }
                }
            }
        }
        return loadedFiles;
    }

    public static Block loadSim(InputStream in) {
        BufferedReader reader = null;
        try {
            Program program = new Program();
            reader = new BufferedReader(new InputStreamReader(in));
            InstructionTree tree = program.getInstructions();
            Deque<InstructionBranch> branches = new LinkedBlockingDeque<>();
            branches.add(tree.getStartInstruction());
            while (reader.ready()) {
                String line = reader.readLine();
                parseLine(program, line, branches);
            }
            //System.out.println(program.toString());
            reader.close();
            String blockId = program.getVariable("$id");
            String blockName = program.getVariable("$name");
            //System.out.println(blockId + " / " + blockName);
            if (blockId != null) program.setId(blockId);
            if (blockName != null) program.setName(blockName);
            Sim sim = new Sim(program);
            SimResult result = sim.startSim();
            SimState endState = result.getEndState();
            if (endState != SimState.FINISHED) {
                throw new RuntimeException("Sim finished unexpectedly with state " + endState.getState());
            }
            return result.getBlock();
        } catch (Exception e) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {}
            throw new RuntimeException("Exception loading sim!", e);
        }
    }

    private static void parseLine(Program program, String line, Deque<InstructionBranch> branches) throws IllegalSimFormat {
        if (line == null) return;
        //System.out.println("Parsing line: " + line);
        String origLine = line;
        line = line.trim(); //get rid of whitespace
        if (line.isEmpty() || line.startsWith("#")) return;
        line = line.split(Patterns.NUMBERSIGN)[0].trim(); //get rid of trailing comments
        if (line.startsWith("$")) {
            String[] variable = line.split(Patterns.EQUALS);
            if (variable.length != 2) {
                throw new IllegalSimFormat("Incorrectly formatted variable!", Boxle.instance(), origLine);
            }
            program.setVariable(variable[0], variable[1]);
        } else if (line.endsWith(";")) {
            int semiLoc = line.indexOf(';');
            String atomName = line.substring(0, semiLoc);
            int numSemis = line.substring(semiLoc).length();
            branches.addFirst(addAtom(atomName, branches, origLine));
            if (numSemis > branches.size()) {
                throw new IllegalSimFormat("Too many semicolons!", Boxle.instance(), origLine);
            }
            for (int count = 0; count < numSemis; count++) {
                branches.remove();
            }
        } else if (line.endsWith(":")) {
            branches.addFirst(addAtom(line.substring(0, line.indexOf(':')), branches, origLine));
        } else {
            throw new IllegalSimFormat("Incorrectly formatted line!", Boxle.instance(), origLine);
        }
    }

    private static InstructionBranch addAtom(String name, Queue<InstructionBranch> branches, String origLine) throws IllegalSimFormat {
        Atom atom = Atoms.ATOMS.getFromId(name);
        if (atom == null) {
            if (name.startsWith("VALUE.PUSH_STRING_")) {
                String str = name.substring(18);
                atom = Atoms.ATOMS.register(new PushStringAtom("Push string " + str, str));
            } else {
                throw new IllegalSimFormat("Non-existant atom: " + name, Boxle.instance(), origLine);
            }
        }
        return branches.peek().addOutput(atom);
    }
}
