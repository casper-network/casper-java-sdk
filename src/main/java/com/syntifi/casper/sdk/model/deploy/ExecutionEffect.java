package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The effect of executing a single deploy
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class ExecutionEffect {

    /**
     * a list of {@link Operation}
     */
    private List<Operation> operations;

    /**
     * a list of {@link Entry}
     */
    private List<Entry> transforms;
}
