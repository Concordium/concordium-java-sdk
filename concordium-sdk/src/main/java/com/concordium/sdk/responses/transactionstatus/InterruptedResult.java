package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractEvent;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.types.ContractAddress;
import com.google.protobuf.ByteString;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class InterruptedResult implements TransactionResultEvent, ContractTraceElement {

    /**
     * The contract that was interrupted.
     */
    private final ContractAddress address;

    /**
     * List of logged events from the contract.
     */
    private final List<byte[]> events;

    public static InterruptedResult from(com.concordium.grpc.v2.ContractTraceElement.Interrupted interrupted) {
        val events = interrupted.getEventsList()
                .stream()
                .map(ContractEvent::getValue)
                .map(ByteString::toByteArray)
                .collect(Collectors.toList());
        return InterruptedResult
                .builder()
                .address(ContractAddress.from(interrupted.getAddress()))
                .events(events)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.INTERRUPTED;
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.INTERRUPTED;
    }
}
