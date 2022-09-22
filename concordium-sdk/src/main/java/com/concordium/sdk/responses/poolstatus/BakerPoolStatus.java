package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Data
@Jacksonized
@Builder
public class BakerPoolStatus extends PoolStatus {

    private final PoolType poolType = PoolType.BAKER_POOL;

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
    private final PoolInfo poolInfo;

    /**
     * Any pending change to the baker's stake.
     */
    private final PendingChange bakerStakePendingChange;

    /**
     * Status of the pool in the current reward period.
     */
    private final Optional<CurrentPaydayStatus> currentPaydayStatus;

    /**
     * Total capital staked across all pools.
     */
    private final CCDAmount allPoolTotalCapital;
}
