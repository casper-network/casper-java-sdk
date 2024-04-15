package com.syntifi.crypto.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.security.GeneralSecurityException;

/**
 * Abstract class for needed shared functionalities
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPrivateKey {

    private byte[] key;

    /**
     * Loads a private key from a byte array
     *
     * @param privateKey the private key bytes
     */
    public abstract void loadPrivateKey(final byte[] privateKey) throws IOException;

    /**
     * Reads the private key from a file
     *
     * @param filename the source filename
     * @throws IOException thrown if an error occurs reading the file
     */
    public final void readPrivateKey(final String filename) throws IOException {
        try (final Reader fileReader = new FileReader(filename)) {
            readPrivateKey(fileReader);
        }
    }

    /**
     * Reads the private key from a stream
     *
     * @param reader the source of the private key
     * @throws IOException thrown if an error occurs reading the file
     */
    public abstract void readPrivateKey(final Reader reader) throws IOException;

    /**
     * Writes the private key to a file
     *
     * @param filename the target filename
     * @throws IOException thrown if an error occurs writing the file
     */
    public final void writePrivateKey(final String filename) throws IOException {
        try (final Writer fileWriter = new FileWriter(filename)) {
            writePrivateKey(fileWriter);
        }
    }

    /**
     * Writes the private key to a file
     *
     * @param writer the target writer
     * @throws IOException thrown if an error occurs writing the file
     */
    public abstract void writePrivateKey(final Writer writer) throws IOException;

    /**
     * Signs a message with the loaded key
     *
     * @param message message to sign
     * @return signed message
     * @throws GeneralSecurityException thrown if an error occurs processing message or signature
     */
    public abstract byte[] sign(final byte[] message) throws GeneralSecurityException;

    /**
     * Derives the public key from the loaded private key
     *
     * @return the derived {@link AbstractPublicKey}
     */
    public abstract AbstractPublicKey derivePublicKey();
}
