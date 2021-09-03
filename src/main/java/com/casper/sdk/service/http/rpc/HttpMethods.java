package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.exceptions.HttpException;
import com.casper.sdk.service.json.JsonConversionService;
import okhttp3.*;

import java.util.Optional;

@SuppressWarnings("ALL")
/**
 * All used HTTP methods
 */
public class HttpMethods {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public final OkHttpClient client = new OkHttpClient();
    private final JsonConversionService jsonConversionService;

    public HttpMethods(final JsonConversionService jsonConversionService) {
        this.jsonConversionService = jsonConversionService;
    }

    public Optional<String> rpcCallMethod(final Method method) throws HttpException {

        try {
            final RequestBody body = RequestBody.create(JSON, jsonConversionService.writeValueAsString(method));

            final Request request = new Request.Builder()
                    .url(buildUrl())
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-type", JSON.toString())
                    .build();

            return Optional.ofNullable(client.newCall(request).execute().body().string());

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }
    }

    private String buildUrl() {
        return new StringBuilder(Properties.properties.get("node-url"))
                .append(":")
                .append(Properties.properties.get("node-port"))
                .append("/rpc")
                .toString();
    }
}
