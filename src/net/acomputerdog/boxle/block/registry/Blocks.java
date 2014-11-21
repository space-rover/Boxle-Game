package net.acomputerdog.boxle.block.registry;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.atom.types.value.PushStringAtom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.program.Program;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionBranch;
import net.acomputerdog.boxle.block.sim.program.tree.InstructionTree;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.SimResult;
import net.acomputerdog.boxle.block.sim.sim.state.SimState;
import net.acomputerdog.boxle.block.util.IllegalSimFormat;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.core.java.Patterns;

import java.io.*;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Blocks {
    public static final Registry<Block> BLOCKS = new Registry<>();

    static {
        //loadInternalSims();
    }

    public static final Block air = createAirBlock();
    public static final Block steel = loadInternalBlock("steel");
    public static final Block grassySteel = loadInternalBlock("grassy_steel");

    private static Block createAirBlock() {
        Block block = new Block("air", "Air");
        block.setBreakable(false);
        block.setResistance(0f);
        block.setExplosionResistance(0f);
        block.setStrength(9999f);
        block.setRenderable(false);
        block.setCollidable(false);
        block.setTransparent(true);
        block.setLightReduction((byte) 0);
        BLOCKS.register(block);
        return block;
    }

    private static Block loadInternalBlock(String name) {
        try {
            return loadSim(Blocks.class.getResourceAsStream("/sim/block/" + name + ".sim"));
        } catch (Throwable t) {
            //System.err.println("Unable to create block: " + name + " at " + "sim/block/" + name + ".sim");
            t.printStackTrace();
            throw new RuntimeException("Excpetion creating block!");
        }
    }

    private static void loadInternalSims() {
        ZipFile jar = null;
        try {
            File jarFile = new File(Blocks.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            jar = new ZipFile(jarFile.getAbsoluteFile());
            Enumeration<? extends ZipEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("sim/")) {
                    try {
                        loadSim(jar.getInputStream(entry));
                    } catch (Exception e) {
                        Boxle.instance().LOGGER_MAIN.logError("Unable to load sim script: " + entry.getName(), e);
                    }
                } else {
                    Boxle.instance().LOGGER_MAIN.logDetail("Skipping entry: " + entry.getName());
                }
            }
        } catch (Exception e) {
            Boxle.instance().LOGGER_MAIN.logError("Unable to load internal sim scripts!", e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private static Block loadSim(InputStream in) {
        BufferedReader reader = null;
        try {
            Program program = new Program();
            reader = new BufferedReader(new InputStreamReader(in));
            InstructionTree tree = program.getInstructions();
            Queue<InstructionBranch> branches = new LinkedBlockingQueue<>();
            branches.add(tree.getStartInstruction());
            while (reader.ready()) {
                String line = reader.readLine();
                parseLine(program, line, branches);
            }
            reader.close();
            String blockId = program.getVariable("$id");
            String blockName = program.getVariable("$name");
            if (blockId != null) program.setId(blockId);
            if (blockName != null) program.setName(blockName);
            Sim sim = new Sim(program);
            SimResult result = sim.startSim();
            SimState endState = result.getEndState();
            if (endState != SimState.FINISHED) {
                throw new RuntimeException("Sim finished unexpectedly with state " + endState.getState());
            }
            Block block = result.getBlock();
            BLOCKS.register(block);
            Boxle.instance().LOGGER_MAIN.logDetail("Loaded block: " + result.getBlock().getId());
            return block;
        } catch (Exception e) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {}
            throw new RuntimeException("Exception loading sim!", e);
        }
    }

    private static void parseLine(Program program, String line, Queue<InstructionBranch> branches) throws IllegalSimFormat {
        if (line == null) return;
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
            branches.add(addAtom(atomName, branches, origLine));
            if (numSemis > branches.size()) {
                throw new IllegalSimFormat("Too many semicolons!", Boxle.instance(), origLine);
            }
            for (int count = 0; count < numSemis; count++) {
                branches.remove();
            }
        } else if (line.endsWith(":")) {
            branches.add(addAtom(line.substring(0, line.indexOf(':')), branches, origLine));
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
