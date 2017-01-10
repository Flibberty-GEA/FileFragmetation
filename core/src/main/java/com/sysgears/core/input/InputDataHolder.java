package com.sysgears.core.input;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Class that convert CommandLine object to specified objects with data from user's command.
 */
public class InputDataHolder {
    public static final Logger log = LogManager.getLogger(InputDataHolder.class);

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
        log.debug("Create " + this.getClass().getSimpleName() + " with param " + cmd.getClass() + ".");
        String inputCommand = cmd.getOptionValue("c");
        try {
            inputCommand.isEmpty();
        } catch (NullPointerException n){
            throw new InputException("Command is not supported. \n" +
                    "Please enter correct command like this " +
                    "\"-c split -p /file_path.file_name -s size_of_part\"");
        }

        if (Command.isCommandCorrect(inputCommand)) {
            command = inputCommand.toLowerCase();
        } else {
            log.warn("Command '" + inputCommand + "' is not supported");
            throw new InputException("Command '" + inputCommand + "' is not supported");
        }

        if (!command.equals("exit")) {
            final String pathValue = cmd.getOptionValue("p");
            if (pathValue == null || !new File(pathValue).exists()) {
                log.warn("Incorrect path name \"" + pathValue + "\".");
                throw new InputException("Incorrect path name");
            } else this.file = new File(pathValue);

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
                    log.warn("Incorrect number format \"" + sizeValue + "\".");
                    throw new InputException("[" + sizeValue + "] has incorrect number format. ");
                }
                this.size = size;
            } else this.size = 0L;

            log.debug("User command \"" + this.command + "\". " +
                    "User's file object \"" + this.file.getName() + "\". " +
                    "Size of file part = " + this.size + " bytes.");
        } else {
            file = null;
            size = 0L;
            log.debug("User command \"" + this.command + "\".");
        }
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
