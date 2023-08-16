package com.casper.sdk.e2e.matcher;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.deployaccepted.DeployAccepted;
import org.hamcrest.CustomMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ian@meywood.com
 */
public class DeployMatchers {


    private static final Logger logger = LoggerFactory.getLogger(DeployMatchers.class);

    public static ExpiringMatcher<Event<DeployAccepted>> theDeployIsAccepted(final String deployHash,
                                                                             final OnMatch<Event<DeployAccepted>> onMatch) {

        return new ExpiringMatcher<>(new CustomMatcher<Event<DeployAccepted>>("The deploy is accepted") {
            @Override
            public boolean matches(Object actual) {
                if (actual instanceof Event && ((Event<?>) actual).getDataType() == DataType.DEPLOY_ACCEPTED) {

                    if (deployHash.equals(((DeployAccepted) ((Event<?>) actual).getData()).getDeploy().getHash().toString())) {
                        logger.info("Matching deploy accepted for {}", deployHash);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
