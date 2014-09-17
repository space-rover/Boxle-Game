package net.acomputerdog.boxle.util.execption;

import net.acomputerdog.boxle.main.Boxle;

/**
 * A boxle-specific checked exception.
 */
public class BoxleException extends Exception {
    /**
     * The boxle instance that this exception occurred under.
     */
    private final Boxle boxle;

    public BoxleException(Boxle boxle) {
        super();
        this.boxle = boxle;
    }

    public BoxleException(String message, Boxle boxle) {
        super(message);
        this.boxle = boxle;
    }

    public BoxleException(String message, Throwable cause, Boxle boxle) {
        super(message, cause);
        this.boxle = boxle;
    }

    public BoxleException(Throwable cause, Boxle boxle) {
        super(cause);
        this.boxle = boxle;
    }

    protected BoxleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Boxle boxle) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.boxle = boxle;
    }

    public Boxle getBoxle() {
        return boxle;
    }
}
