package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public class ResumedResult extends TransactionResultEvent implements ContractTraceElement {
    private final ContractAddress address;
    private final boolean success;

    @JsonCreator
    ResumedResult(@JsonProperty("address") Map<String, Object> address,
                  @JsonProperty("success") boolean success) {
        this.success = success;
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
    }

    public static ResumedResult from(com.concordium.grpc.v2.ContractTraceElement.Resumed resumed) {
        return ResumedResult
                .builder()
                .address(ContractAddress.from(resumed.getAddress()))
                .success(resumed.getSuccess())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.RESUMED;
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.RESUMED;
    }
}
