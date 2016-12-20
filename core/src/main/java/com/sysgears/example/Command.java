package com.sysgears.example;

import com.sysgears.example.controller.StreamController;
import com.sysgears.example.exceptions.InputException;

import java.io.IOException;

/**
 * Enumeration of users command.
 *
 * @author Yevgen Goliuk
 */
public enum Command {

    /**
     * Command to split file into parts.
     */
    SPLIT("split"){
        @Override
        void apply(final StreamController streamController, final String inputRequest) throws IOException {
            System.out.println("bla-bla");
            final ApacheCliParser parser = new ApacheCliParser();
            InputDataHolder dataHolder = parser.parse(inputRequest.split(" "));
        }
    },

    /**
     * Command to quit program.
     */
    EXIT("exit") {
        @Override
        void apply(final StreamController streamController, final String inputRequest) throws IOException {
            streamController.sendMessage("Good by!");
            streamController.getWriter().close();
        }
    },

    /**
     * command for seeing help info
     */
    HELP("help"){
        @Override
        void apply(final StreamController streamController, final String inputRequest) throws IOException {
            String helpInfo = streamController.readFromFile("core/src/main/resources/help.txt");
            streamController.sendMessage(helpInfo);
        }
    };

    /**
     * String value of command.
     */
    private final String value;

    /**
     * Constructs Command with String value.
     *
     * @param value the String value of command.
     */
    Command(String value) {
        this.value = value;
    }

    /**
     * Returns the String value of command.
     *
     * @return the String value of command.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the command corresponding for this value.
     *
     * @param value String value of command.
     * @return the command corresponding for this value.
     * @throws InputException if current command doesn't exist.
     */
    public static Command getCommandByValue(final String value) throws InputException {

        Command command = null;

        /* Iterate by all existed commands. */
        for (Command com : Command.values()) {

            /* Compare value and String value of current command from existed commands. */
            if (com.toString().equals(value)) {
                command = com;
            }
        }

        /* If current command doesn't exist. */
        if (command == null) {
            throw new InputException("Command '" + value + "' is not supported");
        }

        return command;
    }


    /**
     * @param streamController for contact with user by console
     * @throws IOException
     */
    abstract void apply(final StreamController streamController, final String inputRequest) throws IOException;
}
