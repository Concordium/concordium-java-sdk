package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
public class RejectReasonDuplicateAggregationKey extends RejectReason {
    @Getter
    private final String publicKey;

    @JsonCreator
    RejectReasonDuplicateAggregationKey(@JsonProperty("contents") String key) {
        this.publicKey = key;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_AGGREGATION_KEY;
    }

}
