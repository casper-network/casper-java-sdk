package com.casper.sdk.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.casper.sdk.service.CasperObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract class for testing json mappings from sample files
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractJsonTests {

    protected static final ObjectMapper OBJECT_MAPPER = new CasperObjectMapper();

    /**
     * Loads test json from resources
     * 
     * @param filename the file to load
     * @return the file content as String
     * @throws IOException thrown if error reading/accessing file
     */
    protected String loadJsonFromFile(String filename) throws IOException {
        String fileJson;

        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while (true) {
                    assert is != null;
                    if ((readLen = is.read(buf, 0, bufLen)) == -1) break;
                    outputStream.write(buf, 0, readLen);
                }

                fileJson = outputStream.toString();
            }
        }
        return fileJson;
    }

    /**
     * Prettifies json for assertion consistency
     * 
     * @param json json string to prettify
     * @return prettified json
     * @throws JsonMappingException thrown if a mapping error occurs
     * @throws JsonProcessingException thrown if a json procesing error
     */
    protected String getPrettyJson(String json) throws JsonMappingException, JsonProcessingException {
        Object jsonObject = OBJECT_MAPPER.readValue(json, Object.class);
        return getPrettyJson(jsonObject);
    }

    /**
     * Prettifies json for assertion consistency
     * 
     * @param jsonObject object to serialize and prettify
     * @return prettified json
     * @throws JsonMappingException thrown if a mapping error occurs
     * @throws JsonProcessingException thrown if a json procesing error
     */
    protected String getPrettyJson(Object jsonObject) throws JsonMappingException, JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
}
