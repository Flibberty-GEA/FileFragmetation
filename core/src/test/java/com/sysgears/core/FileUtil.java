package com.sysgears.core;

import java.io.*;

/**
 * @author yevgen
 */
public class FileUtil {
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
