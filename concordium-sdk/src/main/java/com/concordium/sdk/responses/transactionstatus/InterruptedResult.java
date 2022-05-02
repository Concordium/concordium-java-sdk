package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class InterruptedResult extends TransactionResultEvent {

    private final ContractAddress address;
    private final List<String> events;

    @JsonCreator
    InterruptedResult(@JsonProperty("address") Map<String, Object> address,
                      @JsonProperty("events") List<String> events){
        this.address = (ContractAddress) AbstractAccount.parseAccount(address);
        this.events = events;
    }
}
