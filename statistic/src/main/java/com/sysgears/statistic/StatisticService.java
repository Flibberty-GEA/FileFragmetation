package com.sysgears.statistic;


/**
 * Service for storages and accesses to statistical data.
 */
public interface StatisticService {

    long STATISTIC_SHOW_TIMEOUT = 1000L;

    /**
     * Sets expected size.
     *
     * @param expectedSize full size of work
     */
    void setFullExpectedSize(long expectedSize);

    /**
     * Sets expected size of work for current thread.
     *
     * @param threadName   name of current thread
     * @param expectedSize full size of work for current thread
     */
    void setExpected(String threadName, Long expectedSize);

    /**
     * Sets actual size of work for current thread.
     *
     * @param threadName name of current thread
     * @param actualSize size of work done for current thread
     */
    void setActual(String threadName, Long actualSize);

    /**
     * Increments actual size of work for current thread and size of full work done.
     *
     * @param threadName name of current thread
     * @param size       count of bytes which need to increase actual size on
     */
    void increaseActual(String threadName, long size);

    /**
     * Returns string with detail statistic of work for all threads.
     *
     * @return string with statistic for all threads
     */
    String get();

    /**
     * Returns delayed a statistic info.
     *
     * @param timeout for wait
     * @return string with statistic for all threads
     */
    String getInfoByTimer(final long timeout);
}
