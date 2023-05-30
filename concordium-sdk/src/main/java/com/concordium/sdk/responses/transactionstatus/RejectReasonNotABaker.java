package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Account is not a baker account.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonNotABaker extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @JsonCreator
    public RejectReasonNotABaker(@JsonProperty("contents") AccountAddress accountAddress) {
        this.accountAddress = accountAddress;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link RejectReasonNotABaker}.
     * @param notABaker {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonNotABaker}.
     */
    public static RejectReasonNotABaker parse(com.concordium.grpc.v2.AccountAddress notABaker) {
        return new RejectReasonNotABaker(AccountAddress.parse(notABaker));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_A_BAKER;
    }
}
