package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MessageTopicSummary {

    @JsonProperty("message_count")
    private int messageCount;

    @JsonProperty("blocktime")
    private long blockTime;

    @JsonProperty("topic_name")
    private String topicName;
}
