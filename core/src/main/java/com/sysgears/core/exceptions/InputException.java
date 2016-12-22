package com.sysgears.core.exceptions;

/**
 * InputException is a subclass of ExecutorException.
 * Throws if something is wrong with input arguments.
 */
public class InputException extends RuntimeException {

    /**
     * Default message of InputException.
     */
    private static final String MES = "Input arguments are incorrect. ";

    /**
     * Constructs InputException without a detail message.
     */
    public InputException() {
        super(MES);
    }

    /**
     * Constructs InputException a with detail message.
     *
     * @param message the detail message.
     */
    public InputException(final String message) {
        super(MES + message);
    }

    /**
     * Constructs InputException with a detail message and a specified cause.
     *
     * @param message the detail message.
     * @param cause   the specified cause of InputException.
     */
    public InputException(final String message, final Throwable cause) {
        super(MES + message, cause);
    }

    /**
     * Constructs InputException with a specified cause.
     *
     * @param cause the specified cause of InputException.
     */
    public InputException(final Throwable cause) {
        super(MES, cause);
    }
}
