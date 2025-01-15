package com.casper.sdk.model.key;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A key for an account hash
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Getter
@Setter
public class AccountHashKey extends Key {

    public AccountHashKey(final String strKey) {
        setTag(KeyTag.ACCOUNT);
        fromStringCustom(strKey);
    }
}
