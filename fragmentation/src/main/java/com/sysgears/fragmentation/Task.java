package com.sysgears.fragmentation;

import com.sysgears.statistic.StatisticService;

import java.io.File;
import java.io.IOException;

/**
 * Class that do some work in another thread
 */
public class Task implements Runnable {

    /**
     * Random access file handler that read and write bytes into file.
     */
    private final FileAccessService fileAccessService = new FileAccessService();

    /**
     * Source file from which need to read bytes.
     */
    private final File originalFile;

    /**
     * Position of bytes which need to read in source file.
     */
    private final long positionFromOriginalFile;

    /**
     * Length of bytes which need to read in source file.
     */
    private final long currentPartSize;

    /**
     * File into which need to write bytes.
     */
    private final File resultFile;

    /**
     * Position in file destination where need to write bytes.
     */
    private final long positionIntoResultFile;

    /**
     * Object which stored statistic of work.
     */
    private final StatisticService statistic;

    /**
     * Buffer for reading and writing bytes.
     */
    private final byte[] bufferForBytes;

    /**
     * Constructs Task with source file, position of bytes in source file,
     * destination file, position in destination file, object statistic and
     * bytes buffer size.
     *
     * @param originalFile              file from which need to read bytes.
     * @param positionFromOriginalFile  position of bytes which need to read in source file.
     * @param partSize                  length of bytes which need to read in source file.
     * @param resultFile                file into which need to write bytes.
     * @param positionIntoResultFile    position in file destination where need to write bytes.
     * @param statistic                 object which stored statistic of work.
     * @param bufferSize                size of buffer for reading and writing bytes.
     */
    public Task(final File originalFile, final long positionFromOriginalFile, final long partSize,
                final File resultFile, final long positionIntoResultFile,
                final StatisticService statistic, final int bufferSize) {

        this.originalFile = originalFile;
        this.positionFromOriginalFile = positionFromOriginalFile;
        this.currentPartSize = partSize;
        this.resultFile = resultFile;
        this.positionIntoResultFile = positionIntoResultFile;
        this.statistic = statistic;
        this.bufferForBytes = new byte[bufferSize];
    }

    /**
     * Read data from source file and write it into destination file.
     */
    public void run() {

        /* Initialize name of current thread. */
        String threadName = Thread.currentThread().getName();

        /* Initialize size of expected work for current worker. */
        statistic.setExpected(threadName, currentPartSize);

        /* Initialize size of actual done work for current worker. */
        statistic.setActual(threadName, 0L);

        /* Initialize length of reading and writing data in buffer. */
        int bufferLength = bufferForBytes.length;

        /* While done size is less than part size. */
        for (long doneSize = 0L; doneSize < currentPartSize; doneSize += bufferLength) {

            /* Decrease bufferLength if part ending size is less than buffer size. */
            if (bufferForBytes.length > currentPartSize - doneSize) {
                bufferLength = (int) (currentPartSize - doneSize);
            }

            try {
                /* Read data from source file. */
                int size = fileAccessService.read(originalFile, bufferForBytes, positionFromOriginalFile + doneSize, bufferLength);

                /* Write data into destination file. */
                fileAccessService.write(resultFile, bufferForBytes, positionIntoResultFile + doneSize, size);
            } catch (IOException ignore) {
            }

            /* Increment actual size of done work. */
            statistic.increaseActual(threadName, bufferLength);
        }
    }
}
