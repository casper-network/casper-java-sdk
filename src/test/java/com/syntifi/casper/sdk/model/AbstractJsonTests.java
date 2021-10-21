package com.syntifi.casper.sdk.model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.casper.sdk.service.CasperObjectMapper;

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
     * @param filename
     * @return
     * @throws IOException
     */
    protected String loadJsonFromFile(String filename) throws IOException {
        String fileJson;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            fileJson = new String(is.readAllBytes());
        }
        return fileJson;
    }

    /**
     * Prettifies json for assertion consistency
     * 
     * @param json json string to prettify
     * @return prettified json
     * @throws JsonMappingException
     * @throws JsonProcessingException
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
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    protected String getPrettyJson(Object jsonObject) throws JsonMappingException, JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
}
