package com.syntifi.crypto.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
public abstract class AbstractPublicKey {

    private byte[] key;

    /**
     * Loads a public key from a byte array
     *
     * @param publicKey the public key bytes
     */
    public abstract void loadPublicKey(final byte[] publicKey) throws IOException;

    /**
     * Reads the public key from a file
     *
     * @param filename the source filename
     * @throws IOException thrown if an error occurs reading the file
     */
    public abstract void readPublicKey(final String filename) throws IOException;

    /**
     * Reads the public key from a file
     *
     * @param reader the source filename
     * @throws IOException thrown if an error occurs reading the file
     */
    public abstract void readPublicKey(final Reader reader) throws IOException;


    /**
     * Writes the public key to a file
     *
     * @param filename the target filename
     * @throws IOException thrown if an error occurs writing the file
     */
    public abstract void writePublicKey(final String filename) throws IOException;

    /**
     * Writes the public key to a file
     *
     * @param writer the target to write the public key
     * @throws IOException thrown if an error occurs writing the file
     */
    public abstract void writePublicKey(final Writer writer) throws IOException;

    /**
     * Verifies message with given signature
     *
     * @param message   the signed message
     * @param signature the signature to check against
     * @return true if matches, false otherwise
     * @throws GeneralSecurityException thrown if an error occurs processing message and signature
     */
    public abstract Boolean verify(final byte[] message, final byte[] signature) throws GeneralSecurityException;
}
