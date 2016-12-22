package com.sysgears.controller;

import java.io.BufferedWriter;

/**
 * Interface for connect with user.
 */
public interface Controller {

    /**
     * Gets message from user.
     *
     * @return string message from user to application.
     * @throws ControllerException if an controller error occurs.
     */
    String getMessage() throws ControllerException;

    /**
     * Sends message to user.
     *
     * @param message string message from application to user.
     * @throws ControllerException if an controller error occurs.
     */
    void sendMessage(String message) throws ControllerException;

    BufferedWriter getWriter();

}
