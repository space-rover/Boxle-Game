package net.acomputerdog.boxle.block.sim.sim.exec;

import net.acomputerdog.boxle.block.sim.program.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;

public class SimExceptionUnknown extends SimException {
    private final Throwable source;

    public SimExceptionUnknown(Sim sim, Instruction instruction, Throwable source) {
        super(sim, instruction, source);
        this.source = source;
    }

    public SimExceptionUnknown(Sim sim, Instruction instruction, String message, Throwable source) {
        super(sim, instruction, message, source);
        this.source = source;
    }

    public Throwable getSource() {
        return source;
    }
}
