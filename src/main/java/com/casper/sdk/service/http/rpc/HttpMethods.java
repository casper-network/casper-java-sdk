package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.exceptions.HttpException;
import com.casper.sdk.service.json.JsonConversionService;
import okhttp3.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;


/**
 * All used HTTP methods
 */
public class HttpMethods {

    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-type";
    private static final MediaType JSON = MediaType.get(APPLICATION_JSON);
    public static final String RPC = "rpc";
    private final OkHttpClient client = new OkHttpClient();
    private final JsonConversionService jsonConversionService;

    public HttpMethods(final JsonConversionService jsonConversionService) {
        this.jsonConversionService = jsonConversionService;
    }

    Optional<String> rpcCallMethod(final Method method) throws HttpException {

        try {

            final String content = jsonConversionService.writeValueAsString(method);
            final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            final RequestBody body = RequestBody.create(bytes, JSON);

            final Request request = new Request.Builder()
                    .url(buildRpcUrl())
                    .header(ACCEPT, APPLICATION_JSON)
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .post(body)
                    .build();

            final Response response = client.newCall(request).execute();
            //noinspection ConstantConditions
            return Optional.ofNullable(response.body().string());

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }
    }

    public Optional<String> callGetMethod(final String urlPath) {

        try {
            final Request request = new Request.Builder()
                    .url(buildUrl(urlPath))
                    .header(ACCEPT, APPLICATION_JSON)
                    .get()
                    .build();

            final Response response = client.newCall(request).execute();
            //noinspection ConstantConditions
            return Optional.ofNullable(response.body().string());

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }
    }

    private String buildUrl(final String urlPath) {
        return Properties.properties.get(Properties.NODE_URL) +
               ':' +
               Properties.properties.get(Properties.NODE_PORT) +
               '/' +
               urlPath;
    }

    private String buildRpcUrl() {
        return buildUrl(RPC);
    }
}
