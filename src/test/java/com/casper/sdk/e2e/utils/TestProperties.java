package com.casper.sdk.e2e.utils;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ian@meywood.com
 */
@Getter
public class TestProperties {

    private final Logger logger = LoggerFactory.getLogger(TestProperties.class);
    private final String hostname;
    private final String dockerName;
    private final int rcpPort;
    private final int restPort;
    private final int ssePort;

    public TestProperties() {

        this.hostname = getProperty("cspr.hostname", "localhost");
        this.dockerName = getProperty("cspr.docker.name", "cspr-nctl");
        this.rcpPort = getIntProperty("cspr.port.rcp", 11101);
        this.restPort = getIntProperty("cspr.port.rest", 14101);
        this.ssePort = getIntProperty("cspr.port.sse", 18101);
    }

    private String getProperty(final String name, final String defaultValue) {
        final String property = System.getProperty(name);
        logger.info("{} = {}", name, property);
        return property != null ? property : defaultValue;
    }

    private int getIntProperty(final String name, final int defaultValue) {
        final String property = getProperty(name, null);
        logger.info("{} = {}", name, property);
        return property != null ? Integer.parseInt(property) : defaultValue;
    }
}
