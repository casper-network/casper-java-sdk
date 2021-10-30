package com.casper.sdk.service;

import com.casper.sdk.Properties;
import com.casper.sdk.exceptions.HttpException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Optional;

public class MetricsService {

    public final OkHttpClient client = new OkHttpClient();

    public MetricsService(){

    }

    public String getMetrics() throws Exception {

        Optional<String> result = callGetMethod("metrics");
        return result.orElse(null);

    }

    private Optional<String> callGetMethod(final String urlPath) throws HttpException {

        try{
            final Request request = new Request.Builder()
                    .url(buildUrl(urlPath))
                    .header("Accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String string = response.body().string();
            return Optional.ofNullable(string);

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

    }

    private String buildUrl(String urlPath) {
        return new StringBuilder(Properties.properties.get("node-url"))
                .append(":")
                .append(Properties.properties.get("node-port"))
                .append("/")
                .append(urlPath)
                .toString();
    }

}
