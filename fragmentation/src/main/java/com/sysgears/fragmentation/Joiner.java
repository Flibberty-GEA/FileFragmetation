package com.sysgears.fragmentation;

import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.repository.StatisticRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yevgen Goliuk
 */
public class Joiner {
    public static final Logger log = LogManager.getLogger(Joiner.class);

    /**
     * Postfix in name of file parts.
     */
    private final String partNamePostfix = "_part_";

    /**
     * MyMainExecutor that executes workers.
     */
    private final ExecutorService executorService;

    /**
     * Buffer for reading and writing bytes.
     */
    private final int bufferSize;

    /**
     * Buffer for reading and writing bytes.
     */
    private final int DEFAULT_BUFFER_SIZE = 1000;

    /**
     * Construct Joiner with executor service and default buffer size.
     *
     * @param executorService executor that executes workers.
     */
    public Joiner(final ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        log.debug("Initialize Joiner with next params:\n" +
                "\t\t- executorService with " + this.executorService.getClass().getSimpleName() +
                " with maximum pool size = " + ((ThreadPoolExecutor) this.executorService).getMaximumPoolSize() + ";\n" +
                "\t\t- size of buffer for reading and writing is default = " + this.bufferSize + " bytes.");
    }

    /**
     * Construct Joiner with executor service and bytes buffer.
     *
     * @param executorService executor that executes workers.
     * @param bufferSize      size of buffer for reading and writing bytes.
     */
    public Joiner(final ExecutorService executorService, final int bufferSize) {
        this.executorService = executorService;
        this.bufferSize = bufferSize;
        log.debug("Initialize Joiner with next params:\n" +
                "\t\t- executorService with " + this.executorService.getClass().getSimpleName() +
                " with maximum pool size = " + ((ThreadPoolExecutor) this.executorService).getMaximumPoolSize() + ";\n" +
                "\t\t- size of buffer for reading and writing = " + this.bufferSize + " bytes.");
    }


    /**
     * Joins parts of the file.
     *
     * @param statistic statistic object.
     * @param anyPart   any part of file which needs to join.
     */
    public void join(final StatisticService statistic, final File anyPart) {

        String partWithNumbRegex = partNamePostfix + "\\d+$";
        String originalFilePath = anyPart.getAbsolutePath().replaceAll(partWithNumbRegex, "");
        File resultFile = new File(originalFilePath);
        long positionReultFiel = 0L;

        for (int counter = 0; new File(originalFilePath + partNamePostfix + counter).exists(); counter++) {
            File originalFile = new File(originalFilePath + partNamePostfix + counter);
            Task task = new Task(originalFile, 0, originalFile.length(), resultFile, positionReultFiel, statistic, bufferSize);
            log.debug("Initialize and execute task number " + (counter + 1));
            executorService.execute(task);
            positionReultFiel += anyPart.length();
        }
        log.debug("Set full size of work for statistic. Size = " + positionReultFiel);
        statistic.setFullExpectedSize(positionReultFiel);
    }
}
