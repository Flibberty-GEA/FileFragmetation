package com.sysgears.fragmentation;

import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yevgen
 */
public class SplitterTest {
    StatisticService statistic;
    Splitter splitter;
    File file;
    long maxPartSize;
    String testFileName;
    String prefix;
    int count;

    @BeforeTest(groups = { "all-tests" })
    public void exitMock() {
        splitter = new Splitter(Executors.newFixedThreadPool(2));
        statistic = new StatisticServiceImpl();
        testFileName = "/home/yevgen/IdeaProjects/FileFragmetation/fragmentation/src/test/resources/file.bmp";
        prefix = "_part_";
        file = new File(testFileName);
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][]{
//                {2000000L},
                {1000000L}
        };
    }

    @Test(dataProvider = "data", groups = { "all-tests" })
    public void testSplitter(final long partSize){
        maxPartSize = partSize;
        splitter.split(statistic, file, maxPartSize);
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        File firstFilePart = new File(testFileName+prefix+0);
        Assert.assertTrue(firstFilePart.isFile());

        count = (int) (file.length()/maxPartSize);
        File secondFilePart = new File(testFileName+prefix+count);
        Assert.assertTrue(secondFilePart.isFile());
        File wrongFilePart = new File(testFileName+prefix+(count+1));
        Assert.assertFalse(wrongFilePart.isFile());
    }
}
