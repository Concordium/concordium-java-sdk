package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class ResumedResult extends TransactionResultEvent {
    private final ContractAddress address;
    private final boolean success;

    @JsonCreator
    ResumedResult(@JsonProperty("address") Map<String, Object> address,
                  @JsonProperty("success") boolean success) {
        this.success = success;
        this.address = (ContractAddress) AbstractAddress.parseAccount(address);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.RESUMED;
    }
}
