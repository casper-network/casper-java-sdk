package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

abstract class AbstractSigner implements Signer {

    private final SignatureAlgorithm algorithm;

    AbstractSigner(final SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return algorithm;
    }

    byte[] readPemFile(final InputStream keyStream) {

        final PemReader pemReader = new PemReader(new InputStreamReader(keyStream));
        final PemObject pemObject;
        try {
            pemObject = pemReader.readPemObject();
        } catch (IOException e) {
            throw new SignatureException(e);
        }

        return pemObject.getContent();
    }
}
