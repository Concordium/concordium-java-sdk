package com.concordium.sdk.responses.transactionevent.accounttransactionresults;


import com.concordium.grpc.v2.AccountAddress;
import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Events that may result from the ConfigureBaker transaction.
 */
public interface BakerEvent {

    BakerEventType getBakerEventType();

    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent} and {@link AccountAddress} to {@link BakerEvent}.
     * @param bakerEvent {@link com.concordium.grpc.v2.BakerEvent} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerEvent}.
     */
    static BakerEvent parse(com.concordium.grpc.v2.BakerEvent bakerEvent, AccountAddress sender) {
        switch (bakerEvent.getEventCase()) {
            case BAKER_ADDED:
                return BakerAddedResult.parse(bakerEvent.getBakerAdded());
            case BAKER_REMOVED:
                return BakerRemovedResult.parse(bakerEvent.getBakerRemoved(), sender);
            case BAKER_STAKE_INCREASED:
                return BakerStakeIncreasedResult.parse(bakerEvent.getBakerStakeIncreased(), sender);
            case BAKER_STAKE_DECREASED:
                return BakerStakeDecreasedResult.parse(bakerEvent.getBakerStakeDecreased(), sender);
            case BAKER_RESTAKE_EARNINGS_UPDATED:
                return BakerSetRestakeEarningsResult.parse(bakerEvent.getBakerRestakeEarningsUpdated(), sender);
            case BAKER_KEYS_UPDATED:
                return BakerKeysUpdatedResult.parse(bakerEvent.getBakerKeysUpdated());
            case BAKER_SET_OPEN_STATUS:
                return BakerSetOpenStatus.parse(bakerEvent.getBakerSetOpenStatus(), sender);
            case BAKER_SET_METADATA_URL:
                return BakerSetMetadataURL.parse(bakerEvent.getBakerSetMetadataUrl(), sender);
            case BAKER_SET_TRANSACTION_FEE_COMMISSION:
                return BakerSetTransactionFeeCommission.parse(bakerEvent.getBakerSetTransactionFeeCommission(), sender);
            case BAKER_SET_BAKING_REWARD_COMMISSION:
                return BakerSetBakingRewardCommission.parse(bakerEvent.getBakerSetBakingRewardCommission(), sender);
            case BAKER_SET_FINALIZATION_REWARD_COMMISSION:
                return BakerSetFinalizationRewardCommission.parse(bakerEvent.getBakerSetFinalizationRewardCommission(), sender);
            default:
                throw new IllegalArgumentException();
        }
    }
}
