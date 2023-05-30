package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Updated baking reward commission for a baker pool.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
public class BakerSetBakingRewardCommission extends AbstractBakerResult {

    /**
     * The baking reward commission.
     */
    private final PartsPerHundredThousand bakingRewardCommission;

    @JsonCreator
    BakerSetBakingRewardCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                   @JsonProperty("account") AccountAddress bakerAccount,
                                   @JsonProperty("bakingRewardCommission") PartsPerHundredThousand bakingRewardCommission) {
        super(bakerId, bakerAccount);
        this.bakingRewardCommission = bakingRewardCommission;
    }

    /**
     * Parses {@link BakerEvent.BakerSetBakingRewardCommission} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetBakingRewardCommission}.
     * @param bakerSetBakingRewardCommission {@link BakerEvent.BakerSetBakingRewardCommission} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetBakingRewardCommission}.
     */
    public static BakerSetBakingRewardCommission parse(BakerEvent.BakerSetBakingRewardCommission bakerSetBakingRewardCommission, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetBakingRewardCommission.builder()
                .bakerId(AccountIndex.from(bakerSetBakingRewardCommission.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .bakingRewardCommission(PartsPerHundredThousand.parse(bakerSetBakingRewardCommission.getBakingRewardCommission()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_BAKING_REWARD_COMMISSION;
    }
}
