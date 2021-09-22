package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RejectReasonInvalidContractAddress extends RejectReasonContent{
    private final ContractAddress contractAddress;

    @JsonCreator
    RejectReasonInvalidContractAddress(@JsonProperty("contractAddress") ContractAddress contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CONTRACT_ADDRESS;
    }
}
