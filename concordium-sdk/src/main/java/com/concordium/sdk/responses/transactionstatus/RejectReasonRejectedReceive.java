package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Rejected due to contract logic in its receive method.
 */
@Getter
@ToString
public class RejectReasonRejectedReceive extends RejectReason {
    /**
     * The reject reason
     */
    private final int rejectReason;
    /**
     * The address of the contract which rejected.
     */
    private final ContractAddress contractAddress;
    /**
     * The name of the receive method.
     */
    private final String receiveName;
    /**
     * The parameter the it was called with.
     */
    private final String parameter;

    @JsonCreator
    RejectReasonRejectedReceive(@JsonProperty("rejectReason") int rejectReason,
                                @JsonProperty("contractAddress") ContractAddress contractAddress,
                                @JsonProperty("receiveName") String receiveName,
                                @JsonProperty("parameter") String parameter) {
        this.rejectReason = rejectReason;
        this.contractAddress = contractAddress;
        this.receiveName = receiveName;
        this.parameter = parameter;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_RECEIVE;
    }
}
