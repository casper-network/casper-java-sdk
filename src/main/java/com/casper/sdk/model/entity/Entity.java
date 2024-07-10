package com.casper.sdk.model.entity;

import com.casper.sdk.model.account.ActionThresholds;
import com.casper.sdk.model.account.AssociatedKey;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    @JsonProperty("protocol_version")
    private String protocolVersion;

    @JsonProperty("entity_kind")
    private EntityAddressKind entityAddressKind;

    @JsonProperty("package_hash")
    private String packageHash;

    @JsonProperty("byte_code_hash")
    private String byteCodeHash;

    @JsonProperty("main_purse")
    private URef mainPurse;

    @JsonProperty("associated_keys")
    private List<AssociatedKey> associatedKeys;

    @JsonProperty("action_thresholds")
    private ActionThresholds actionThresholds;

    @JsonProperty("message_topics")
    private List<String> messageTopics;

}
