package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerId;
import com.concordium.sdk.responses.AccountIndex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Tried to add baker/delegator for an account that already has a baker.
 */
@ToString
@Getter
public class RejectReasonAlreadyABaker extends RejectReason {
    private final AccountIndex bakerId;

    @JsonCreator
    RejectReasonAlreadyABaker(@JsonProperty("contents") AccountIndex bakerId) {
        this.bakerId = bakerId;
    }

    /**
     * Parses {@link BakerId} to {@link RejectReasonAlreadyABaker}.
     * @param alreadyABaker {@link BakerId} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonAlreadyABaker}.
     */
    public static RejectReasonAlreadyABaker parse(BakerId alreadyABaker) {
        return new RejectReasonAlreadyABaker(AccountIndex.from(alreadyABaker.getValue()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_BAKER;
    }
}
