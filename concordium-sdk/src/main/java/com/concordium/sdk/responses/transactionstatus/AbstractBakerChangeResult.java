package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractBakerChangeResult extends AbstractBakerResult {
    private final String electionKey;
    private final String aggregationKey;
    private final String signKey;

    @JsonCreator
    AbstractBakerChangeResult(@JsonProperty("bakerId") String bakerId,
                                     @JsonProperty("account") AccountAddress account,
                                     @JsonProperty("electionKey") String electionKey,
                                     @JsonProperty("aggregationKey") String aggregationKey,
                                     @JsonProperty("signKey") String signKey) {
        super(bakerId, account);
        this.electionKey = electionKey;
        this.aggregationKey = aggregationKey;
        this.signKey = signKey;
    }
}
