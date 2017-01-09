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
 * @author yevgen
 */
public class CompareSplittingAndJoinningTestIT {
    ExecutorService service;
    StatisticService statistic = new StatisticServiceImpl();
    Splitter splitter;
    Joiner joiner;
    FileUtil fileUtil = new FileUtil();


    File originalFile = fileUtil.createFile("original.txt", 37472054);
    File copyFile = fileUtil.copyFile(originalFile, "copy.txt");
    File wrongFile = fileUtil.createFile("wrong.txt", 1000);
    File secondFile;

    String testFileName = copyFile.getPath();
    String prefix = "_part_";

    long maxPartSize = 1000000L;
    int count = (int) (copyFile.length() / maxPartSize);

    @BeforeMethod
    public void createExecutorService(){
        service = Executors.newFixedThreadPool(2);
    }

    @Test(groups = {"all-tests"})
    public void splitBeforeJoining() {

        splitter = new Splitter(service);

        Assert.assertTrue(originalFile.length() == copyFile.length());
        Assert.assertFalse(originalFile.equals(copyFile));
        Assert.assertTrue(fileUtil.compareFilesByHash(originalFile, copyFile));

        splitter.split(statistic, this.copyFile, maxPartSize);
        service.shutdown();
        try {
            service.awaitTermination(100, TimeUnit.SECONDS);
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

    @Test(groups = {"all-tests"}, dependsOnMethods = {"splitBeforeJoining"})
    public void testMain() {

        joiner = new Joiner(service);
        joiner.join(statistic, new File(testFileName + prefix + 0));

        service.shutdown();
        try {
            service.awaitTermination(100, TimeUnit.SECONDS);
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
//        new File(testFileName).delete();
    }
}
