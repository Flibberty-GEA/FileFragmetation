package com.sysgears.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Class for contact with user.
 *
 * @author Yevgen Goliuk
 */
public class StreamController implements Controller {
    public static final Logger log = LogManager.getLogger(StreamController.class);

    private final BufferedReader reader;
    private final BufferedWriter writer;
    private boolean isOpen;

    /**
     * Initializes buffered reader and writer from input and output stream.
     *
     * @param input  source of input data
     * @param output destination of output data
     */
    public StreamController(final InputStream input, final OutputStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.writer = new BufferedWriter(new OutputStreamWriter(output));
        this.isOpen = true;
    }

    @Override
    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     * Gets message from user.
     *
     * @return string message from user to application
     * @throws ControllerException if an controller error occurs
     */
    @Override
    public String getMessage() throws ControllerException {
        String message;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            log.error("Reading error.", e);
            throw new ControllerException("Could not read users message. ", e);
        }
        log.info("Get message from user. Message: \"" + message + "\".");
        return message;
    }

    /**
     * Sends message to user.
     *
     * @param message string message from application to user
     * @throws ControllerException if an controller error occurs
     */
    @Override
    public void sendMessage(final String message) throws ControllerException {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
            log.info("Send message to user. Message: \"" + message + "\".");
        } catch (IOException e) {
            log.error("Writing error.", e);
            throw new ControllerException("Could not write message to user. ", e);
        }
    }

    /**
     * Check is StreamController open.
     *
     * @return true if controller is open
     */
    @Override
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Closes StreamController.
     *
     * @throws ControllerException if an controller error occurs
     */
    @Override
    public void closeController() throws ControllerException {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            log.error("Closing error.", e);
            throw new ControllerException("Could not close input or output stream. ", e);
        } finally {
            isOpen = false;
        }
        log.info("Close BufferedReader and BufferedWriter of stream Controller.");
    }
}