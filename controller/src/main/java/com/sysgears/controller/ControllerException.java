package com.sysgears.controller;

/**
 * ControllerException is subclass if ExecutorException.
 * Throws if something is wrong with connection with user.
 */
public class ControllerException extends RuntimeException {

    /**
     * Default message for ControllerException.
     */
    private static final String MESSEGE = "Connection is lost. ";

    /**
     * Constructs ControllerException without detail message.
     */
    public ControllerException() {
        super(MESSEGE);
    }

    /**
     * Constructs ControllerException with detail message.
     *
     * @param message the detail message
     */
    public ControllerException(final String message) {
        super(MESSEGE + message);
    }

    /**
     * Constructs ControllerException with detail message and specified cause.
     *
     * @param message the detail message
     * @param cause   the specified cause of ControllerException
     */
    public ControllerException(final String message, final Throwable cause) {
        super(MESSEGE + message, cause);
    }

    /**
     * Constructs ControllerException with specified cause.
     *
     * @param cause the specified cause of ControllerException
     */
    public ControllerException(final Throwable cause) {
        super(MESSEGE, cause);
    }
}
