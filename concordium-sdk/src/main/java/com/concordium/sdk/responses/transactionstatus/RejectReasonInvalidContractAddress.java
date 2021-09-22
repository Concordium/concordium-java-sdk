package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class RejectReasonInvalidContractAddress extends RejectReason {
    private final ContractAddress contractAddress;

    @JsonCreator
    RejectReasonInvalidContractAddress(@JsonProperty("contents") Map<String, Object> contractAddress) {
        this.contractAddress = (ContractAddress) ContractAddress.parseAccount(contractAddress);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CONTRACT_ADDRESS;
    }
}
