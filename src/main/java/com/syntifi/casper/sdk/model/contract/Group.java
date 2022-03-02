package com.syntifi.casper.sdk.model.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A (labelled) "user group". Each method of a versioned contract may be
 * associated with one or more user groups which are allowed to call it
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class Group {
    private String name;
}
