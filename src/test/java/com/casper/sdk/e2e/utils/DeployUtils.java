package com.casper.sdk.e2e.utils;

import com.casper.sdk.e2e.exception.TimeoutException;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.service.CasperService;

/**
 * @author ian@meywood.com
 */
public class DeployUtils {
    public static DeployData waitForDeploy(final String deployHash,
                                           final int timeoutSeconds,
                                           final CasperService casperService) {

        final long timeout = timeoutSeconds * 1000L;
        final long now = System.currentTimeMillis();

        DeployData deploy = null;

        while (deploy == null || deploy.getExecutionResults().isEmpty()) {

            deploy = casperService.getDeploy(deployHash);
            if (deploy.getExecutionResults().isEmpty() && System.currentTimeMillis() > now + timeout) {
                throw new TimeoutException("Timed-out waiting for deploy " + deployHash);
            }

            try {
                //noinspection BusyWait
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return deploy;
    }
}
