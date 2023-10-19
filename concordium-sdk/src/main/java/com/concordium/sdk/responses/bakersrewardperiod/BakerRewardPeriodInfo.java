package com.concordium.sdk.responses.bakersrewardperiod;

import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Information about a particular baker with respect to the current reward period.
 */
@Builder
@ToString
@Getter
@EqualsAndHashCode
public class BakerRewardPeriodInfo {

    /**
     * The baker id and public keys for the baker.
     */
    private BakerInfo baker;
    /**
     * The effective stake of the baker for the consensus protocol.
     * The returned amount accounts for delegation, capital bounds and leverage bounds.
     */
    private CCDAmount effectiveStake;
    /**
     * The effective commission rate for the baker that applies for the reward period.
     */
    private CommissionRates commissionRates;
    /**
     * The amount staked by the baker itself.
     */
    private CCDAmount equityCapital;
    /**
     * The total amount of capital delegated to this baker pool.
     */
    private CCDAmount delegatedCapital;
    /**
     * Whether the baker is a finalizer or not.
     */
    private boolean isFinalizer;

    /**
     * Parses {@link com.concordium.grpc.v2.BakerRewardPeriodInfo} to {@link BakerRewardPeriodInfo}.
     *
     * @param info {@link com.concordium.grpc.v2.BakerRewardPeriodInfo} returned by the GRPC v2 API.
     * @return Parsed {@link BakerRewardPeriodInfo}.
     */
    public static BakerRewardPeriodInfo from(com.concordium.grpc.v2.BakerRewardPeriodInfo info) {
        return BakerRewardPeriodInfo.builder()
                .baker(BakerInfo.from(info.getBaker()))
                .effectiveStake(CCDAmount.from(info.getEffectiveStake()))
                .commissionRates(CommissionRates.from(info.getCommissionRates()))
                .equityCapital(CCDAmount.from(info.getEquityCapital()))
                .delegatedCapital(CCDAmount.from(info.getDelegatedCapital()))
                .isFinalizer(info.getIsFinalizer())
                .build();

    }

}
