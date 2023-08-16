package com.casper.sdk.e2e.matcher;

/**
 * @author ian@meywood.com
 */
public interface OnMatch<T> {
    void onMatch(final T match);
}
