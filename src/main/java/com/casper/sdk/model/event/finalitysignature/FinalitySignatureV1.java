package com.casper.sdk.model.event.finalitysignature;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A validator's signature of a block, to confirm it is finalized. Clients and joining nodes should wait until the
 * signers' combined weight exceeds their fault tolerance threshold before accepting the block as finalized.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
//@NoArgsConstructor(force = true)
@JsonTypeName("V1")
public class FinalitySignatureV1 extends FinalitySignature {

}
