package com.sysgears.example.controller;

import java.io.*;

/**
 * Provides streams control, for contacts with user via the console.
 *
 * @author  Yevgen Goliuk
 */
public class StreamController {


    BufferedReader reader;
    BufferedWriter writer;

    /**
     * @return buffered character-output stream that uses a default-sized output buffer.
     */
    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     * Initialize buffered reader and writer from input and output stream
     *
     * @param input  source of input data
     * @param output destination of output data
     */
    public StreamController(final InputStream input, final OutputStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.writer = new BufferedWriter(new OutputStreamWriter(output));
    }

    /**
     * @return next line from reader
     * @throws StreamException if an I/O error occurs
     */
    public String getRequest() throws StreamException {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new StreamException("Sorry, I can't read your expression. Please try again. ");
        }
    }

    /**
     * @param response is a message or result of request
     * @throws StreamException if an I/O error occurs
     */
    public void sendMessage(final String response) throws StreamException {
        try {
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new StreamException("Sorry, a connection broken. Please try again. ");
        }
    }


    /**
     * @param fileName the File to read from
     * @return all info from File
     *
     * @throws StreamException  if the file does not exist, is a directory rather than a regular file,
     * or for some other reason cannot be opened for reading.
     */
    public String readFromFile (final String fileName) throws StreamException {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String lineText;
            while ((lineText = in.readLine()) != null) {
                result.append(lineText+"\n");
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new StreamException("Sorry, there are no information. ");
        } catch (IOException e) {
            throw new StreamException("Sorry, there are no information. ");
        }
        return result.toString();
    }
}
