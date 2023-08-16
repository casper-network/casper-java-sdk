package com.casper.sdk.e2e.matcher;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventType;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Map of EventType to list of Matchers.
 *
 * @author ian@meywood.com
 */
public class MatcherMap {

    private final Map<EventType, List<Matcher<?>>> eventTypeListMap = new LinkedHashMap<>();

    public void addEventMatcher(final EventType eventType, Matcher<?> matcher) {
        getMatchers(eventType).add(matcher);
    }


    private List<Matcher<?>> getMatchers(final EventType eventType) {
        return eventTypeListMap.computeIfAbsent(eventType, k -> new ArrayList<>());
    }


    public void handleEvent(Event<?> event) {
        List<Matcher<?>> matchers = getMatchers(event.getEventType());

        matchers.forEach(matcher -> matcher.matches(event));
    }

    public void removeEventMatcher(EventType eventType, Matcher<?> matcher) {
        List<Matcher<?>> matchers = getMatchers(eventType);
        matchers.remove(matcher);
    }
}
