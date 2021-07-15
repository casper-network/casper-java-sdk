package com.casper.sdk;

import org.junit.Test;
import com.casper.sdk.service.TransferService;

import java.io.IOException;

public class TestTransfer {

    private final TransferService transfer = new TransferService();


    @Test
    public void testTransferJson() throws IOException {

        String s = transfer.buildJson();


    }



}
