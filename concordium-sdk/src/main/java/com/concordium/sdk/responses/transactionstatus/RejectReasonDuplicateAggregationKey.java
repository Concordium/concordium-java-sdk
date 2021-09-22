package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public class RejectReasonDuplicateAggregationKey extends RejectReasonContent{
    @Getter
    private final String publicKey;

    @JsonCreator
    RejectReasonDuplicateAggregationKey(String key) {
        this.publicKey = key;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_AGGREGATION_KEY;
    }
}
