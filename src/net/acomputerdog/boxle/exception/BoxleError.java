package net.acomputerdog.boxle.exception;

import net.acomputerdog.boxle.main.Boxle;

public class BoxleError extends Error {
    /**
     * The boxle instance that this exception occurred under.
     */
    private final Boxle boxle;

    public BoxleError(Boxle boxle) {
        super();
        this.boxle = boxle;
    }

    public BoxleError(String message, Boxle boxle) {
        super(message);
        this.boxle = boxle;
    }

    public BoxleError(String message, Throwable cause, Boxle boxle) {
        super(message, cause);
        this.boxle = boxle;
    }

    public BoxleError(Throwable cause, Boxle boxle) {
        super(cause);
        this.boxle = boxle;
    }

    protected BoxleError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Boxle boxle) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.boxle = boxle;
    }

    public Boxle getBoxle() {
        return boxle;
    }
}
