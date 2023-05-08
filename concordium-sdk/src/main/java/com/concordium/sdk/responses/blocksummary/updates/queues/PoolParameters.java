package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.Range;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Pool parameters for protocol versions > 3
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
public final class PoolParameters {

    /**
     * The range of allowed finalization commissions.
     */
    private final double passiveFinalizationCommission;

    /**
     * The range of allowed baker commissions.
     */
    private final double passiveBakingCommission;

    /**
     * Commission rates charged for passive delegation.
     */
    private final double passiveTransactionCommission;

    /**
     * The range of allowed finalization commissions.
     */
    private final Range finalizationCommissionRange;

    /**
     * The range of allowed baker commissions.
     */
    private final Range bakingCommissionRange;

    /**
     * The range of allowed transaction commissions.
     */
    private final Range transactionCommissionRange;

    /**
     * Minimum equity capital required for a new baker.
     */
    private final CCDAmount minimumEquityCapital;

    /**
     * A bound on the relative share of the total staked capital that a baker can have as its stake.
     * This is required to be greater than 0.
     */
    private final double capitalBound;

    /**
     * The maximum leverage that a baker can have as a ratio of total stake
     * to equity capital.
     */
    private final Fraction leverageBound;


    @JsonCreator
    PoolParameters(
            @JsonProperty("passiveTransactionCommission") double passiveTransactionCommission,
            @JsonProperty("bakingCommissionRange") Range bakingCommissionRange,
            @JsonProperty("finalizationCommissionRange") Range finalizationCommissionRange,
            @JsonProperty("transactionCommissionRange") Range transactionCommissionRange,
            @JsonProperty("minimumEquityCapital") CCDAmount minimumEquityCapital,
            @JsonProperty("passiveBakingCommission") double passiveBakingCommission,
            @JsonProperty("leverageBound") Fraction leverageBound,
            @JsonProperty("passiveFinalizationCommission") double passiveFinalizationCommission,
            @JsonProperty("capitalBound") double capitalBound) {
        this.passiveFinalizationCommission = passiveFinalizationCommission;
        this.passiveBakingCommission = passiveBakingCommission;
        this.passiveTransactionCommission = passiveTransactionCommission;
        this.finalizationCommissionRange = finalizationCommissionRange;
        this.bakingCommissionRange = bakingCommissionRange;
        this.transactionCommissionRange = transactionCommissionRange;
        this.minimumEquityCapital = minimumEquityCapital;
        this.capitalBound = capitalBound;
        this.leverageBound = leverageBound;
    }
}
