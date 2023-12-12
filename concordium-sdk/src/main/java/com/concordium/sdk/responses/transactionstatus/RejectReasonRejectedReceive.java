package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.ContractAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Rejected due to contract logic in its receive method.
 */
@Getter
@ToString
@Builder
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

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_RECEIVE;
    }
}
