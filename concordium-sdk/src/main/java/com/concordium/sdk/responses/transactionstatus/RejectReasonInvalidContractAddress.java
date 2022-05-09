package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class RejectReasonInvalidContractAddress extends RejectReason {
    private final ContractAddress contractAddress;

    @JsonCreator
    RejectReasonInvalidContractAddress(@JsonProperty("contents") ContractAddress contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CONTRACT_ADDRESS;
    }
}
