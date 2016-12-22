package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.ControllerException;
import com.sysgears.core.exceptions.InputException;
import com.sysgears.core.input.Command;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputDataParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that execute full cycle of applications work.
 */
public class MyMainExecutor {

    private final Controller streamController;
    private final InputDataParser inputDataParser;
    private final ExecutorService executorService;
    private final int threadPoolSize = 2;



    /**
     * Constructs MyMainExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with.
     * properties in specified format.
     *
     * @param streamController      streamController to contact with user.
     * @param inputDataParser inputDataParser to parse input messages from user.
     */
    public MyMainExecutor(final Controller streamController,
                          final InputDataParser inputDataParser) {
        this.streamController = streamController;
        this.inputDataParser = inputDataParser;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Constructs MyMainExecutor with streamController to contact with user,
     * inputDataParser to parse input messages from user and object with.
     * properties in specified format.
     *
     * @param streamController      streamController to contact with user.
     * @param inputDataParser inputDataParser to parse input messages from user.
     * @param nThreads the number of threads in the pool.
     */
    public MyMainExecutor(final Controller streamController,
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
                InputDataHolder inputDataHolder = inputDataParser.parse(message.split(" "));
                Command command = Command.getCommandByValue(inputDataHolder.getCommand());
                command.apply(inputDataHolder, executorService, streamController);
            } catch (ControllerException e) {
                streamController.sendMessage(e.getMessage());

            } catch (InputException e) {
                streamController.sendMessage(e.getMessage());

            } /*catch (InterruptedException e) {
                streamController.sendMessage("Program interrupted. Sorry. Try again. ");

            }*/ catch (Throwable t) {
                streamController.sendMessage("Critical error. " + t.getMessage());
            }
        }
    }
}