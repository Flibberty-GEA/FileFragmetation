package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.ControllerException;
import com.sysgears.core.input.InputException;
import com.sysgears.core.input.Command;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputDataParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that execute full cycle of applications work.
 *
 * @author Yevgen Goliuk
 */
public class AppExecutor {

    public static final Logger log = LogManager.getLogger(AppExecutor.class);

    private final Controller streamController;
    private final InputDataParser inputDataParser;
    private final ExecutorService executorService;
    private final int threadPoolSize = 2;

    /**
     * Constructs AppExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with
     * properties in specified format.
     *
     * @param streamController streamController to contact with user
     * @param inputDataParser  inputDataParser to parse input messages from user
     */
    public AppExecutor(final Controller streamController,
                       final InputDataParser inputDataParser) {
        this.streamController = streamController;
        this.inputDataParser = inputDataParser;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        log.debug("Initialize AppExecutor with params: " +
                "StreamController, InputDataParser, ThreadPoolExecutor with default pool size = " + threadPoolSize);
    }

    /**
     * Constructs AppExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with
     * properties in specified format.
     *
     * @param streamController streamController to contact with user
     * @param inputDataParser  inputDataParser to parse input messages from user
     * @param nThreads         the number of threads in the pool
     */
    public AppExecutor(final Controller streamController,
                       final InputDataParser inputDataParser,
                       final int nThreads) {
        this.streamController = streamController;
        this.inputDataParser = inputDataParser;
        this.executorService = Executors.newFixedThreadPool(nThreads);
        log.debug("Initialize AppExecutor with params: " +
                "StreamController, InputDataParser, ThreadPoolExecutor with pool size = " + nThreads);
    }

    /**
     * Executes full cycle of applications work.
     */
    public void execute() {
        log.debug("Starts to execute full cycle of applications work.");

        while (streamController.isOpen()) {
            try {
                streamController.sendMessage("Enter parameters OR 'exit' to quit program");
                String message = streamController.getMessage();
                InputDataHolder inputDataHolder = inputDataParser.parse(message.split(" "));
                Command command = Command.getCommandByValue(inputDataHolder.getCommand());
                command.apply(inputDataHolder, executorService, streamController);
            } catch (ControllerException c) {
                log.warn("ControllerException "+c.toString());
                streamController.sendMessage(c.getMessage());
            } catch (InputException i) {
                log.warn("InputException "+i.toString());
                streamController.sendMessage(i.getMessage());
            } catch (Throwable t) {
                log.error("Unexpected exception "+t.toString());
                streamController.sendMessage("Unexpected exception ");
            }
        }
    }
}
