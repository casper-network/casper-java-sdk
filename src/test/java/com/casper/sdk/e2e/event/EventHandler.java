package com.casper.sdk.e2e.event;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.e2e.matcher.MatcherMap;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Helper class for consuming and matching events.
 *
 * @author ian@meywood.com
 */
public class EventHandler {

    private final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final MatcherMap matcherMap = new MatcherMap();
    private final List<AutoCloseable> sseSources = new ArrayList<>();

    public EventHandler(final EventTarget eventTarget) {
        consume(EventType.DEPLOYS, eventTarget);
        consume(EventType.MAIN, eventTarget);
        consume(EventType.SIGS, eventTarget);
    }

    public void close() {

        for (AutoCloseable sseSource : sseSources) {
            try {
                sseSource.close();
            } catch (Exception e) {
                logger.error("Error closing SSE Source", e);
            }
        }
    }

    private void consume(final EventType eventType, final EventTarget eventTarget) {

        sseSources.add(
                CasperClientProvider.getInstance().getEventService().consumeEvents(eventType, eventTarget, null, event -> {
//                    logger.info("Got {} event {}", eventType, event);
                    handleMatchers(event);
                }, throwable -> logger.error("Error processing SSE event", throwable))
        );
    }

    public <T> Matcher<T> addEventMatcher(final EventType eventType, final Matcher<T> matcher) {
        matcherMap.addEventMatcher(eventType, matcher);
        return matcher;
    }

    private void handleMatchers(Event<?> event) {
        matcherMap.handleEvent(event);
    }

    public <T> void removeEventMatcher(final EventType eventType, final Matcher<T> matcher) {
        matcherMap.removeEventMatcher(eventType, matcher);
    }
}
