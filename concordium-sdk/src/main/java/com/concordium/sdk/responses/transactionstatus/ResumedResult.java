package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ResumedResult implements ContractTraceElement, TransactionResultEvent {
    private final ContractAddress address;
    private final boolean success;
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
