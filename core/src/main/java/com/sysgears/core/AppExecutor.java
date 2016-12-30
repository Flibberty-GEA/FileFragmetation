package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.ControllerException;
import com.sysgears.core.exceptions.InputException;
import com.sysgears.core.input.Command;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputDataParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that execute full cycle of applications work.
 */
public class AppExecutor {

    public static final Logger LOG = LogManager.getLogger(AppExecutor.class);

    private final Controller streamController;
    private final InputDataParser inputDataParser;
    private final ExecutorService executorService;
    private final int threadPoolSize = 2;

    /**
     * Constructs AppExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with.
     * properties in specified format.
     *
     * @param streamController      streamController to contact with user.
     * @param inputDataParser inputDataParser to parse input messages from user.
     */
    public AppExecutor(final Controller streamController,
                       final InputDataParser inputDataParser) {
        this.streamController = streamController;
        this.inputDataParser = inputDataParser;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Constructs AppExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with.
     * properties in specified format.
     *
     * @param streamController      streamController to contact with user.
     * @param inputDataParser inputDataParser to parse input messages from user.
     * @param nThreads the number of threads in the pool.
     */
    public AppExecutor(final Controller streamController,
                       final InputDataParser inputDataParser,
                       final int nThreads) {
        this.streamController = streamController;
        this.inputDataParser = inputDataParser;
        this.executorService = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * Executes full cycle of applications work.
     */
    public void execute() {

        while (true) {
            try {
                streamController.sendMessage("Enter parameters OR 'exit' to quit program");
                String message = streamController.getMessage();

                LOG.info("Get message from user. Message: '" + message + "'.");

                InputDataHolder inputDataHolder = inputDataParser.parse(message.split(" "));

                LOG.info("Parse message to InputDataHolder.");

                Command command = Command.getCommandByValue(inputDataHolder.getCommand());

                LOG.info("Get command from CommandLine. Command = " + command + ". Try to apply this command.");

                command.apply(inputDataHolder, executorService, streamController);

            } catch (ControllerException e) {
                streamController.sendMessage(e.getMessage());

            } catch (InputException e) {
                streamController.sendMessage(e.getMessage());

            } catch (Throwable t) {
                streamController.sendMessage("Critical error. " + t.getMessage());
            }
        }
    }
}
