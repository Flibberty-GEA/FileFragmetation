package com.sysgears.core.input;

import com.sysgears.core.exceptions.InputException;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Class that convert CommandLine object to specified objects.
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
        log.info("Create " + this.getClass().getSimpleName() + " with param " + cmd.getClass() + ".");
        String inputCommand = cmd.getOptionValue("c");
        if (Command.isCommandCorrect(inputCommand)) {
            command = inputCommand.toLowerCase();
        } else {
            log.error("Command '" + inputCommand + "' is not supported");
            throw new InputException("Command '" + inputCommand + "' is not supported");
        }

        if (!command.equals("exit")) {
            final String pathValue = cmd.getOptionValue("p");
            if (pathValue == null || !new File(pathValue).exists()) {
                log.error("Incorrect path name \"" + pathValue + "\".");
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
                    log.error("Incorrect number format \"" + sizeValue + "\".");
                    throw new InputException("[" + sizeValue + "] has incorrect number format. ");
                }
                this.size = size;
            } else this.size = 0L;

            log.info("User command \"" + this.command + "\". " +
                    "User's file object \"" + this.file.getName() + "\". " +
                    "Size of file part = " + this.size + " bytes.");
        } else {
            file = null;
            size = 0L;
            log.info("User command \"" + this.command + "\".");
        }
    }

    /**
     * Returns the users command.
     *
     * @return string with user command
     */
    public String getCommand() {
//        log.info("User command \"" + command + "\".");
        return command;
    }


    /**
     * Returns the users file object.
     *
     * @return the users file object
     */
    public File getFile() {
//        log.info("Users file object \"" + file.getName() + "\".");
        return file;
    }

    /**
     * Returns size of file part.
     *
     * @return size of file part
     */
    public long getSize() {
//        log.info("Size of file part = " + size + " bytes.");
        return size;
    }
}
