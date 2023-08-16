package com.casper.sdk.e2e.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

class ConsoleStream implements Runnable {
    private final InputStream inputStream;
    private final Consumer<String> consumer;

    public ConsoleStream(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .forEach(consumer);
    }
}
