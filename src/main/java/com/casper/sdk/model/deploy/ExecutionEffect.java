package com.casper.sdk.model.deploy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
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
