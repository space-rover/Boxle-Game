package net.acomputerdog.boxle.block.sim.sim.exec;

import net.acomputerdog.boxle.exception.BoxleException;
import net.acomputerdog.boxle.main.Boxle;

public class IllegalSimFormat extends BoxleException {
    private final String line;

    public IllegalSimFormat(Boxle boxle, String line) {
        super(boxle);
        this.line = line;
    }

    public IllegalSimFormat(String message, Boxle boxle, String line) {
        super(message, boxle);
        this.line = line;
    }

    public IllegalSimFormat(String message, Throwable cause, Boxle boxle, String line) {
        super(message, cause, boxle);
        this.line = line;
    }

    public String getLine() {
        return line;
    }
}
