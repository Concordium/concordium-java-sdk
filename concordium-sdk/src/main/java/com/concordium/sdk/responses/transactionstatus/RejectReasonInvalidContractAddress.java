package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Contract instance does not exist on chain.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class RejectReasonInvalidContractAddress extends RejectReason {
    private final ContractAddress contractAddress;

    @JsonCreator
    RejectReasonInvalidContractAddress(@JsonProperty("contents") ContractAddress contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.ContractAddress} to {@link RejectReasonInvalidContractAddress}.
     * @param invalidContractAddress {@link com.concordium.grpc.v2.ContractAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonInvalidContractAddress}.
     */
    public static RejectReason parse(com.concordium.grpc.v2.ContractAddress invalidContractAddress) {
        return new RejectReasonInvalidContractAddress(ContractAddress.parse(invalidContractAddress));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CONTRACT_ADDRESS;
    }
}
