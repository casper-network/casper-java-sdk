package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.exceptions.HttpException;
import com.casper.sdk.service.json.JsonConversionService;
import okhttp3.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@SuppressWarnings("ALL")
/**
 * All used HTTP methods
 */
public class HttpMethods {

    private static final MediaType JSON = MediaType.get("application/json");
    public final OkHttpClient client = new OkHttpClient();
    private final JsonConversionService jsonConversionService;

    public HttpMethods(final JsonConversionService jsonConversionService) {
        this.jsonConversionService = jsonConversionService;
    }

    public Optional<String> rpcCallMethod(final Method method) throws HttpException {

        try {
            final String content = jsonConversionService.writeValueAsString(method);
            final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            final RequestBody body = RequestBody.create(JSON, bytes);

            final Request request = new Request.Builder()
                    .url(buildUrl())
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String string = response.body().string();
            return Optional.ofNullable(string);

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
