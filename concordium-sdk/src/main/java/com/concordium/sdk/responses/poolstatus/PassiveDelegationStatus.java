package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@ToString
public class PassiveDelegationStatus extends PoolStatus {

    /**
     * The total capital delegated passively.
     */
    private CCDAmount delegatedCapital;

    /**
     * "The passive delegation commission rates.
     */
    private CommissionRates commissionRates;

    /**
     * The transaction fees accruing to the passive delegators in the current reward period.
     */
    private CCDAmount currentPaydayTransactionFeesEarned;

    /**
     * The effective delegated capital to the passive delegators for the current reward period.
     */
    private CCDAmount currentPaydayDelegatedCapital;

    /**
     * Total capital staked across all pools, including passive delegation.
     */
    private CCDAmount allPoolTotalCapital;

    public PassiveDelegationStatus() {
        this.setPoolType(PoolType.PASSIVE_DELEGATION);
    }

    public static PassiveDelegationStatus fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), PassiveDelegationStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse PoolStatus JSON", e);
        }
    }
}
