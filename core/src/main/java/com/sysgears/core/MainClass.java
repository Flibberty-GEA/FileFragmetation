package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.StreamController;
import com.sysgears.core.input.ApacheCliParser;
import com.sysgears.core.input.InputDataParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.io.IOException;

/**
 * Main class of application.
 */
public class MainClass {
//    -c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/main/resources/file.bmp -s 1M
//    -c exit

    public static final Logger LOG = LogManager.getLogger(MainClass.class);

    /**
     * Main method of application.
     *
     * @param args input arguments from command line.
     * @throws IOException if something is wrong with properties.
     */
    public static void main(String[] args) throws IOException {

        LOG.info("Start program.");

        try {
            final Controller controller = new StreamController(System.in, System.out);
            final InputDataParser inputDataParser = new ApacheCliParser();
            new MyMainExecutor(controller, inputDataParser, 2).execute();
        } catch (Throwable t) {
            LOG.fatal(t.getMessage(), t);
        }
    }
}
