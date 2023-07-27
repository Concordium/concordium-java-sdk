package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractEvent;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public class InterruptedResult extends TransactionResultEvent implements ContractTraceElement{

    /**
     * The contract that was interrupted.
     */
    private final ContractAddress address;

    /**
     * List of logged events from the contract.
     */
    private final List<byte[]> events;

    @SneakyThrows
    @JsonCreator
    InterruptedResult(@JsonProperty("address") Map<String, Object> address,
                      @JsonProperty("events") List<String> events) {
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
        this.events = new ArrayList<>();
        for (String event : events) {
            this.events.add(Hex.decodeHex(event));
        }
    }

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
