package com.casper.sdk.service.http.rpc;

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
    public static final String RPC = "rpc";
    private static final MediaType JSON = MediaType.get(APPLICATION_JSON);
    private final OkHttpClient client = new OkHttpClient();
    private final JsonConversionService jsonConversionService;
    private final String url;
    private final int port;

    public HttpMethods(final JsonConversionService jsonConversionService, final String url, int port) {
        this.jsonConversionService = jsonConversionService;
        this.url = url;
        this.port = port;
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
        return url + ':' + port + '/' + urlPath;
    }

    private String buildRpcUrl() {
        return buildUrl(RPC);
    }
}
