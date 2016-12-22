package com.sysgears.core.input;

import com.sysgears.core.exceptions.InputException;
import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Class that convert CommandLine object to specified objects.
 */
public class InputDataHolder {

    /**
     * CommandLine objects that needs to convert.
     */
//    private final CommandLine cmd;

    private final String command;
    private final File file;
    private final long size;

    /**
     * Constructs InputDataHolder with CommandLine object.
     *
     * @param cmd CommandLine object with options.
     */
    public InputDataHolder(final CommandLine cmd) {
//        this.cmd = cmd;
        this.command = cmd.getOptionValue("c");
        final String pathValue = cmd.getOptionValue("p");
//        if (pathValue == null || !new File(pathValue).exists()) {
//            throw new InputException("Incorrect path name");
//        }
        if (pathValue == null || !new File(pathValue).exists()) {
            file = null;
//            throw new InputException("Incorrect path name");
        } else this.file = new File(pathValue);
        String sizeValue = cmd.getOptionValue("s");

        if (sizeValue != null){
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

//    /**
//     * Returns the users command.
//     *
//     * @return the specified users command.
//     * @throws InputException if users command is not supported.
//     */
//    public Command getCommandObj() throws InputException {
//        return Command.getCommandByValue(cmd.getOptionValue("c"));
//    }

    public String getCommand() {

        return command;

    }


    /**
     * Returns the users file object.
     *
     * @return the users file object.
     * @throws InputException if users file is not found or
     *                                 user didn't enter filepath name.
     */
    public File getFile() throws InputException {

        return file;

//        /* Get the filepath name from CommandLine object. */
//        final String pathValue = cmd.getOptionValue("p");
//
//        /* Throw InputArgumentException if filepath name is null
//        * or file doesn't exist.
//        * */
//        if (pathValue == null || !new File(pathValue).exists()) {
//            throw new InputException("Incorrect path name");
//        }
//
//        /* Return file with specified filepath name. */
//        return new File(pathValue);
    }

    /**
     * Returns size of file part.
     *
     * @return size of file part.
     * @throws InputException if size has incorrect format.
     */
    public long getSize() throws InputException {

        return size;
//
//        /* Get size from CommandLine object. */
//        String sizeValue = cmd.getOptionValue("s");
//
//        if (sizeValue.contains("M")) {
//
//            /* Replace 'M' or 'Mb' to six zeros. */
//            sizeValue = sizeValue.replaceAll("M", "000000");
//
//        } else if (sizeValue.contains("K")) {
//
//            /* Replace 'K' or 'K' to three zeros. */
//            sizeValue = sizeValue.replaceAll("K", "000");
//        }
//
//        long size;
//
//        try {
//            /* Try to parse size to Long. */
//            size = Long.parseLong(sizeValue);
//        } catch (NumberFormatException e) {
//            throw new InputException("[" + sizeValue + "] has incorrect number format. ");
//        }
//
//        return size;
    }
}
