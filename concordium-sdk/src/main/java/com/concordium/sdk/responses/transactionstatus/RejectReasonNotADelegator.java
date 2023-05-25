package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account is not a delegation account.
 */
@ToString
@Getter
public class RejectReasonNotADelegator extends RejectReason {
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonNotADelegator(@JsonProperty("contents") AccountAddress accountAddress) {
        this.accountAddress = accountAddress;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link RejectReasonNotADelegator}.
     * @param notADelegator {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonNotADelegator}.
     */
    public static RejectReasonNotADelegator parse(com.concordium.grpc.v2.AccountAddress notADelegator) {
        return new RejectReasonNotADelegator(AccountAddress.parse(notADelegator));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_A_DELEGATOR;
    }
}
