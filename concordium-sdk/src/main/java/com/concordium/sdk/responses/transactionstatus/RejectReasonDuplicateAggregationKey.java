package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerAggregationVerifyKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * A baker with the given aggregation key already exists
 */
@ToString
public class RejectReasonDuplicateAggregationKey extends RejectReason {
    @Getter
    private final String publicKey;

    @JsonCreator
    RejectReasonDuplicateAggregationKey(@JsonProperty("contents") String key) {
        this.publicKey = key;
    }

    /**
     * Parses {@link BakerAggregationVerifyKey} to {@link RejectReasonDuplicateAggregationKey}.
     * @param duplicateAggregationKey {@link BakerAggregationVerifyKey} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonDuplicateAggregationKey}.
     */
    public static RejectReasonDuplicateAggregationKey parse(BakerAggregationVerifyKey duplicateAggregationKey) {
        return new RejectReasonDuplicateAggregationKey(Hex.encodeHexString(duplicateAggregationKey.getValue().toByteArray()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_AGGREGATION_KEY;
    }

}
