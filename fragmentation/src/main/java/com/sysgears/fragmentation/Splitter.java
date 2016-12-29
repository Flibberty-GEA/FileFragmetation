package com.sysgears.fragmentation;

import com.sysgears.statistic.StatisticService;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * Class that splits file into parts.
 */
public class Splitter {

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
     * Construct Splitter with executor service and default buffer size.
     *
     * @param executorService executor that executes workers.
     */
    public Splitter(final ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = DEFAULT_BUFFER_SIZE;
    }

    /**
     * Construct Splitter with executor service and bytes buffer.
     *
     * @param executorService executor that executes workers.
     * @param bufferSize      size of buffer for reading and writing bytes.
     */
    public Splitter(final ExecutorService executorService, final int bufferSize) {
        this.executorService = executorService;
        this.bufferSize = bufferSize;
    }

    /**
     * Splits file into parts.
     *
     * @param statistic statistic object.
     * @param originalFile       source file than need to split.
     * @param maxPartSize  size of parts that need to split a file.
     */
    public void split(final StatisticService statistic, final File originalFile, final long maxPartSize) {

        long fullSize = originalFile.length();
        statistic.setFullExpectedSize(fullSize);
        int partCounter = 0;
        long currentRealPartSize = maxPartSize;

        /* While reading position in source file is less than source file size. */
        for (long positionSrc = 0; positionSrc < fullSize; positionSrc += currentRealPartSize) {
            /* Decrease current part size if file ending size is less than part size. */
            if (fullSize - positionSrc < maxPartSize) {
                currentRealPartSize = fullSize - positionSrc;
            }

            File resultFile = new File(originalFile.getAbsolutePath() + partNamePostfix + partCounter);
            Task task = new Task(originalFile, positionSrc, currentRealPartSize, resultFile, 0, statistic, bufferSize);
            executorService.execute(task);
            partCounter++;
        }
//        executorService.shutdownNow();
    }
}
