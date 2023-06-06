package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.transactionevent.accounttransactionresults.ContractTraceElement;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.ContractTraceElementType;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * A previously interrupted contract was resumed.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ResumedResult extends TransactionResultEvent implements ContractTraceElement {
    /**
     * The contract resumed.
     */
    private final ContractAddress address;
    /**
     * Whether the action that caused the interruption (invoke contract or make transfer) was successful or not.
     */
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
    public static ResumedResult parse(com.concordium.grpc.v2.ContractTraceElement.Resumed resumed) {
        return ResumedResult.builder()
                .address(ContractAddress.parse(resumed.getAddress()))
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
