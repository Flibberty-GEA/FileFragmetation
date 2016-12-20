package com.sysgears.example;

import com.sysgears.example.exceptions.InputException;
import org.apache.commons.cli.*;

/**
 * Class that parses input arguments using Apache Commons Cli library.
 *
 * @author Yevgen Goliuk
 */
public class ApacheCliParser {
//https://habrahabr.ru/post/123360/
    /**
     * Returns CommandLine object with options.
     *
     * @param args input arguments.
     * @return CommandLine object with options.
     */
    public InputDataHolder parse(final String[] args) {

        /* Initialize options object. */
        final Options options = new Options();

        /* Add specified options. */
        options.addOption("c", "command", true, "Command");
        options.addOption("p", "path", true, "Path to file");
        options.addOption("s", "size", true, "Size of file part");

        /* Initialize default apache commons parser. */
        final CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        try {
            /* Try to parse arguments to cmd. */
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new InputException("Could not parse input arguments. ");
        }

        return new InputDataHolder(cmd);
    }
}
