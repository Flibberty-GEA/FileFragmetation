package com.sysgears.statistic;

import com.sysgears.statistic.repository.StatisticRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Class that store the statistic of work for all threads.
 */
public class StatisticServiceImpl implements StatisticService {
    public static final Logger log = LogManager.getLogger(StatisticServiceImpl.class);

    private final StatisticRepository repository = new StatisticRepository();

    /**
     * Sets expected size.
     *
     * @param expectedSize full size of work.
     */
    public void setFullExpectedSize(long expectedSize) {
        repository.setFullExpectedSize(expectedSize);
    }

    /**
     * Sets expected size of work for current thread.
     *
     * @param threadName   name of current thread.
     * @param expectedSize full size of work for current thread.
     */
    public synchronized void setExpected(final String threadName, final Long expectedSize) {
        repository.setExpected(threadName, expectedSize);
    }

    /**
     * Sets actual size of work for current thread.
     *
     * @param threadName name of current thread.
     * @param actualSize size of work done for current thread.
     */
    public synchronized void setActual(final String threadName, final Long actualSize) {
        repository.setActual(threadName, actualSize);
    }

    /**
     * Increments actual size of work for current thread.
     *
     * @param threadName name of current thread.
     * @param size       count of bytes which need to increase actual size on.
     */
    public synchronized void increaseActual(final String threadName, final long size) {
        repository.increaseActual(threadName, size);
    }

    /**
     * Returns string with detail statistic of work for all threads.
     *
     * @return string with statistic for all threads.
     */
    public synchronized String get() {


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

        return statistic.toString();
    }
}
