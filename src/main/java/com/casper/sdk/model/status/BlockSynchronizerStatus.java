package com.casper.sdk.model.status;

import lombok.*;

/**
 * The status of the block synchronizer builders
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockSynchronizerStatus {

    /**
     * The status of syncing a historical block, if any
     */
    private BlockSyncStatus historical;

    /**
     * The status of syncing a forward block, if any
     */
    private BlockSyncStatus forward;
}
