package com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards;

import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class TransactionFeeDistribution {
    private final double gasAccount;
    private final double baker;
    private final int threshold;
    private final List<Integer> authorizedKeys;
    private final Nonce nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    TransactionFeeDistribution(@JsonProperty("gasAccount") double gasAccount,
                               @JsonProperty("baker") double baker,
                               @JsonProperty("threshold") int threshold,
                               @JsonProperty("authorizedKeys") List<Integer> authorizedKeys,
                               @JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                               @JsonProperty("queue") List<Object> queue) {
        this.gasAccount = gasAccount;
        this.baker = baker;
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
