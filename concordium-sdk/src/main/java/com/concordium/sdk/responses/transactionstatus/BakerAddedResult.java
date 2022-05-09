package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class BakerAddedResult extends AbstractBakerChangeResult {
    private final boolean restakeEarnings;
    private final String stake;

    @JsonCreator
    BakerAddedResult(
            @JsonProperty("bakerId") String bakerId,
            @JsonProperty("account") AccountAddress account,
            @JsonProperty("electionKey") String electionKey,
            @JsonProperty("aggregationKey") String aggregationKey,
            @JsonProperty("signKey") String signKey,
            @JsonProperty("restakeEarnings") boolean restakeEarnings,
            @JsonProperty("stake") String stake) {
        super(bakerId, account, electionKey, aggregationKey, signKey);
        this.restakeEarnings = restakeEarnings;
        this.stake = stake;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_ADDED;
    }
}
