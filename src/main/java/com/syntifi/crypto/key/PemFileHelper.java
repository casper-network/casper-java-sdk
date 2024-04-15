package com.syntifi.crypto.key;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Helper methods for dealing with PEM files
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PemFileHelper {
    /**
     * Reads a PEM file
     *
     * @param keyReader the reader to load the key from
     * @return a byte array with file content
     * @throws IOException thrown if error reading file
     */
    public static byte[] readPemFile(final Reader keyReader) throws IOException {
        try (final PemReader pemReader = new PemReader(keyReader)) {
            final PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        }
    }

    /**
     * Writes a PEM file
     *
     * @param fileWriter the writer of the key
     * @param encodedKey the encoded key
     * @param keyType    the key type
     * @throws IOException thrown if error writing file
     */
    public static void writePemFile(final Writer fileWriter, final byte[] encodedKey, final String keyType) throws IOException {
        try (final PemWriter pemWriter = new PemWriter(fileWriter)) {
            pemWriter.writeObject(new PemObject(keyType, encodedKey));
        }
    }
}
