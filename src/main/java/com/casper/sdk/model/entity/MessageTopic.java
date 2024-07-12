package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Collection of named message topics.
 *
 * @author carl@stormeye.co.uk
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageTopic {

    /** The name of the topic on which the message was emitted on. */
    @JsonProperty("topic_name")
    private String topicName;

    /** The hash of the name of the topic. */
    @JsonProperty("topic_name_hash")
    private String topicNameHash;
}
