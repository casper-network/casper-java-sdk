package com.casper.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.casper.sdk.json.deserialize.DeserializerContext.clear;

/**
 * Service used to convert between CL domain objects to JSON
 */
public class JsonConversionService {

    private ObjectMapper mapper;

    public JsonConversionService() {
        this.mapper =  new ObjectMapper();
        final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer(prettyPrinter);
    }

    /**
     * Converts a Casper domain object ot a JSON string
     *
     * @param clObject the domain object to write
     * @return the JSON representation of the clObject
     * @throws IOException - on write error
     */
    public String toJson(final Object clObject) throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        toJson(clObject, out);
        return out.toString();
    }

    public String writeValueAsString(final Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    /**
     * Writes a Casper domain object ot a JSON string to an {@link OutputStream}
     *
     * @param clObject the domain object to write
     * @param out      the stream to write JSON representation of the clObject to
     * @throws IOException - on write error
     */
    public void toJson(final Object clObject, final OutputStream out) throws IOException {

        final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer(prettyPrinter).writeValue(out, clObject);
        out.close();
    }

    /**
     * Converts a JSON string to a casper domain object
     *
     * @param json the JSON to parse
     * @return the casper domain object parsed from the JSON
     * @throws IOException - on a read error
     */
    public <T> T fromJson(final String json, final Class<T> type) throws IOException {
        clear();
        return mapper.reader().readValue(json, type);
    }

    /**
     * Parses JSON from an input stream to create a casper domain object
     *
     * @param in the stream to of JSON to parse
     * @return the casper domain object parsed from the JSON
     * @throws IOException - on a read error
     */
    public <T> T fromJson(final InputStream in, final Class<T> type) throws IOException {
        clear();
        return mapper.reader().readValue(in, type);
    }
}
