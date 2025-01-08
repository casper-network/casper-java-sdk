package com.casper.sdk.model.entity;

import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("MessageTopic")
public class MessageTopicSummary implements StoredValue<MessageTopic> {

    @JsonProperty("message_count")
    private int messageCount;

    @JsonProperty("block_time")
    private long blockTime;

    @JsonProperty("topic_name")
    private String topicName;

    @Override
    public MessageTopic getValue() {
        return MessageTopic.builder()
                .topicName(topicName)
                .build();
    }
}
