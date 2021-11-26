package com.syntifi.casper.sdk.crypto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
}
