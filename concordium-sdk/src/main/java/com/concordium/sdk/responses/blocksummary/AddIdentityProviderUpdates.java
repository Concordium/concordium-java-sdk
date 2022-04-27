package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class AddIdentityProviderUpdates {
    private final int threshold;
    private final List<Integer> authorizedKeys;
    private final Nonce nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    AddIdentityProviderUpdates(@JsonProperty("threshold") int threshold,
                               @JsonProperty("authorizedKeys") List<Integer> authorizedKeys,
                               @JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                               @JsonProperty("queue") List<Object> queue) {
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
