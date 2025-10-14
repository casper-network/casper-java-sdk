package com.howto;

import com.casper.sdk.model.transaction.GetTransactionResult;
import com.casper.sdk.model.transaction.TransactionHash;
import com.casper.sdk.service.CasperService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class HowToMethods {

    private CasperService casperService;

    public CasperService connect() throws IOException, URISyntaxException {
//        casperService = CasperService.usingPeer("localhost", 21101);

        final String authTokenPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/auth.token")).toURI()).toString();

        final String authToken = Files.lines(Paths.get(authTokenPath), StandardCharsets.UTF_8)
            .collect(Collectors.toList()).get(0);

        return CasperService.usingPeer(new URL("https://node.testnet.cspr.cloud/rpc"),
            new HashMap<String, String>() {{
                put("Authorization", authToken);
            }});
    }

    GetTransactionResult waitForTransaction(final TransactionHash hash) throws TimeoutException {

        final long timeout = 300 * 1000L;
        final long now = System.currentTimeMillis();

        GetTransactionResult result = null;

        while (result == null || result.getExecutionInfo() == null) {

            result = casperService.getTransaction(hash);

            if (result.getExecutionInfo() != null && System.currentTimeMillis() > now + timeout) {
                throw new TimeoutException("Timed-out waiting for transaction deploy " + hash);
            }

            try {
                //noinspection BusyWait
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

}
