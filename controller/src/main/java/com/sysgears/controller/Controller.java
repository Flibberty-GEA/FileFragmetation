package com.sysgears.controller;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Interface for connect with user.
 */
public interface Controller {

    /**
     * Gets message from user.
     *
     * @return string message from user to application
     * @throws ControllerException if an controller error occurs
     */
    String getMessage();

    /**
     * Sends message to user.
     *
     * @param message string message from application to user
     * @throws ControllerException if an controller error occurs
     */
    void sendMessage(String message) ;

    BufferedWriter getWriter();

    /**
     * Check is Controller open.
     *
     * @return true if controller is open
     */
    boolean isOpen();

    /**
     * Closes StreamController.
     *
     * @throws ControllerException if an controller error occurs
     */
    void closeController() ;

}
