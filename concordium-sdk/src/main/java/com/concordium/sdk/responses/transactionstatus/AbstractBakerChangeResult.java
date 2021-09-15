package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AbstractBakerChangeResult extends AbstractBakerResult {
    private final String electionKey;
    private final String aggregationKey;
    private final String signKey;

    @JsonCreator
    AbstractBakerChangeResult(@JsonProperty("bakerId") String bakerId,
                                     @JsonProperty("account") String account,
                                     @JsonProperty("electionKey") String electionKey,
                                     @JsonProperty("aggregationKey") String aggregationKey,
                                     @JsonProperty("signKey") String signKey) {
        super(bakerId, account);
        this.electionKey = electionKey;
        this.aggregationKey = aggregationKey;
        this.signKey = signKey;
    }
}
