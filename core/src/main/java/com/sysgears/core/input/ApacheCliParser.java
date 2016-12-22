package com.sysgears.core.input;

import com.sysgears.core.exceptions.InputException;
import org.apache.commons.cli.*;

/**
 * Class that parses input arguments using Apache Commons Cli library.
 */
public class ApacheCliParser implements InputDataParser {

    /**
     * Returns CommandLine object with options.
     *
     * @param args input arguments.
     * @return InputDataHolder object with options.
     */
    public InputDataHolder parse(final String[] args) {

        final Options options = new Options();

        options.addOption("c", "command", true, "Command");
        options.addOption("p", "path", true, "Path to file");
        options.addOption("s", "size", true, "Size of file part");

        final CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new InputException("Could not parse input arguments. ");
        }

        return new InputDataHolder(cmd);
    }
}