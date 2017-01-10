package com.sysgears.core;

import java.io.*;

/**
 * Create default file.
 *
 * @author yevgen
 */
public class FileCreator {
    final String clazzPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    final String modulePath = clazzPath + "../../";
    final String linuxResourcesPath = modulePath + "src/test/resources/";
    final String absoluteResorsePath = clazzPath.contains("\\") ? linuxResourcesPath.replace("/", "\\\\") : linuxResourcesPath;


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
}
