package com.syntifi.crypto.key;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

/**
 * Shared key testing functionality
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public abstract class AbstractCryptoTests {
    /**
     * Loads test key file from resources
     *
     * @param filename the file name
     * @return a string with file path from resources
     * @throws URISyntaxException thrown if it can't parse file url to URI for fetching the path
     */
    protected String getResourcesKeyPath(String filename) throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI()).toString();
    }

    /**
     * Compare text files
     *
     * @param file1 a file object
     * @param file2 another file object
     * @return true if matches, false otherwise
     * @throws IOException thrown if error reading files
     */
    protected boolean compareTextFiles(File file1, File file2) throws IOException {
        byte[] contentFile1 = Files.readAllBytes(file1.toPath());
        byte[] contentFile2 = Files.readAllBytes(file2.toPath());

        return Arrays.equals(contentFile1, contentFile2);
    }

    /**
     * @param file1 a file
     * @param file2 another file
     * @return true if matches, false otherwise
     * @throws IOException thrown if error reading files
     */
    protected boolean compareFiles(File file1, File file2) throws IOException {
        try (RandomAccessFile randomAccessFile1 = new RandomAccessFile(file1, "r");
             RandomAccessFile randomAccessFile2 = new RandomAccessFile(file2, "r")) {

            FileChannel ch1 = randomAccessFile1.getChannel();
            FileChannel ch2 = randomAccessFile2.getChannel();

            if (ch1.size() != ch2.size()) {
                return false;
            }

            long size = ch1.size();
            MappedByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
            MappedByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);

            return m1.equals(m2);
        }
    }
}
