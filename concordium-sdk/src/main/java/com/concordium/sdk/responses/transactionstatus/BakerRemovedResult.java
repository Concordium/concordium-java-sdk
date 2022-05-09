package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class BakerRemovedResult extends AbstractBakerResult {
    @JsonCreator
    BakerRemovedResult(@JsonProperty("bakerId") String bakerId,
                       @JsonProperty("account") String account) {
        super(bakerId, account);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_REMOVED;
    }
}
