package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
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
    private final BLSPublicKey publicKey;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_AGGREGATION_KEY;
    }

}
