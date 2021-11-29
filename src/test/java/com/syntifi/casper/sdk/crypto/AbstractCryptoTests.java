package com.syntifi.casper.sdk.crypto;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public abstract class AbstractCryptoTests {

    /**
     * Loads test key file from resources
     * 
     * @param filename
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    protected String getResourcesKeyPath(String filename) throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource(filename);

        String path = url.toURI().getPath();

        return path;
    }

    /**
     * Compare files
     * 
     * @param file1
     * @param file2
     * @throws IOException
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
