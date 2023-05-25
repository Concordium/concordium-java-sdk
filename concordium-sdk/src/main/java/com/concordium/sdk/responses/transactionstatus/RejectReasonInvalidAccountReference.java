package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account does not exist
 */
@ToString
public class RejectReasonInvalidAccountReference extends RejectReason {
    @Getter
    private final AccountAddress address;

    @JsonCreator
    RejectReasonInvalidAccountReference(@JsonProperty("contents") AccountAddress address) {
        this.address = address;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link RejectReasonInvalidAccountReference}.
     * @param invalidAccountReference {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonInvalidAccountReference}.
     */
    public static RejectReasonInvalidAccountReference parse(com.concordium.grpc.v2.AccountAddress invalidAccountReference) {
        return new RejectReasonInvalidAccountReference(AccountAddress.parse(invalidAccountReference));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_REFERENCE;
    }
}
