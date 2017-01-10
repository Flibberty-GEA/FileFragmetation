package com.sysgears.statistic;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * Check a statistic info and a timeout.
 *
 * @author yevgen
 */
public class StatisticTestIT {

    StatisticInfoValidator validator;
    StatisticService statisticService;

    @BeforeTest(groups = {"all-tests"})
    public void beforTestStatisticInfo() {
        validator = new StatisticInfoValidator();
        statisticService = new StatisticServiceImpl();
        statisticService.setFullExpectedSize(1000000);
        statisticService.setExpected("pool-1-thread-1", 2000L);
        statisticService.setActual("pool-1-thread-1", 0L);
        statisticService.increaseActual("pool-1-thread-1", 1000L);
        statisticService.setExpected("pool-1-thread-2", 2000L);
        statisticService.setActual("pool-1-thread-2", 0L);
        statisticService.increaseActual("pool-1-thread-2", 1000L);
    }

    /**
     * Check a statistic info for StatisticService.get().
     */
    @Test(groups = {"all-tests"})
    public void testStatisticInfo() {
        Assert.assertTrue(validator.validate(statisticService.get()));
    }

    @DataProvider
    public Object[][] timeout() {
        return new Object[][]{
                {1000L},
                {2000L},
                {100L}
        };
    }

    /**
     * Check a timeout for StatisticService.getInfoByTimer(timeout).
     */
    @Test(dataProvider = "timeout", groups = {"all-tests"})
    public void testTimeout(final long timeout) {
        final long start = System.currentTimeMillis();
        statisticService.getInfoByTimer(timeout);
        final long finish = System.currentTimeMillis();
        double timeConsumedMillis = finish - start;
        final long correctTimeout;
        if (timeout < StatisticService.STATISTIC_SHOW_TIMEOUT) correctTimeout = StatisticService.STATISTIC_SHOW_TIMEOUT;
        else correctTimeout = timeout;

        final double maxError = 1d;
        final double realError = ((timeConsumedMillis - correctTimeout) * 100) / correctTimeout;
        Assert.assertTrue(realError <= maxError);
    }

    /**
     * Inner class for validating statistic info.
     */
    class StatisticInfoValidator {
        private static final String STATISTIC_PATTERN = "Total progress: (100|[1-9]?[0-9]{1})%, " +
                "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
                "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
                "time remaining: \\d+\\.\\d+s\\.";
        private final Pattern pattern;

        public StatisticInfoValidator() {
            pattern = Pattern.compile(STATISTIC_PATTERN);
        }

        /**
         * Validating statistic info.
         *
         * @param statisticInfo string for checking
         * @return true if statisticInfo like as STATISTIC_PATTERN
         */
        public boolean validate(String statisticInfo) {
            return pattern.matcher(statisticInfo).matches();
        }
    }
}
