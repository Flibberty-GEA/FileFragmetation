package com.sysgears.core.input;

/**
 * Interface that parses input arguments.
 */
public interface InputDataParser {

    /**
     * Returns InputDataHolder object with data from user's command.
     *
     * @param args input arguments
     * @return InputDataHolder object
     */
    InputDataHolder parse(String[] args);

}
