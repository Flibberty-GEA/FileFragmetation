package com.sysgears.core.input;

import org.apache.commons.cli.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;


/**
 * Class that parses input arguments using Apache Commons Cli library.
 */
public class ApacheCliParser implements InputDataParser {
    public static final Logger log = LogManager.getLogger(ApacheCliParser.class);

    /**
     * Returns InputDataHolder object with data from user's command.
     *
     * @param args input arguments
     * @return InputDataHolder object with data from user's command
     * @throws InputException if CommandLineParser
     *                        could not parse input arguments
     */
    public InputDataHolder parse(final String[] args) {
        log.info("Starts with parameter " + args.getClass().getTypeName() +
                " which includes " + Arrays.toString(args));

        final Options options = new Options();

        options.addOption("c", "command", true, "Command");
        options.addOption("p", "path", true, "Path to file");
        options.addOption("s", "size", true, "Size of file part");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Could not parse input arguments.");
            throw new InputException("Could not parse input arguments. ");
        }
        InputDataHolder result = new InputDataHolder(cmd);
        log.info("Return " + result.getClass().getTypeName());
        return result;
    }
}