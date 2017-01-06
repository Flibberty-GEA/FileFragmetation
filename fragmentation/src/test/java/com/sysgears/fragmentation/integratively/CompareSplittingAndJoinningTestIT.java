package com.sysgears.fragmentation.integratively;

import com.sysgears.fragmentation.Joiner;
import com.sysgears.fragmentation.Splitter;
import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yevgen
 */
public class CompareSplittingAndJoinningTestIT {
    ExecutorService service = Executors.newFixedThreadPool(2);
    StatisticService statistic = new StatisticServiceImpl();
    Splitter splitter = new Splitter(service);
    Joiner joiner = new Joiner(service);
    FileUtil fileUtil = new FileUtil();


    File exampleFile = fileUtil.createFile("original.txt", 37472054);
    File copyFile = fileUtil.copyFile(exampleFile, "copy.txt");
    File wrongFile = fileUtil.createFile("wrong.txt", 1000);

    String testFileName = copyFile.getPath();
    String prefix = "_part_";
    String wrongFileName = wrongFile.getPath();

    long maxPartSize = 1000000L;
    int count = (int) (copyFile.length()/maxPartSize);

    @Test(groups = { "all-tests" })
    public void splitBeforeJoining(){

        Assert.assertTrue(exampleFile.length() == copyFile.length());
        Assert.assertFalse(exampleFile.equals(copyFile));
        Assert.assertTrue(fileUtil.compareFiles(exampleFile, copyFile));

        splitter.split(statistic, this.copyFile, maxPartSize);
        while (((ThreadPoolExecutor) service).getActiveCount() > 0) {
        }
//        try {
//            TimeUnit.MILLISECONDS.sleep(3000);
//        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
//        }
        File firstFilePart = new File(copyFile.getPath()+prefix+0);
//         проверка наличия первого фрагмента файла
        Assert.assertTrue(firstFilePart.isFile());
        File lastFilePart = new File(copyFile.getPath()+prefix+count);
//          проверка наличия крайнего фрагмента файла
        Assert.assertTrue(lastFilePart.isFile());
        File wrongFilePart = new File(copyFile.getPath()+prefix+(count+1));
//          проверка отсутствия лишнего файла
        Assert.assertFalse(wrongFilePart.isFile());
        File currentFile;
        int resultSize = 0;
        for (int index = 0; index <=count; index++ ){
            currentFile = new File(copyFile.getPath()+prefix+index);
            resultSize += currentFile.length();
        }
//         сравнить размер исходного файла и сумму размеров его фрагментов
        Assert.assertEquals(resultSize, copyFile.length());
        this.copyFile.delete();
    }

    @Test(groups = { "all-tests" }, dependsOnMethods={"splitBeforeJoining"})
    public void testMain(){
        while (((ThreadPoolExecutor) service).getActiveCount() > 0) {
        }
        joiner.join(statistic, new File(testFileName+prefix+0));
        while (((ThreadPoolExecutor) service).getActiveCount() > 0) {
        }

        File firstFile = exampleFile;
        File secondFile = new File(testFileName);
        File wrongFile = new File(wrongFileName);

        Assert.assertFalse(firstFile.equals(secondFile));
        Assert.assertTrue(fileUtil.compareFiles(firstFile, secondFile));
        Assert.assertFalse(fileUtil.compareFiles(firstFile, wrongFile));

        firstFile.delete();
        secondFile.delete();
    }

    @AfterTest(groups = { "all-tests" })
    public void deleteFiles(){
        wrongFile.delete();
        File deleteFile;
        for (int index = 0; index <=count; index++ ){
            deleteFile = new File(testFileName+prefix+index);
            deleteFile.delete();
        }
    }
}
