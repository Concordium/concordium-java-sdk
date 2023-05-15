package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Rejected due to contract logic in the init method of the contract.
 */
@Getter
@ToString
public class RejectReasonRejectedInit extends RejectReason {
    /**
     * The reject reason code.
     */
    private final int rejectedInit;

    @JsonCreator
    public RejectReasonRejectedInit(@JsonProperty("rejectReason") int rejectedInit) {
        this.rejectedInit = rejectedInit;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason.RejectedInit} to {@link RejectReasonRejectedInit}.
     * @param rejectedInit {@link com.concordium.grpc.v2.RejectReason.RejectedInit} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonRejectedInit}.
     */
    public static RejectReasonRejectedInit parse(com.concordium.grpc.v2.RejectReason.RejectedInit rejectedInit) {
        return new RejectReasonRejectedInit(rejectedInit.getRejectReason());
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_INIT;
    }
}
