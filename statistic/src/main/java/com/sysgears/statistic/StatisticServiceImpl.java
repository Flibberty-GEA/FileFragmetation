package com.sysgears.statistic;

import com.sysgears.statistic.repository.StatisticRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Service for storages and accesses to statistical data of work for all threads.
 */
public class StatisticServiceImpl implements StatisticService {

    public static final Logger log = LogManager.getLogger(StatisticServiceImpl.class);

    private final StatisticRepository repository = new StatisticRepository();

    public StatisticServiceImpl() {
        log.debug("Initialize without params.");
    }

    /**
     * Sets expected size.
     *
     * @param expectedSize full size of work
     */
    @Override
    public void setFullExpectedSize(long expectedSize) {
        repository.setFullExpectedSize(expectedSize);
    }

    /**
     * Sets expected size of work for current thread.
     *
     * @param threadName   name of current thread
     * @param expectedSize full size of work for current thread
     */
    @Override
    public synchronized void setExpected(final String threadName, final Long expectedSize) {
        log.trace("StatisticServiceImpl.setExpected() starts with params: " +
                "thread name \"" + threadName + "\"; expected size = " + expectedSize + "bytes.");
        repository.setExpected(threadName, expectedSize);
    }

    /**
     * Sets actual size of work for current thread.
     *
     * @param threadName name of current thread
     * @param actualSize size of work done for current thread
     */
    @Override
    public synchronized void setActual(final String threadName, final Long actualSize) {
        log.trace("StatisticServiceImpl.setActual() starts with params: " +
                "thread name \"" + threadName + "\"; actual size = " + actualSize + "bytes.");
        repository.setActual(threadName, actualSize);
    }

    /**
     * Increments actual size of work for current thread.
     *
     * @param threadName name of current thread
     * @param size       count of bytes which need to increase actual size on
     */
    @Override
    public synchronized void increaseActual(final String threadName, final long size) {
        log.trace("StatisticServiceImpl.increaseActual() starts with params: " +
                "thread name \"" + threadName + "\"; " + "count to increase size on = " + size + "bytes.");
        repository.increaseActual(threadName, size);
    }

    /**
     * Returns string with detail statistic of work for all threads.
     *
     * @return string with statistic for all threads
     */
    @Override
    public synchronized String get() {

        log.debug("call to method get()");

        /* Calculate total percent of work done. */
        final long totalPercent = 100 * repository.getActualSize() / repository.getExpectedSize();

        /* Initialize beginning of the statistic string and append info about total progress. */
        final StringBuilder statistic = new StringBuilder("Total progress: ").append(totalPercent).append("%, ");

        /* Iterate by all threads. */
        for (String threadName : repository.getActualByThread().keySet()) {

            /* Calculate percent for current thread. */
            long threadPercent = 100 * repository.getActualByThread().get(threadName) /
                    repository.getExpectedByThread().get(threadName);

            /* Append percent for current thread into statistic string. */
            statistic.append(threadName).append(": ").append(threadPercent).append("%, ");
        }

        /* Calculate how long the statistic work. */
        final long timeDiffMs = new Date().getTime() - repository.getStart().getTime();

        /* Calculate how many time needs to finish full work. */
        final double timeRemainingSec = Math.round(((double) timeDiffMs / repository.getActualSize()) *
                (repository.getExpectedSize() - repository.getActualSize()) / 1000);

        /* Append time remaining into statistic string. */
        statistic.append("time remaining: ").append(timeRemainingSec).append("s.");

        log.debug("Return string with statistic for all threads:\"" + statistic.toString() + "\'");

        return statistic.toString();
    }

    /**
     * Returns delayed a statistic info.
     *
     * @param timeout for wait
     * @return string with statistic for all threads
     */
    @Override
    public String getInfoByTimer(final long timeout) {
        final long correctTimeout;
        if(timeout<STATISTIC_SHOW_TIMEOUT) correctTimeout = STATISTIC_SHOW_TIMEOUT;
        else correctTimeout=timeout;
        try {
            TimeUnit.MILLISECONDS.sleep(correctTimeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted, closing. ", e);
        }
        String statisticMessage = this.get();
        return statisticMessage;
    }
}
