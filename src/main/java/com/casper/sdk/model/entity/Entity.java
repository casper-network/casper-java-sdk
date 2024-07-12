package com.casper.sdk.model.entity;

import com.casper.sdk.model.account.ActionThresholds;
import com.casper.sdk.model.account.AssociatedKey;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Methods and type signatures supported by a contract.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    /** Casper Platform protocol version */
    @JsonProperty("protocol_version")
    private String protocolVersion;

    /** The type of Package. */
    @JsonProperty("entity_kind")
    private EntityAddressKind entityAddressKind;

    /** A Package in the global state. */
    @JsonProperty("package_hash")
    private String packageHash;

    /** HashAddr which is the raw bytes of the ByteCodeHash */
    @JsonProperty("byte_code_hash")
    private String byteCodeHash;

    /** Purse address */
    @JsonProperty("main_purse")
    private URef mainPurse;

    /** A collection of weighted public keys (represented as account hashes) associated with an account. */
    @JsonProperty("associated_keys")
    private List<AssociatedKey> associatedKeys;

    /** Thresholds that have to be met when executing an action of a certain type. */
    @JsonProperty("action_thresholds")
    private ActionThresholds actionThresholds;

    /** Collection of named message topics. */
    @JsonProperty("message_topics")
    private List<MessageTopic> messageTopics;

}
