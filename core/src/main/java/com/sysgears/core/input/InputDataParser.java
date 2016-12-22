package com.sysgears.core.input;

/**
 * Interface that parses input arguments.
 */
public interface InputDataParser {

    /**
     * Returns CommandLine object with options.
     *
     * @param args input arguments.
     * @return InputDataHolder object with options.
     */
    InputDataHolder parse(String[] args);

}
