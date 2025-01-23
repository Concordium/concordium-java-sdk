package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class CurrentPaydayStatus {

    /**
     * The number of blocks baked in the current reward period.
     */
    private final UInt64 blocksBaked;

    /**
     * Whether the baker has contributed a finalization proof in the current reward period.
     */
    private final boolean finalizationLive;

    /**
     * The transaction fees accruing to the pool in the current reward period.
     */
    private final CCDAmount transactionFeesEarned;

    /**
     * The effective stake of the baker in the current reward period.
     */
    private final CCDAmount effectiveStake;

    /**
     * The lottery power of the baker in the current reward period.
     */
    private final double lotteryPower;

    /**
     * The effective equity capital of the baker for the current reward period.
     */
    private final CCDAmount bakerEquityCapital;

    /**
     * The effective delegated capital to the pool for the current reward period.
     */
    private final CCDAmount delegatedCapital;

    /**
     * The commission rates that apply for the current reward period.
     */
    private final CommissionRates commissionRates;

    /**
     * A flag indicating whether the pool owner is primed for suspension.
     * Absent if the protocol version does not support validator suspension.
     */
    private final Boolean isPrimedForSuspension;

    public Optional<Boolean> isPrimedForSuspension() {
        return Optional.ofNullable(isPrimedForSuspension);
    }

    /**
     * The number of missed rounds in the current reward period.
     * Absent if the protocol version does not support validator suspension.
     */
    private final UInt64 missedRounds;

    public Optional<UInt64> getMissedRounds() {
        return Optional.ofNullable(missedRounds);
    }
}
