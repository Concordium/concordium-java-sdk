package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString
public final class ContractInitializedResult extends TransactionResultEvent {
    private final String ref;
    private final ContractAddress address;
    private CCDAmount amount;
    private final String initName;
    private final List<String> events;
    private final int version;

    @JsonCreator
    ContractInitializedResult(@JsonProperty("ref") String ref,
                              @JsonProperty("address") ContractAddress address,
                              @JsonProperty("amount") String amount,
                              @JsonProperty("initName") String initName,
                              @JsonProperty("events") List<String> events,
                              @JsonProperty("contractVersion") int version) {
        this.ref = ref;
        this.address = address;
        if(!Objects.isNull(amount)) {
            this.amount = CCDAmount.fromMicro(amount);
        }
        this.initName = initName;
        this.events = events;
        this.version = version;
    }
}