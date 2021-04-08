package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.exceptions.HttpException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@SuppressWarnings("ALL")
public class HttpMethods {

    private final Gson gson;

    public HttpMethods() {
        gson = new GsonBuilder().setLenient().create();
    }

    public Optional<String> rpcCallMethod(final Method method) throws HttpException {

        try{
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(
                            new StringBuilder().append(Properties.properties.get("node-url")).append(":")
                                    .append(Properties.properties.get("node-port")).append("/rpc").toString()))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(method)))
                    .build();

            final HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return Optional.ofNullable(response.body());

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

    }

}
