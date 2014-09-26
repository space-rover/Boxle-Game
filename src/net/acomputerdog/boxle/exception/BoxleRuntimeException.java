package net.acomputerdog.boxle.exception;

import net.acomputerdog.boxle.main.Boxle;

/**
 * A boxle-specific runtime exception
 */
public class BoxleRuntimeException extends RuntimeException {
    /**
     * The boxle instance that this exception occurred under.
     */
    private final Boxle boxle;

    public BoxleRuntimeException(Boxle boxle) {
        super();
        this.boxle = boxle;
    }

    public BoxleRuntimeException(String message, Boxle boxle) {
        super(message);
        this.boxle = boxle;
    }

    public BoxleRuntimeException(String message, Throwable cause, Boxle boxle) {
        super(message, cause);
        this.boxle = boxle;
    }

    public BoxleRuntimeException(Throwable cause, Boxle boxle) {
        super(cause);
        this.boxle = boxle;
    }

    protected BoxleRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Boxle boxle) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.boxle = boxle;
    }

    public Boxle getBoxle() {
        return boxle;
    }
}
