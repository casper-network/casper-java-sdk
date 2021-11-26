package com.syntifi.casper.sdk.crypto;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PemFileHelper {
    public static byte[] readPemFile(String filename) throws IOException {
        try (FileReader keyReader = new FileReader(filename); PemReader pemReader = new PemReader(keyReader)) {
            PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        }
    }

    public static void writePemFile(String filename, byte[] encodedKey, String keyType) throws IOException {
        try (PemWriter pemWriter = new PemWriter(new FileWriter(filename))) {
            pemWriter.writeObject(new PemObject(keyType, encodedKey));
        }
    }
}
