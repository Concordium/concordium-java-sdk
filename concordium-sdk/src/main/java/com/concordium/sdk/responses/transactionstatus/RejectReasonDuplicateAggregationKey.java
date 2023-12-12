package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A baker with the given aggregation key already exists
 */
@ToString
@Builder
public class RejectReasonDuplicateAggregationKey extends RejectReason {
    @Getter
    private final String publicKey;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_AGGREGATION_KEY;
    }

}
