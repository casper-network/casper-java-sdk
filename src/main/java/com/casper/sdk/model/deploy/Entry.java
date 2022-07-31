package com.casper.sdk.model.deploy;

import com.casper.sdk.model.deploy.transform.Transform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
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
