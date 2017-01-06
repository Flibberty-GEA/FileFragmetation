package com.sysgears.fragmentation.integratively;

import com.sysgears.fragmentation.Splitter;
import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yevgen
 */
public class SplittingTestIT {
//    StatisticService statistic;
//    Splitter splitter;
//    File file;
//    long maxPartSize;
//    String testFileName;
//    String prefix;
//    int count;
//
//    @BeforeTest(groups = { "all-tests" })
//    public void exitMock() {
//        splitter = new Splitter(Executors.newFixedThreadPool(2));
//        statistic = new StatisticServiceImpl();
//        testFileName = "/home/yevgen/IdeaProjects/FileFragmetation/fragmentation/src/test/resources/fileForSplitting.bmp";
//        prefix = "_part_";
//        file = new File(testFileName);
//    }
//
//    @DataProvider
//    public Object[][] data() {
//        return new Object[][]{
//                {2000000L},
//                {1000000L}
//        };
//    }
//
//    @Test(dataProvider = "data", groups = { "all-tests" })
//    public void testSplitter(final long partSize){
//        maxPartSize = partSize;
//        splitter.split(statistic, file, maxPartSize);
//        try {
//            TimeUnit.MILLISECONDS.sleep(3000);
//        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
//        }
//        File firstFilePart = new File(testFileName+prefix+0);
////         проверка наличия первого фрагмента файла
//        Assert.assertTrue(firstFilePart.isFile());
//
//        count = (int) (file.length()/maxPartSize);
//        File secondFilePart = new File(testFileName+prefix+count);
////          проверка наличия крайнего фрагмента файла
//        Assert.assertTrue(secondFilePart.isFile());
//        File wrongFilePart = new File(testFileName+prefix+(count+1));
////          проверка отсутствия лишнего файла
//        Assert.assertFalse(wrongFilePart.isFile());
//        File currentFile;
//        int resultSize = 0;
//        for (int index = 0; index <=count; index++ ){
//            currentFile = new File(testFileName+prefix+index);
//            resultSize += currentFile.length();
//        }
////        System.out.println(partSize+" "+resultSize+" "+originFile.length());
////         сравнить размер исходного файла и сумму размеров его фрагментов
//        Assert.assertEquals(resultSize, file.length());
//    }
//
//    @AfterMethod(groups = { "all-tests" })
//    public void deleteTestFile(){
//        File deleteFile;
//        for (int index = 0; index <=count; index++ ){
//            deleteFile = new File(testFileName+prefix+index);
//            deleteFile.delete();
//        }
//    }
}
