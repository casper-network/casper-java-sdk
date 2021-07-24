package com.casper.sdk.types;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class AccessRightsTest {

    @Test
    void accessRights() {
        assertThat(AccessRights.NONE.getBits(), is((byte) 0));
        assertThat(AccessRights.READ.getBits(), is((byte) 1));
        assertThat(AccessRights.WRITE.getBits(), is((byte) 2));
        assertThat(AccessRights.READ_WRITE.getBits(), is((byte) 3));
        assertThat(AccessRights.ADD.getBits(), is((byte) 4));
        assertThat(AccessRights.READ_ADD.getBits(), is((byte) 5));
        assertThat(AccessRights.ADD_WRITE.getBits(), is((byte) 6));
        assertThat(AccessRights.READ_ADD_WRITE.getBits(), is((byte) 7));
    }
}
