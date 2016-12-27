package com.sysgears.core.input;

import com.sysgears.controller.Controller;
import com.sysgears.core.MainClass;
import com.sysgears.core.exceptions.InputException;
import com.sysgears.fragmentation.Splitter;
import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Enumeration of users command.
 */
public enum Command {

    /**
     * Command to split file into parts.
     */
    SPLIT("split"){
        public void apply(final InputDataHolder inputDataHolder,
                          final ExecutorService executorService,
                          final Controller controller) throws IOException {

            LOG.info("Initialize StatisticService.");

            StatisticService statistic = new StatisticServiceImpl();

            LOG.info("Initialize Splitter and File");

            Splitter splitter = new Splitter(executorService);
            File fileIn = inputDataHolder.getFile();

            long partSize = inputDataHolder.getSize();

            LOG.info("Initialize size of parts. Size = " + partSize);

            splitter.split(statistic, fileIn, partSize);
            this.printStatistic(executorService, statistic, controller);
        }
    },

    /**
     * Command to quit program.
     */
    EXIT("exit") {
        public void apply(final InputDataHolder inputDataHolder,
                          final ExecutorService executorService,
                          final Controller controller) throws IOException {
            executorService.shutdownNow();
            LOG.info("Attempts to stop all actively executing tasks, halts the processing of waiting tasks.");
            controller.sendMessage("Good by!");
            controller.getWriter().close();
            LOG.info("Close BufferedWriter of stream Controller.");
        }
    };

    public static final Logger LOG = LogManager.getLogger(Command.class);

    /**
     * String value of command.
     */
    private final String value;

    /**
     * Constructs Command with String value.
     *
     * @param value the String value of command.
     */
    Command(String value) {
        this.value = value;
    }

    /**
     * Returns the String value of command.
     *
     * @return the String value of command.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the command corresponding for this value.
     *
     * @param value String value of command.
     * @return the command corresponding for this value.
     * @throws InputException if current command doesn't exist.
     */
    public static Command getCommandByValue(final String value) throws InputException {
        Command command = null;
        for (Command com : Command.values()) {
            if (com.toString().equals(value)) {
                command = com;
            }
        }
        if (command == null) {
            LOG.error("Command '" + value + "' is not supported");
            throw new InputException("Command '" + value + "' is not supported");
        }
        return command;
    }

    /**
     * @throws IOException
     */
    public abstract void apply(final InputDataHolder inputDataHolder,
                               final ExecutorService executorService,
                               final Controller controller) throws IOException;

    protected void printStatistic(final ExecutorService executorService,
                                  final StatisticService statistic,
                                  final Controller controller){
        while (((ThreadPoolExecutor) executorService).getActiveCount() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(statistic.STATISTIC_SHOW_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String statisticMessage = statistic.get();

            LOG.info("Send statistic message to user. Message: " + statisticMessage);

            controller.sendMessage(statisticMessage);
        }
    }
}
