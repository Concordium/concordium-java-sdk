package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class BakerKeysUpdatedResult extends AbstractBakerChangeResult {
    @JsonCreator
    BakerKeysUpdatedResult(@JsonProperty("bakerId") long bakerId,
                           @JsonProperty("account") AccountAddress account,
                           @JsonProperty("electionKey") String electionKey,
                           @JsonProperty("aggregationKey") String aggregationKey,
                           @JsonProperty("signKey") String signKey) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_KEYS_UPDATED;
    }
}
