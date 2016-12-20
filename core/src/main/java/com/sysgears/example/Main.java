package com.sysgears.example;

import com.sysgears.example.controller.StreamController;
import com.sysgears.example.exceptions.PropertyException;

import java.io.IOException;

/**
 * Main class of application.
 *
 * @author Yevgen Goliuk
 */
public class Main {
    /**
     * Main method of application.
     *
     * @param args input arguments from command line.
     * @throws IOException if something is wrong with properties.
     */
    public static void main(String[] args) throws IOException {

        try {

            /* Initialize Controller by StreamController. */
            final StreamController controller = new StreamController(System.in, System.out);

            /* Initialize InputDataParser by ApacheCliParser. */
            final ApacheCliParser inputDataParser = new ApacheCliParser();

//            /* Initialize PropertyData by parsing and converting file with properties. */
//            final PropertyData propertyData = PropertyUtil.convertToPropertyData(new PropertyParser().parse());

            /* Initialize Executor with Controller, InputDataParser and PropertyData. */
            final Executor executor = new Executor(controller, inputDataParser/*, propertyData*/);

            /* Start program. */
            executor.execute();
        } catch (PropertyException e) {
        } catch (Throwable t) {
        }
    }
}
