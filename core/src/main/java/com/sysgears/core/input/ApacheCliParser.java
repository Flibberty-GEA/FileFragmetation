package com.sysgears.core.input;

import org.apache.commons.cli.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;


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
        log.debug("Starts with parameter " + args.getClass().getTypeName() +
                " which includes " + Arrays.toString(args));
        final Options options = new Options();

        options.addOption("c", "command", true, "It's a user command");
        options.addOption("p", "path", true, "It's a path to file");
        options.addOption("s", "size", true, "It's a size of file part");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            log.warn("CommandLineParser could not parse input arguments. \n" +
                    "Method parse(options, args) takes next params:\n" +
                    "\t\t- options (org.apache.commons.cli.Options): " +
                    Arrays.toString(new Object[]{options.getOptions()}) + "\n" +
                    "\t\t- String[] args: " + Arrays.toString(args)); // add info param
            throw new InputException("Could not parse input arguments. ");
        }
        InputDataHolder result = new InputDataHolder(cmd);
        log.debug("Return " + result.getClass().getTypeName());
        return result;
    }
}