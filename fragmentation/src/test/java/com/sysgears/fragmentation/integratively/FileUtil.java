package com.sysgears.fragmentation.integratively;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;

/**
 * @author yevgen
 */
public class FileUtil {
    //    final String fileSeparator = (String)System.getProperty("file.separator");
    final String clazzPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    final String modulePath = clazzPath + "../../";
    final String linuxResourcesPath = modulePath + "src/test/resources/";
    final String absoluteResorsePath = clazzPath.contains("\\") ? linuxResourcesPath.replace("/", "\\\\") : linuxResourcesPath;

    public boolean compareFilesByHash(final File first, final File second) {
        if (!compareFileSize(first, second)) return false;
        String firstHash = getFileHash(first.getPath());
        String secondHash = getFileHash(second.getPath());
        return compareHash(firstHash, secondHash);
    }

    public String getFileHash(String fileName) {
        String result = null;
        try {
            result = DigestUtils.md5Hex(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean compareHash(final String first, final String second){
        return first.equals(second);
    }

    public boolean compareFileSize(final File first, final File second) {
        boolean result = false;
        if (first.length() == second.length()) result = true;
        return result;
    }

    public File copyFile(final File inputFile, final String newFileName) {
        File outputFile = new File(absoluteResorsePath + newFileName);
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
