package com.casper.sdk.e2e.matcher;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import org.hamcrest.CustomMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * Matchers for block added events
 *
 * @author ian@meywood.com
 */
public class BlockAddedMatchers {

    private static final Logger logger = LoggerFactory.getLogger(BlockAddedMatchers.class);

    public static ExpiringMatcher<Event<BlockAdded>> hasTransferHashWithin(final String expectedTransferHash,
                                                                           final OnMatch<Event<BlockAdded>> onMatch) {
        return new ExpiringMatcher<>(
                new CustomMatcher<Event<BlockAdded>>("transferHashes contains deployHash") {
                    @Override
                    public boolean matches(Object actual) {
                        if (actual instanceof Event && ((Event<?>) actual).getDataType() == DataType.BLOCK_ADDED) {
                            //noinspection unchecked
                            final Event<BlockAdded> event = (Event<BlockAdded>) actual;
                            if (event.getDataType() == DataType.BLOCK_ADDED) {
                                final BlockAdded blockAdded = event.getData();

                                final String deployHashes = blockAdded.getBlock().getBody().getDeployHashes()
                                        .stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(", "));

                                final String transferHashes = blockAdded.getBlock().getBody().getTransferHashes()
                                        .stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(", "));

                                logger.info("Block added deploy hashes: [{}], transfer hashes: [{}]", deployHashes, transferHashes);

                                if (transferHashes.contains(expectedTransferHash)) {
                                    if (onMatch != null) {
                                        onMatch.onMatch(event);
                                    }
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                }
        );
    }
}
