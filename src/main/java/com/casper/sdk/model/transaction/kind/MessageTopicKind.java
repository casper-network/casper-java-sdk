package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.MessageTopicSummary;
import com.casper.sdk.model.storedvalue.StoredValue;
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
public class MessageTopicKind implements StoredValue<MessageTopicSummary> {

    @JsonProperty("MessageTopic")
    private MessageTopicSummary value;

    @Override
    public MessageTopicSummary getValue() {
        return value;
    }
}
