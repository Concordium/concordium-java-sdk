package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSetFinalizationRewardCommission extends AbstractBakerResult {

    private final double finalizationRewardCommission;
    public static BakerSetFinalizationRewardCommission from(BakerEvent.BakerSetFinalizationRewardCommission bakerSetFinalizationRewardCommission, AccountAddress sender) {
        return BakerSetFinalizationRewardCommission
                .builder()
                .finalizationRewardCommission(PartsPerHundredThousand.from(bakerSetFinalizationRewardCommission.getFinalizationRewardCommission().getPartsPerHundredThousand()).asDouble())
                .bakerId(BakerId.from(bakerSetFinalizationRewardCommission.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_FINALIZATION_REWARD_COMMISSION;
    }
}
