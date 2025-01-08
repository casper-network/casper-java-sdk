package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.MessageTopicSummary;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ian@meywood.com
 */
public class MessageTopicKind implements StoredValue<MessageTopicSummary> {

    @JsonProperty("MessageTopic")
    private MessageTopicSummary value;

    @Override
    public MessageTopicSummary getValue() {
        return value;
    }
}
