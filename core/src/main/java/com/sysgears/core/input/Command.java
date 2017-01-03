package com.sysgears.core.input;

import com.sysgears.controller.Controller;
import com.sysgears.fragmentation.Joiner;
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

            log.info("Starts with parameters:\n" + "\t\t- " + inputDataHolder.getClass() +
                    " which includes Command \"" + inputDataHolder.getCommand() +
                    "\", file object  \"" + inputDataHolder.getFile().getName() +
                    "\", size of file part " + inputDataHolder.getSize() + " bytes;\n" +
                    "\t\t- " + executorService.getClass() +
                    ((executorService instanceof ThreadPoolExecutor)? " with maximum allowed number of threads = "
                            + ((ThreadPoolExecutor)executorService).getMaximumPoolSize() : "") + "\n" +
                    "\t\t- " + controller.getClass());

            log.info("Initialize StatisticService and Splitter.");
            StatisticService statistic = new StatisticServiceImpl();
            Splitter splitter = new Splitter(executorService);
            File fileIn = inputDataHolder.getFile();
            long partSize = inputDataHolder.getSize();
            splitter.split(statistic, fileIn, partSize);
            this.printStatistic(executorService, statistic, controller);
        }
    },

    /**
     * Command to join parts of file.
     */
    JOIN("join"){
        public void apply(final InputDataHolder inputDataHolder,
                          final ExecutorService executorService,
                          final Controller controller) throws IOException {
            log.info("Starts with parameters:\n" + "\t\t- " + inputDataHolder.getClass() +
                    " which includes Command \"" + inputDataHolder.getCommand() +
                    "\", file object  \"" + inputDataHolder.getFile().getName() +
                    "\", size of file part " + inputDataHolder.getSize() + " bytes;\n" +
                    "\t\t- " + executorService.getClass() +
                    ((executorService instanceof ThreadPoolExecutor)? " with maximum allowed number of threads = "
                            + ((ThreadPoolExecutor)executorService).getMaximumPoolSize() : "") + "\n" +
                    "\t\t- " + controller.getClass());

            log.info("Initialize StatisticService and Joiner.");
            StatisticService statistic = new StatisticServiceImpl();
            Joiner joiner = new Joiner(executorService);
            File anyPart = inputDataHolder.getFile();
            joiner.join(statistic, anyPart);
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
            log.info("Starts Command \"" + inputDataHolder.getCommand() + "\"");
            executorService.shutdownNow();
            log.info("Attempts to stop all actively executing tasks, halts the processing of waiting tasks.");
            controller.sendMessage("Good by!");
            controller.closeController();
        }
    };

    public static final Logger log = LogManager.getLogger(Command.class);

    /**
     * String value of command.
     */
    private final String value;

    /**
     * Constructs Command with String value.
     *
     * @param value the String value of command
     */
    Command(String value) {
        this.value = value;
    }

    /**
     * Returns the String value of command.
     *
     * @return the String value of command
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the command corresponding for this value.
     *
     * @param value String value of command
     * @return the command corresponding for this value
     */
    public static Command getCommandByValue(final String value) {
        Command command = null;
        for (Command com : Command.values()) {
            if (com.toString().equals(value.toLowerCase())) {
                command = com;
            }
        }
        log.info("Create Command \"" + value + "\".");
        return command;
    }

    /**
     * Is user's command a correct command.
     *
     * @param inputCommand string user's command
     * @return true if command is correct
     */
    public static boolean isCommandCorrect(final String inputCommand){
        String userCommand = inputCommand.toLowerCase();
        for (Command comm: Command.values()) {
            if(comm.toString().equals(userCommand)){
                return true;
            }
        } return false;
    }

    /**
     * Command should to do.
     *
     * @param inputDataHolder specified objects with data from user's command:
     *                        command name, file for splitting or joining, size of file part
     * @param executorService
     * @param controller for input and output streams
     * @throws IOException
     */
    public abstract void apply(final InputDataHolder inputDataHolder,
                               final ExecutorService executorService,
                               final Controller controller) throws IOException;

    /**
     * Print statistic info.
     *
     * @param executorService
     * @param statistic service for storages and accesses to statistical data
     * @param controller for input and output streams
     */
    protected void printStatistic(final ExecutorService executorService,
                                  final StatisticService statistic,
                                  final Controller controller){
        while (((ThreadPoolExecutor) executorService).getActiveCount() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(statistic.STATISTIC_SHOW_TIMEOUT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted, closing. ", e);
            }
            String statisticMessage = statistic.get();
            controller.sendMessage(statisticMessage);
        }
    }
}
