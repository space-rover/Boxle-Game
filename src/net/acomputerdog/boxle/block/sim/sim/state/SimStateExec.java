package net.acomputerdog.boxle.block.sim.sim.state;

import net.acomputerdog.boxle.block.sim.sim.exec.SimException;

public class SimStateExec extends SimState {
    private final SimException exec;

    public SimStateExec(SimException exec) {
        super("STOP_EXCEPTION");
        this.exec = exec;
    }

    public SimException getExec() {
        return exec;
    }
}
