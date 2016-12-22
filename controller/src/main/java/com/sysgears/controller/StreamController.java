package com.sysgears.controller;

import java.io.*;

/**
 * Class for contact with user.
 */
public class StreamController implements Controller {

    /**
     * Buffered reader of input data.
     */
    private final BufferedReader reader;

    /**
     * Buffered writer of output data.
     */
    private final BufferedWriter writer;

    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     * Initializes buffered reader and writer from input and output stream.
     *
     * @param input  source of input data.
     * @param output destination of output data.
     */
    public StreamController(final InputStream input, final OutputStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.writer = new BufferedWriter(new OutputStreamWriter(output));
    }

    /**
     * Gets message from user.
     *
     * @return string message from user to application.
     * @throws ControllerException if an controller error occurs.
     */
    public String getMessage() throws ControllerException {
        String message;

        try {
            message = reader.readLine();
        } catch (IOException e) {
            throw new ControllerException("Could not read users message. ", e);
        }

        return message;
    }

    /**
     * Sends message to user.
     *
     * @param message string message from application to user.
     * @throws ControllerException if an controller error occurs.
     */
    public void sendMessage(final String message) throws ControllerException {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new ControllerException("Could not write message to user. ", e);
        }
    }
}