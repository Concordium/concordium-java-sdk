package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.BakerPoolInfo;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class BakerPoolStatus {

    /**
     * "The 'BakerId' of the pool owner.
     */
    private final BakerId bakerId;

    /**
     * The account address of the pool owner.
     */
    private final AccountAddress bakerAddress;

    /**
     * The equity capital provided by the pool owner.
     */
    private final CCDAmount bakerEquityCapital;

    /**
     * The capital delegated to the pool by other accounts.
     */
    private final CCDAmount delegatedCapital;

    /**
     * The maximum amount that may be delegated to the pool, accounting for leverage and stake limits.
     */
    private final CCDAmount delegatedCapitalCap;

    /**
     * The pool info associated with the pool: open status, metadata URL and commission rates.
     */
    private final BakerPoolInfo poolInfo;

    /**
     * Any pending change to the baker's stake.
     * This is not used from protocol version 7 onwards, as stake changes are immediate.
     */
    private final PendingChange bakerStakePendingChange;

    /**
     * Status of the pool in the current reward period.
     */
    private final CurrentPaydayStatus currentPaydayStatus;

    /**
     * Total capital staked across all pools.
     */
    private final CCDAmount allPoolTotalCapital;

    /**
     * A flag indicating whether the pool owner is suspended.
     * Absent if the protocol version does not support validator suspension or the pool is removed.
     */
    private final Boolean isSuspended;

    public Optional<CCDAmount> getBakerEquityCapital() {
        return Optional.ofNullable(bakerEquityCapital);
    }

    public Optional<CCDAmount> getDelegatedCapital() {
        return Optional.ofNullable(delegatedCapital);
    }

    public Optional<CCDAmount> getDelegatedCapitalCap() {
        return Optional.ofNullable(delegatedCapitalCap);
    }

    public Optional<BakerPoolInfo> getPoolInfo() {
        return Optional.ofNullable(poolInfo);
    }

    public Optional<PendingChange> getBakerStakePendingChange() {
        return Optional.ofNullable(bakerStakePendingChange);
    }

    public Optional<CurrentPaydayStatus> getCurrentPaydayStatus() {
        return Optional.ofNullable(currentPaydayStatus);
    }

    public Optional<Boolean> isSuspended() {
        return Optional.ofNullable(isSuspended);
    }
}
