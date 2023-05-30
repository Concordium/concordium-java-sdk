package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractTraceElement;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A contract was interrupted.
 * This occurs when a contract invokes another contract or makes a transfer to an account.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class InterruptedResult implements TransactionResultEvent {

    /**
     * The contract interrupted.
     */
    private final ContractAddress address;
    /**
     * The events generated up until the interruption.
     */
    private final List<byte[]> events;

    @SneakyThrows
    @JsonCreator
    InterruptedResult(@JsonProperty("address") Map<String, Object> address,
                      @JsonProperty("events") List<String> events){
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
        this.events = new ArrayList<>();
        for (String event : events) {
            this.events.add(Hex.decodeHex(event));
        }
    }

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement.Interrupted} to {@link InterruptedResult}.
     * @param interrupted {@link com.concordium.grpc.v2.ContractTraceElement.Interrupted} returned by the GRPC V2 API.
     * @return parsed {@link InterruptedResult}.
     */
    public static InterruptedResult parse(ContractTraceElement.Interrupted interrupted) {
        val events = new ImmutableList.Builder<byte[]>();
        interrupted.getEventsList().forEach(e -> events.add(e.getValue().toByteArray()));
        return InterruptedResult.builder()
                .address(ContractAddress.parse(interrupted.getAddress()))
                .events(events.build()).build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.INTERRUPTED;
    }
}
