package com.sysgears.statistic.repository;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class StatisticRepository {
    public static final Logger log = LogManager.getLogger(StatisticRepository.class);

    /**
     * Map where key is a thread name and value is expected count of
     * bytes that this thread should to read and write.
     */
    private final Map<String, Long> expectedByThread = new TreeMap<String, Long>();

    /**
     * Map where key is a thread name and value is actual count of
     * bytes that this thread already read and written.
     */
    private final Map<String, Long> actualByThread = new TreeMap<String, Long>();

    /**
     * Time when statistic started.
     */
    private final Date start = new Date();

    /**
     * Full size of work.
     */
    private long expectedSize = 0L;

    /**
     * Size of work done.
     */
    private long actualSize = 0L;

    /**
     * Sets expected size of work for all threads.
     *
     * @param expectedSize full size of work
     */
    public void setFullExpectedSize(long expectedSize) {
        this.expectedSize = expectedSize;
    }

    /**
     * Sets expected size of work for current thread.
     *
     * @param threadName   name of current thread
     * @param expectedSize full size of work for current thread
     */
    public synchronized void setExpected(final String threadName, final Long expectedSize) {
        expectedByThread.put(threadName, expectedSize);
    }

    /**
     * Sets actual size of work for current thread.
     *
     * @param threadName name of current thread
     * @param actualSize size of work done for current thread
     */
    public synchronized void setActual(final String threadName, final Long actualSize) {
        actualByThread.put(threadName, actualSize);
    }

    /**
     * Increments actual size of work for current thread.
     *
     * @param threadName name of current thread
     * @param size       count of bytes which need to increase actual size on
     */
    public synchronized void increaseActual(final String threadName, final long size) {
        log.trace("StatisticRepository.increaseActual() starts with params: " +
                "thread name \""+threadName+ "\", count to increase actual size on = "+size +
                ". Old size = "+actualByThread.get(threadName));

        /* Increase size of work done for current thread. */
        actualByThread.put(threadName, size + actualByThread.get(threadName));

        /* Increase size of work done for all threads. */
        this.actualSize += size;
//        log.debug("New size = "+actualByThread.get(threadName));
    }

    public Map<String, Long> getExpectedByThread() {
        return expectedByThread;
    }

    public Map<String, Long> getActualByThread() {
        return actualByThread;
    }

    public Date getStart() {
        return start;
    }

    public long getExpectedSize() {
        return expectedSize;
    }

    public long getActualSize() {
        return actualSize;
    }
}
