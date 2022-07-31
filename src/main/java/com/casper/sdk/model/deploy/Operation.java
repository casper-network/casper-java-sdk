package com.casper.sdk.model.deploy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An operation performed while executing a deploy.
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
public class Operation {

    /**
     * The formatted string of the `Key`
     */
    private String key;

    /**
     * @see OpKind
     */
    private OpKind kind;
}
