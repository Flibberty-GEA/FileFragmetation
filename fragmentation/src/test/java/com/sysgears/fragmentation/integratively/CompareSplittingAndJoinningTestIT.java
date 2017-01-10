package com.sysgears.fragmentation.integratively;

import com.sysgears.fragmentation.Joiner;
import com.sysgears.fragmentation.Splitter;
import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.util.Currency;
import java.util.concurrent.*;

/**
 * Compare results files after splitting and joining.
 *
 * @author yevgen
 */
public class CompareSplittingAndJoinningTestIT {
    ExecutorService service;
    StatisticService statistic = new StatisticServiceImpl();
    FileUtil fileUtil = new FileUtil();

    File originalFile = fileUtil.createFile("original.txt", 37472054);
    File copyFile = fileUtil.copyFile(originalFile, "copy.txt");
    File wrongFile = fileUtil.createFile("wrong.txt", 1000);
    File secondFile;

    String testFileName = copyFile.getPath();
    String prefix = "_part_";

    long maxPartSize = 1000000L;
    int count = (int) (copyFile.length() / maxPartSize);

    @BeforeMethod(groups = {"all-tests"})
    public void createExecutorService(){
        service = Executors.newFixedThreadPool(2);
    }

    /**
     * Is the splitting correct?
     */
    @Test(groups = {"all-tests"})
    public void splitBeforeJoining() {

        Assert.assertTrue(originalFile.length() == copyFile.length());
        Assert.assertFalse(originalFile.equals(copyFile));
        Assert.assertTrue(fileUtil.compareFilesByHash(originalFile, copyFile));

        new Splitter(service).split(statistic, this.copyFile, maxPartSize);
        service.shutdown();
        try {
            service.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File firstFilePart = new File(copyFile.getPath() + prefix + 0);
//         проверка наличия первого фрагмента файла
        Assert.assertTrue(firstFilePart.isFile());
        File lastFilePart = new File(copyFile.getPath() + prefix + count);
//          проверка наличия крайнего фрагмента файла
        Assert.assertTrue(lastFilePart.isFile());
        File wrongFilePart = new File(copyFile.getPath() + prefix + (count + 1));
//          проверка отсутствия лишнего файла
        Assert.assertFalse(wrongFilePart.isFile());
        File currentFile;
        int resultSize = 0;
        for (int index = 0; index <= count; index++) {
            currentFile = new File(copyFile.getPath() + prefix + index);
            resultSize += currentFile.length();
        }
//         сравнить размер исходного файла и сумму размеров его фрагментов
        Assert.assertEquals(resultSize, copyFile.length());
        this.copyFile.delete();
    }

    /**
     * Is the joining correct?
     *
     * Join has to joining all parts in the same file, which was splitting before.
     */
    @Test(groups = {"all-tests"}, dependsOnMethods = {"splitBeforeJoining"})
    public void joinAfterSplitting() {
        new Joiner(service).join(statistic, new File(testFileName + prefix + 0));

        service.shutdown();
        try {
            service.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        secondFile = new File(testFileName);

        Assert.assertFalse(originalFile.equals(secondFile));
        Assert.assertTrue(fileUtil.compareFilesByHash(originalFile, secondFile));
        Assert.assertFalse(fileUtil.compareFilesByHash(originalFile, wrongFile));
    }

    @AfterTest(groups = {"all-tests"})
    public void deleteFiles() {
        this.originalFile.delete();
        copyFile.delete();
        wrongFile.delete();
        File deleteFile;
        for (int index = 0; index <= count; index++) {
            deleteFile = new File(testFileName + prefix + index);
            deleteFile.delete();
        }
    }
}
