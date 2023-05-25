package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractTraceElement;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class ResumedResult implements TransactionResultEvent {
    private final ContractAddress address;
    private final boolean success;

    @JsonCreator
    ResumedResult(@JsonProperty("address") Map<String, Object> address,
                  @JsonProperty("success") boolean success) {
        this.success = success;
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
    }

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement.Resumed} to {@link ResumedResult}.
     * @param resumed {@link com.concordium.grpc.v2.ContractTraceElement.Resumed} returned by the GRPC V2 API.
     * @return parsed {@link ResumedResult}.
     */
    public static ResumedResult parse(ContractTraceElement.Resumed resumed) {
        return ResumedResult.builder()
                .address(ContractAddress.parse(resumed.getAddress()))
                .success(resumed.getSuccess())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.RESUMED;
    }
}
