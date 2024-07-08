package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Message that was emitted by an addressable entity during execution.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {
    /** The identity of the entity that produced the message. */
    @JsonProperty("entity_hash")
    private String entityAddr;
    @JsonProperty("message")
    private MessagePayload message;
    /** The name of the topic on which the message was emitted on. */
    @JsonProperty("topic_name")
    private String topicName;
    /** The hash of the name of the topic. */
    @JsonProperty("topic_name_hash")
    private String topicNameHash;
    /** Message index in the topic. */
    @JsonProperty("topic_index")
    private int topicIndex;
    /** Message index in the block. */
    @JsonProperty("block_index")
    private long blockIndex;
}
