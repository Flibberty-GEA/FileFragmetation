package com.sysgears.fragmentation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class that reads and writes bytes into file.
 */
public class FileAccessService {
    public static final Logger log = LogManager.getLogger(FileAccessService.class);

    /**
     * Reads bytes from the source file into the buffer.
     *
     * @param originalFile      file from which needs to read bytes.
     * @param buffer     buffer for bytes read.
     * @param position index in source file from which needs to start reading.
     * @param limitBytesRead      count of bytes that needs to read.
     * @return count of bytes read.
     * @throws IOException if an I/O error occurs.
     */
    public int read(final File originalFile,
                    byte[] buffer,
                    final long position,
                    final int limitBytesRead) throws IOException {

        /* Lock file before reading. */
        synchronized (originalFile) {

            RandomAccessFile fileAccessor = new RandomAccessFile(originalFile, "r");

            /* Set the file-pointer position. */
            fileAccessor.seek(position);

            /* Read len bytes into the specified byte array. */
            int result = fileAccessor.read(buffer, 0, limitBytesRead);

            fileAccessor.close();

            return result;
        }
    }

    /**
     * Writes bytes from the buffer to the destination file.
     *
     * @param resultFile     file into which needs to write bytes.
     * @param buff     buffer with bytes that needs to write.
     * @param position index in destination file from which need to start writing.
     * @param len      count of bytes that needs to write.
     * @throws IOException if an I/O error occurs.
     */
    public void write(final File resultFile,
                      final byte[] buff,
                      final long position,
                      final int len) throws IOException {

        /* Lock file before writing. */
        synchronized (resultFile) {

            RandomAccessFile randomAccessFile = new RandomAccessFile(resultFile, "rw");

            /* Set the file-pointer position. */
            randomAccessFile.seek(position);

            /* Write len bytes from the specified byte array. */
            randomAccessFile.write(buff, 0, len);

            /* Close this random access file stream. */
            randomAccessFile.close();
        }
    }
}
