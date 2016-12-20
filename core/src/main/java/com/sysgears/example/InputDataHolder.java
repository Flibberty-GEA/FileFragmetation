package com.sysgears.example;

import com.sysgears.example.exceptions.InputException;
import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Class that convert CommandLine object to specified objects.
 *
 * @author Yevgen Goliuk
 */
public class InputDataHolder{

    /**
     * CommandLine objects that needs to convert.
     */
    private final CommandLine cmd;

    /**
     * Constructs CmdHolder with CommandLine object.
     *
     * @param cmd CommandLine object with options.
     */
    public InputDataHolder(final CommandLine cmd) {
        this.cmd = cmd;
    }

    /**
     * Returns the users command.
     *
     * @return the specified users command.
     * @throws InputException if users command is not supported.
     */
    public Command getCommand() throws InputException {
        return Command.getCommandByValue(cmd.getOptionValue("c"));
    }

    /**
     * Returns the users file object.
     *
     * @return the users file object.
     * @throws InputException if users file is not found or
     *                                 user didn't enter filepath name.
     */
    public File getFile() throws InputException {

        /* Get the filepath name from CommandLine object. */
        final String pathValue = cmd.getOptionValue("p");

        /* Throw InputArgumentException if filepath name is null
        * or file doesn't exist.
        * */
        if (pathValue == null || !new File(pathValue).exists()) {
            throw new InputException("Incorrect path name");
        }

        /* Return file with specified filepath name. */
        return new File(pathValue);
    }

    /**
     * Returns size of file part.
     *
     * @return size of file part.
     * @throws InputException if size has incorrect format.
     */
    public long getSize() throws InputException {

        /* Get size from CommandLine object. */
        String sizeValue = cmd.getOptionValue("s");

        if (sizeValue.contains("M")) {

            /* Replace 'M' or 'Mb' to six zeros. */
            sizeValue = sizeValue.replaceAll("M", "000000");

        } else if (sizeValue.contains("K")) {

            /* Replace 'K' or 'K' to three zeros. */
            sizeValue = sizeValue.replaceAll("K", "000");
        }

        long size;

        try {
            /* Try to parse size to Long. */
            size = Long.parseLong(sizeValue);
        } catch (NumberFormatException e) {
            throw new InputException("[" + sizeValue + "] has incorrect number format. ");
        }

        return size;
    }
}
