package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class BakerRemovedResult extends AbstractBakerResult {
    @JsonCreator
    BakerRemovedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                       @JsonProperty("account") AccountAddress account) {
        super(bakerId, account);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_REMOVED;
    }
}
