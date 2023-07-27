package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.CommissionRanges;
import com.concordium.grpc.v2.PoolParametersCpv1;
import com.concordium.sdk.Range;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Pool parameters for protocol versions > 3
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
public final class PoolParameters {

    /**
     * The range of allowed finalization commissions.
     */
    @JsonProperty("passiveFinalizationCommission")
    private final double passiveFinalizationCommission;

    /**
     * The range of allowed baker commissions.
     */
    @JsonProperty("passiveBakingCommission")
    private final double passiveBakingCommission;

    /**
     * Commission rates charged for passive delegation.
     */
    @JsonProperty("passiveTransactionCommission")
    private final double passiveTransactionCommission;

    /**
     * The range of allowed finalization commissions.
     */
    @JsonProperty("finalizationCommissionRange")
    private final Range finalizationCommissionRange;

    /**
     * The range of allowed baker commissions.
     */
    @JsonProperty("bakingCommissionRange")
    private final Range bakingCommissionRange;

    /**
     * The range of allowed transaction commissions.
     */
    @JsonProperty("transactionCommissionRange")
    private final Range transactionCommissionRange;

    /**
     * Minimum equity capital required for a new baker.
     */
    @JsonProperty("minimumEquityCapital")
    private final CCDAmount minimumEquityCapital;

    /**
     * A bound on the relative share of the total staked capital that a baker can have as its stake.
     * This is required to be greater than 0.
     */
    @JsonProperty("capitalBound")
    private final double capitalBound;

    /**
     * The maximum leverage that a baker can have as a ratio of total stake
     * to equity capital.
     */
    @JsonProperty("leverageBound")
    private final Fraction leverageBound;

    public static PoolParameters from(PoolParametersCpv1 update) {
        CommissionRanges commissionBounds = update.getCommissionBounds();
        return PoolParameters
                .builder()
                .passiveBakingCommission(update.getPassiveBakingCommission().getPartsPerHundredThousand()/100_000d)
                .passiveTransactionCommission(update.getPassiveTransactionCommission().getPartsPerHundredThousand()/100_000d)
                .passiveFinalizationCommission(update.getPassiveFinalizationCommission().getPartsPerHundredThousand()/100_000d)
                .finalizationCommissionRange(Range.from(commissionBounds.getFinalization()))
                .bakingCommissionRange(Range.from(commissionBounds.getBaking()))
                .transactionCommissionRange(Range.from(commissionBounds.getTransaction()))
                .minimumEquityCapital(CCDAmount.fromMicro(update.getMinimumEquityCapital().getValue()))
                .capitalBound(update.getCapitalBound().getValue().getPartsPerHundredThousand()/100_000d)
                .leverageBound(Fraction.from(update.getLeverageBound().getValue()))
                .build();
    }
}
