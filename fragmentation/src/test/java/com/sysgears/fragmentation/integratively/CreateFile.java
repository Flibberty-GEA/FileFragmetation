package com.sysgears.fragmentation.integratively;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;

/**
 * @author yevgen
 */
public class CreateFile {
    //    final String fileSeparator = (String)System.getProperty("file.separator");
    final String clazzPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    final String modulePath = clazzPath + "../../";
    final String linuxResourcesPath = modulePath + "src/test/resources/";
    final String absoluteResorsePath = clazzPath.contains("\\") ? linuxResourcesPath.replace("/", "\\\\") : linuxResourcesPath;



    public File copyFile(final File inputFile, final String newFileName) {
        File outputFile = new File(absoluteResorsePath+newFileName);
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    @Test
    public File createFile(final String fileName, final int fileSize) {

        File file = new File(absoluteResorsePath + fileName);
        try {
            byte[] data = new byte[fileSize];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (Math.random() * 2);
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.write(data);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        return file;
    }

    public String getAbsoluteResorsePath() {
        return absoluteResorsePath;
    }

}
