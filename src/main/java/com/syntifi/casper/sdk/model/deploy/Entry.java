package com.syntifi.casper.sdk.model.deploy;

import com.syntifi.casper.sdk.model.deploy.transform.Transform;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A transformation performed while executing a deploy.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class Entry {

    /**
     * The formatted string of the `Key`
     */
    private String key;

    /**
     * @see Transform
     */
    private Transform transform;
}
