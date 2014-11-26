package net.acomputerdog.boxle.block.sim.sim.exec;

import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.boxle.block.sim.sim.Sim;

public class InvalidStackDataException extends SimException {
    public InvalidStackDataException(Sim sim, Instruction instruction) {
        super(sim, instruction);
    }

    public InvalidStackDataException(Sim sim, Instruction instruction, String message) {
        super(sim, instruction, message);
    }

    public InvalidStackDataException(Sim sim, Instruction instruction, Exception parent) {
        super(sim, instruction, parent);
    }

    public InvalidStackDataException(Sim sim, Instruction instruction, String message, Exception parent) {
        super(sim, instruction, message, parent);
    }
}
