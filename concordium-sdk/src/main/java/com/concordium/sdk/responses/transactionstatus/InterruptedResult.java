package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class InterruptedResult extends TransactionResultEvent {

    private final ContractAddress address;
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

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.INTERRUPTED;
    }
}
