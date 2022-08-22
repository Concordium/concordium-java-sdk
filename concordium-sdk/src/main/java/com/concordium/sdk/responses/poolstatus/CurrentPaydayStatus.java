package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
@Getter
public class CurrentPaydayStatus {

    /**
     * The number of blocks baked in the current reward period.
     */
    private UInt64 blocksBaked;

    /**
     * Whether the baker has contributed a finalization proof in the current reward period.
     */
    private boolean finalizationLive;

    /**
     * The transaction fees accruing to the pool in the current reward period.
     */
    private CCDAmount transactionFeesEarned;

    /**
     * The effective stake of the baker in the current reward period.
     */
    private CCDAmount effectiveStake;

    /**
     * The lottery power of the baker in the current reward period.
     */
    private double lotteryPower;

    /**
     * The effective equity capital of the baker for the current reward period.
     */
    private CCDAmount bakerEquityCapital;

    /**
     * The effective delegated capital to the pool for the current reward period.
     */
    private CCDAmount delegatedCapital;
}
