package com.casper.sdk.e2e.matcher;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;

/**
 * Matchers for comparing NCTL responses to SDK responses.
 *
 * @author ian@meywood.com
 */
public class NctlMatchers {

    /**
     * Matcher that verifies a merkel proof length matches that obtains from nctl.
     * <p>
     * nclt only provides the length in a text string in the JSON such as: "merkle_proof": "[2160 hex chars]",
     *
     * @param expectedNctlMerkelProof the response from NCTL as shown above
     * @return the request matcher for merkel proofs
     */
    @SuppressWarnings("rawtypes")
    public static Matcher<String> isValidMerkleProof(final String expectedNctlMerkelProof) {

        //noinspection unchecked
        return new CustomMatcher("Nctl Merkel proof match") {
            @Override
            public boolean matches(final Object actual) {
                if (actual instanceof String) {
                    return getExpectedLength(expectedNctlMerkelProof) == actual.toString().length();
                }
                return false;
            }

            private int getExpectedLength(final String nctlMerkelProof) {
                return Integer.parseInt(nctlMerkelProof.split(" ")[0].substring(1));
            }
        };
    }
}
