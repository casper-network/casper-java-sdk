package com.casper.sdk.e2e.matcher;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.step.Step;
import org.hamcrest.CustomMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matches an era end Step event
 */
public class EraMatcher {
    private static final Logger logger = LoggerFactory.getLogger(EraMatcher.class);

    public static ExpiringMatcher<Event<Step>> theEraHasChanged() {
        return new ExpiringMatcher<>(new CustomMatcher("A step event is encountered") {
            @Override
            public boolean matches(final Object actual) {
                if (actual instanceof Event && ((Event<?>) actual).getDataType() == DataType.STEP) {
                    logger.info("Step event encountered");
                    return true;
                }
                return false;
            }
        });
    }

}
