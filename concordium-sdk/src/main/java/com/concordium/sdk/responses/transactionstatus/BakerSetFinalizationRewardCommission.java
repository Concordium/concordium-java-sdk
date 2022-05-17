package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@ToString
public class BakerSetFinalizationRewardCommission extends AbstractBakerResult {
    // parts per 100000

    private final PartsPerHundredThousand finalizationRewardCommission;

    @JsonCreator
    BakerSetFinalizationRewardCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                         @JsonProperty("account") AccountAddress bakerAccount,
                                         @JsonProperty("finalizationRewardCommission") PartsPerHundredThousand finalizationRewardCommission){
        super(bakerId, bakerAccount);
        this.finalizationRewardCommission = finalizationRewardCommission;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_FINALIZATION_REWARD_COMMISSION;
    }
}
