package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

import java.util.List;

/**
 * Events where one or more occurs when a {@link com.concordium.sdk.transactions.ConfigureBaker} transaction
 * is executed on the chain.
 */
@EqualsAndHashCode
@Getter
@Builder
public class BakerConfigured {

    @Singular
    private final List<AbstractBakerResult> events;

    public static BakerConfigured from(AccountTransactionEffects.BakerConfigured bakerConfigured, AccountAddress sender) {
        val builder = BakerConfigured.builder();
        val bakerEvents = bakerConfigured.getEventsList();
        for (BakerEvent bakerEvent : bakerEvents) {
            switch (bakerEvent.getEventCase()) {
                case BAKER_ADDED:
                    builder.event(BakerAddedResult.from(bakerEvent.getBakerAdded()));
                    break;
                case BAKER_REMOVED:
                    builder.event(BakerRemovedResult.from(bakerEvent.getBakerRemoved(), sender));
                    break;
                case BAKER_STAKE_INCREASED:
                    builder.event(BakerStakeIncreasedResult.from(bakerEvent.getBakerStakeIncreased(), sender));
                    break;
                case BAKER_STAKE_DECREASED:
                    builder.event(BakerStakeDecreasedResult.from(bakerEvent.getBakerStakeDecreased(), sender));
                    break;
                case BAKER_RESTAKE_EARNINGS_UPDATED:
                    builder.event(BakerSetRestakeEarningsResult.from(bakerEvent.getBakerRestakeEarningsUpdated(), sender));
                    break;
                case BAKER_KEYS_UPDATED:
                    builder.event(BakerKeysUpdatedResult.from(bakerEvent.getBakerKeysUpdated(), sender));
                    break;
                case BAKER_SET_OPEN_STATUS:
                    builder.event(BakerSetOpenStatus.from(bakerEvent.getBakerSetOpenStatus(), sender));
                    break;
                case BAKER_SET_METADATA_URL:
                    builder.event(BakerSetMetadataURL.from(bakerEvent.getBakerSetMetadataUrl(), sender));
                    break;
                case BAKER_SET_TRANSACTION_FEE_COMMISSION:
                    builder.event(BakerSetTransactionFeeCommission.from(bakerEvent.getBakerSetTransactionFeeCommission(), sender));
                    break;
                case BAKER_SET_BAKING_REWARD_COMMISSION:
                    builder.event(BakerSetBakingRewardCommission.from(bakerEvent.getBakerSetBakingRewardCommission(), sender));
                    break;
                case BAKER_SET_FINALIZATION_REWARD_COMMISSION:
                    builder.event(BakerSetFinalizationRewardCommission.from(bakerEvent.getBakerSetFinalizationRewardCommission(), sender));
                    break;
                case EVENT_NOT_SET:
                    throw new IllegalArgumentException("Baker event was not set.");
            }
        }
        return builder.build();
    }
}
