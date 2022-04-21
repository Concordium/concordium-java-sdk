package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BakerSetFinalizationRewardCommission extends AbstractBakerResult {
    // parts per 100000
    private final int finalizationRewardCommission;

    @JsonCreator
    BakerSetFinalizationRewardCommission(@JsonProperty("bakerId") String bakerId,
                                         @JsonProperty("account") String bakerAccount,
                                         @JsonProperty("finalizationRewardCommission") int finalizationRewardCommission){
        super(bakerId, bakerAccount);
        this.finalizationRewardCommission = finalizationRewardCommission;
    }
}
