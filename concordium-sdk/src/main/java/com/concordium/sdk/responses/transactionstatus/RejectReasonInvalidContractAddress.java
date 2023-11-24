package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.ContractAddress;
import lombok.Builder;
import lombok.Getter;

/**
 * Contract instance does not exist on chain.
 */
@Getter
@Builder
public class RejectReasonInvalidContractAddress extends RejectReason {
    private final ContractAddress contractAddress;
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CONTRACT_ADDRESS;
    }
}
