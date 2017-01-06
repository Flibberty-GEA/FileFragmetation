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
import java.util.concurrent.TimeUnit;

/**
 * @author yevgen
 */
public class CompareSplittingAndJoinningTestIT {
    CreateFile creator = new CreateFile();
    String prefix = "_part_";
    File fileOne = creator.createFile("fileOne.txt", 37472054);
    File fileTwo = creator.createFile("fileTwo.txt", 37472054);
    String testFileName = "/home/yevgen/IdeaProjects/FileFragmetation/fragmentation/src/test/resources/fileForJoining.bmp";
    String wrongFileName = "/home/yevgen/IdeaProjects/FileFragmetation/fragmentation/src/test/resources/wrongFile.bmp";
    File originFile = new File("/home/yevgen/IdeaProjects/FileFragmetation/fragmentation/src/test/resources/file.bmp");
    File deleteFile = new File(testFileName);
    ExecutorService service = Executors.newFixedThreadPool(2);
    StatisticService statistic = new StatisticServiceImpl();
    Splitter splitter = new Splitter(service);
    Joiner joiner = new Joiner(service);
    long maxPartSize = 1000000L;
    int count = (int) (deleteFile.length()/maxPartSize);

    @Test(groups = { "all-tests" })
    public void splitBeforeJoining(){
        File newFile = creator.copyFile(fileOne, "fileThree.txt");

        Assert.assertTrue(fileOne.isFile());
        Assert.assertTrue(fileOne.length() == fileTwo.length());
        Assert.assertFalse(fileOne.equals(newFile));
        Assert.assertTrue(compareFiles(fileOne, newFile));

        splitter.split(statistic, this.deleteFile, maxPartSize);
        while (((ThreadPoolExecutor) service).getActiveCount() > 0) {
        }
//        try {
//            TimeUnit.MILLISECONDS.sleep(3000);
//        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
//        }
        File firstFilePart = new File(testFileName+prefix+0);
//         проверка наличия первого фрагмента файла
        Assert.assertTrue(firstFilePart.isFile());

        File lastFilePart = new File(testFileName+prefix+count);
//          проверка наличия крайнего фрагмента файла
        Assert.assertTrue(lastFilePart.isFile());
        File wrongFilePart = new File(testFileName+prefix+(count+1));
//          проверка отсутствия лишнего файла
        Assert.assertFalse(wrongFilePart.isFile());
        File currentFile;
        int resultSize = 0;
        for (int index = 0; index <=count; index++ ){
            currentFile = new File(testFileName+prefix+index);
            resultSize += currentFile.length();
        }
//         сравнить размер исходного файла и сумму размеров его фрагментов
        Assert.assertEquals(resultSize, deleteFile.length());
        this.deleteFile.delete();
//        try {
//            TimeUnit.MILLISECONDS.sleep(3000);
//        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
//        }
    }

    @Test(groups = { "all-tests" }, dependsOnMethods={"splitBeforeJoining"})
    public void testMain(){
        joiner.join(statistic, new File(testFileName+prefix+0));
        while (((ThreadPoolExecutor) service).getActiveCount() > 0) {
        }
//        try {
//            TimeUnit.MILLISECONDS.sleep(3000);
//        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
//        }
        File firstFile = originFile;
        File secondFile = new File(testFileName);
        File wrongFile = new File(wrongFileName);

        Assert.assertFalse(firstFile.equals(secondFile));
        Assert.assertTrue(compareFiles(firstFile, secondFile));
        Assert.assertFalse(compareFiles(firstFile, wrongFile));
    }

    @AfterTest(groups = { "all-tests" })
    public void deleteFiles(){
        File deleteFile;
        for (int index = 0; index <=count; index++ ){
            deleteFile = new File(testFileName+prefix+index);
            deleteFile.delete();
        }
    }

    public boolean compareFiles(final File first, final File second){
        if (!compareFileSize(first, second)) return false;
        byte[] firstFileBytesArray = getBytesFromFile(first);
        byte[] secondFileBytesArray = getBytesFromFile(second);
        if (!compareBytesArray(firstFileBytesArray, secondFileBytesArray)) return false;
        else return true;
    }

    public static boolean compareFileSize(final File first, final File second){
        boolean result = false;
        if (first.length() == second.length()) result = true;
        return result;
    }

    public static boolean compareBytesArray(final byte[] first, final byte[] second){
        boolean result = true;
        if (first.length != second.length) return false;
        for (int index = 0; index<first.length; index++) {
            if (first[index]!=second[index]){
                return false;
            }
        }
        return result;
    }

    public static byte[] getBytesFromFile(File file){

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            return null;
        }

        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        try {
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            try {
                throw new IOException("Could not completely read originFile "+file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close the input stream and return bytes
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
