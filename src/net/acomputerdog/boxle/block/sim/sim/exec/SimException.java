package net.acomputerdog.boxle.block.sim.sim.exec;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;

public class SimException extends Exception {
    private final Sim sim;
    private final Instruction instruction;

    public SimException(Sim sim, Instruction instruction) {
        super();
        this.sim = sim;
        this.instruction = instruction;
    }

    public SimException(Sim sim, Instruction instruction, String message) {
        super(message);
        this.sim = sim;
        this.instruction = instruction;
    }

    protected SimException(Sim sim, Instruction instruction, Throwable parent) {
        super(parent);
        this.sim = sim;
        this.instruction = instruction;
    }

    protected SimException(Sim sim, Instruction instruction, String message, Throwable parent) {
        super(message, parent);
        this.sim = sim;
        this.instruction = instruction;
    }

    public Sim getSim() {
        return sim;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
