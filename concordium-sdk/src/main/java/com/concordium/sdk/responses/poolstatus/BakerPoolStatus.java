package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Data
@Getter
@ToString
public class BakerPoolStatus extends PoolStatus {

    /**
     * "The 'BakerId' of the pool owner.
     */
    private BakerId bakerId;

    /**
     * The account address of the pool owner.
     */
    private AccountAddress bakerAddress;

    /**
     * The equity capital provided by the pool owner.
     */
    private CCDAmount bakerEquityCapital;

    /**
     * The capital delegated to the pool by other accounts.
     */
    private CCDAmount delegatedCapital;

    /**
     * The maximum amount that may be delegated to the pool, accounting for leverage and stake limits.
     */
    private CCDAmount delegatedCapitalCap;

    /**
     * The pool info associated with the pool: open status, metadata URL and commission rates.
     */
    private PoolInfo poolInfo;

    /**
     * Any pending change to the baker's stake.
     */
    private PendingChange bakerStakePendingChange;

    /**
     * Status of the pool in the current reward period.
     */
    private Optional<CurrentPaydayStatus> currentPaydayStatus;

    /**
     * Total capital staked across all pools.
     */
    private CCDAmount allPoolTotalCapital;

    public BakerPoolStatus() {
        this.setPoolType(PoolType.BAKER_POOL);
    }

    public static BakerPoolStatus fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), BakerPoolStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse PoolStatus JSON", e);
        }
    }
}
