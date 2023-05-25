package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
public class BakerSetFinalizationRewardCommission extends AbstractBakerResult {

    private final PartsPerHundredThousand finalizationRewardCommission;

    @JsonCreator
    BakerSetFinalizationRewardCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                         @JsonProperty("account") AccountAddress bakerAccount,
                                         @JsonProperty("finalizationRewardCommission") PartsPerHundredThousand finalizationRewardCommission){
        super(bakerId, bakerAccount);
        this.finalizationRewardCommission = finalizationRewardCommission;
    }

    /**
     * Parses {@link BakerEvent.BakerSetFinalizationRewardCommission} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetFinalizationRewardCommission}.
     * @param bakerSetFinalizationRewardCommission {@link BakerEvent.BakerSetFinalizationRewardCommission} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetFinalizationRewardCommission}.
     */
    public static BakerSetFinalizationRewardCommission parse(BakerEvent.BakerSetFinalizationRewardCommission bakerSetFinalizationRewardCommission, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetFinalizationRewardCommission.builder()
                .bakerId(AccountIndex.from(bakerSetFinalizationRewardCommission.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .finalizationRewardCommission(PartsPerHundredThousand.parse(bakerSetFinalizationRewardCommission.getFinalizationRewardCommission()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_FINALIZATION_REWARD_COMMISSION;
    }
}
