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
//        -c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/main/resources/file.txt -s 100M
//        -c join -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/main/resources/file.txt_part_0 -s 100M
//        -c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/main/resources/file.bmp -s 1M
//        -c join -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/main/resources/file.bmp_part_0 -s 1M

    public static final Logger log = LogManager.getLogger(MainClass.class);

    /**
     * Main method of application.
     *
     * @param args input arguments from command line.
     * @throws IOException if something is wrong with properties.
     */
    public static void main(String[] args) throws IOException {

        log.info("Start program.");

        try {
            final Controller controller = new StreamController(System.in, System.out);
            final InputDataParser inputDataParser = new ApacheCliParser();

            log.info("Initialize AppExecutor and execute it.");

            new AppExecutor(controller, inputDataParser, 2).execute();
        } catch (Throwable t) {
            log.fatal(t.getMessage(), t);
        }
    }
}
