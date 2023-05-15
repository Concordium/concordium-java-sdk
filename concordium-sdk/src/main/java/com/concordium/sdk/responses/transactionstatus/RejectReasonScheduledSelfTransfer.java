package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer with schedule to itself, that's not allowed.
 */
@ToString
public class RejectReasonScheduledSelfTransfer extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonScheduledSelfTransfer(@JsonProperty("contents") AccountAddress accountAddress) {
        this.accountAddress = accountAddress;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link RejectReasonScheduledSelfTransfer}.
     * @param scheduledSelfTransfer {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonScheduledSelfTransfer}.
     */
    public static RejectReasonScheduledSelfTransfer parse(com.concordium.grpc.v2.AccountAddress scheduledSelfTransfer) {
        return new RejectReasonScheduledSelfTransfer(AccountAddress.from(scheduledSelfTransfer.getValue().toByteArray()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SCHEDULED_SELF_TRANSFER;
    }
}
