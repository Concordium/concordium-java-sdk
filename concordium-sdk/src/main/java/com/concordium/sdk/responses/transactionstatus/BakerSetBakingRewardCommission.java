package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSetBakingRewardCommission extends AbstractBakerResult {
    private final double bakingRewardCommission;

    @JsonCreator
    BakerSetBakingRewardCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                   @JsonProperty("account") AccountAddress bakerAccount,
                                   @JsonProperty("bakingRewardCommission") PartsPerHundredThousand bakingRewardCommission) {
        super(bakerId, bakerAccount);
        this.bakingRewardCommission = bakingRewardCommission.asDouble();
    }

    public static BakerSetBakingRewardCommission from(BakerEvent.BakerSetBakingRewardCommission bakerSetBakingRewardCommission, AccountAddress sender) {
        return BakerSetBakingRewardCommission
                .builder()
                .bakingRewardCommission(bakerSetBakingRewardCommission.getBakingRewardCommission().getPartsPerHundredThousand()/100_000d)
                .bakerId(BakerId.from(bakerSetBakingRewardCommission.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_BAKING_REWARD_COMMISSION;
    }
}
