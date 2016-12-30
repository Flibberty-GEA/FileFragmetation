package com.sysgears.core.input;

import com.sysgears.core.exceptions.InputException;
import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Class that convert CommandLine object to specified objects.
 */
public class InputDataHolder {
    private final String command;
    private final File file;
    private final long size;

    /**
     * Constructs InputDataHolder with CommandLine object.
     *
     * @param cmd CommandLine object with options that needs to convert
     * @throws InputException if users command is not supported,
     *                        if users file is not found or user didn't enter filepath nam,
     *                        if size has incorrect format
     */
    public InputDataHolder(final CommandLine cmd) throws InputException {
        this.command = cmd.getOptionValue("c");
        if (command.equals("exit")) {
            file = null;
        } else {
            final String pathValue = cmd.getOptionValue("p");
            if (pathValue == null || !new File(pathValue).exists()) {
                throw new InputException("Incorrect path name");
            } else this.file = new File(pathValue);
        }

        String sizeValue = cmd.getOptionValue("s");

        if (sizeValue != null) {
            if (sizeValue.contains("M")) {
                sizeValue = sizeValue.replaceAll("M", "000000");
            } else if (sizeValue.contains("K")) {
                sizeValue = sizeValue.replaceAll("K", "000");
            }
            long size;
            try {
                size = Long.parseLong(sizeValue);
            } catch (NumberFormatException e) {
                throw new InputException("[" + sizeValue + "] has incorrect number format. ");
            }
            this.size = size;
        } else this.size = 0L;
    }

    /**
     * Returns the users command.
     *
     * @return string with user command
     */
    public String getCommand() {
        return command;
    }


    /**
     * Returns the users file object.
     *
     * @return the users file object
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns size of file part.
     *
     * @return size of file part
     */
    public long getSize() {
        return size;
    }
}
