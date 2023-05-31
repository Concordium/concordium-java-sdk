package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.*;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * A baker was configured.
 * The details of what happened are stored in the list of BakerEvents
 */
@Builder
@EqualsAndHashCode
@Getter
@ToString
public class BakerConfiguredResult implements AccountTransactionResult {

    private List<BakerEvent> events;
    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_CONFIGURED;
    }

    /**
     * Parses {@link AccountTransactionEffects.BakerConfigured} and {@link AccountAddress} to {@link BakerConfiguredResult}.
     * @param bakerConfigured {@link AccountTransactionEffects.BakerConfigured} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerConfiguredResult}.
     */
    public static BakerConfiguredResult parse(AccountTransactionEffects.BakerConfigured bakerConfigured, AccountAddress sender) {
        val events = new ImmutableList.Builder<BakerEvent>();
        val bakerEvents = bakerConfigured.getEventsList();
        bakerEvents.forEach(e -> {
            switch (e.getEventCase()) {
                case BAKER_ADDED:
                    events.add(BakerAddedResult.parse(e.getBakerAdded()));
                    break;
                case BAKER_REMOVED:
                    events.add(BakerRemovedResult.parse(e.getBakerRemoved(), sender));
                    break;
                case BAKER_STAKE_INCREASED:
                    events.add(BakerStakeIncreasedResult.parse(e.getBakerStakeIncreased(), sender));
                    break;
                case BAKER_STAKE_DECREASED:
                    events.add(BakerStakeDecreasedResult.parse(e.getBakerStakeDecreased(), sender));
                    break;
                case BAKER_RESTAKE_EARNINGS_UPDATED:
                    events.add(BakerSetRestakeEarningsResult.parse(e.getBakerRestakeEarningsUpdated(), sender));
                    break;
                case BAKER_KEYS_UPDATED:
                    events.add(BakerKeysUpdatedResult.parse(e.getBakerKeysUpdated()));
                    break;
                case BAKER_SET_OPEN_STATUS:
                    events.add(BakerSetOpenStatus.parse(e.getBakerSetOpenStatus(), sender));
                    break;
                case BAKER_SET_METADATA_URL:
                    events.add(BakerSetMetadataURL.parse(e.getBakerSetMetadataUrl(), sender));
                    break;
                case BAKER_SET_TRANSACTION_FEE_COMMISSION:
                    events.add(BakerSetTransactionFeeCommission.parse(e.getBakerSetTransactionFeeCommission(), sender));
                    break;
                case BAKER_SET_BAKING_REWARD_COMMISSION:
                    events.add(BakerSetBakingRewardCommission.parse(e.getBakerSetBakingRewardCommission(), sender));
                    break;
                case BAKER_SET_FINALIZATION_REWARD_COMMISSION:
                    events.add(BakerSetFinalizationRewardCommission.parse(e.getBakerSetFinalizationRewardCommission(), sender));
                    break;
                case EVENT_NOT_SET:
                    throw new IllegalArgumentException();
            }
        });

        return BakerConfiguredResult.builder()
                .events(events.build())
                .build();
    }
}
