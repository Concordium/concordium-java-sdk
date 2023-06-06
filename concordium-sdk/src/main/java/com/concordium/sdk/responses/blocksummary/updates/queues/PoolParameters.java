package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.PoolParametersCpv1;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.Range;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdatePayload;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdateType;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * Pool parameters for protocol versions > 3
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public final class PoolParameters implements UpdatePayload {

    /**
     * The range of allowed finalization commissions.
     */
    private final PartsPerHundredThousand passiveFinalizationCommission;

    /**
     * The range of allowed baker commissions.
     */
    private final PartsPerHundredThousand passiveBakingCommission;

    /**
     * Commission rates charged for passive delegation.
     */
    private final PartsPerHundredThousand passiveTransactionCommission;

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
     * Maximum fraction of the total staked capital that a new baker can have.
     */
    private final PartsPerHundredThousand capitalBound;

    /**
     * The maximum leverage that a baker can have as a ratio of total stake
     * to equity capital.
     */
    private final Fraction leverageBound;


    @JsonCreator
    PoolParameters(
            @JsonProperty("passiveTransactionCommission") BigInteger passiveTransactionCommission,
            @JsonProperty("bakingCommissionRange") Range bakingCommissionRange,
            @JsonProperty("finalizationCommissionRange") Range finalizationCommissionRange,
            @JsonProperty("transactionCommissionRange") Range transactionCommissionRange,
            @JsonProperty("minimumEquityCapital") CCDAmount minimumEquityCapital,
            @JsonProperty("passiveBakingCommission") BigInteger passiveBakingCommission,
            @JsonProperty("leverageBound") Fraction leverageBound,
            @JsonProperty("passiveFinalizationCommission") BigInteger passiveFinalizationCommission,
            @JsonProperty("capitalBound") BigInteger capitalBound) {
        this.passiveFinalizationCommission = PartsPerHundredThousand.from(passiveFinalizationCommission);
        this.passiveBakingCommission = PartsPerHundredThousand.from(passiveBakingCommission);
        this.passiveTransactionCommission = PartsPerHundredThousand.from(passiveTransactionCommission);
        this.finalizationCommissionRange = finalizationCommissionRange;
        this.bakingCommissionRange = bakingCommissionRange;
        this.transactionCommissionRange = transactionCommissionRange;
        this.minimumEquityCapital = minimumEquityCapital;
        this.capitalBound = PartsPerHundredThousand.from(capitalBound);
        this.leverageBound = leverageBound;
    }

    /**
     * Parses {@link PoolParametersCpv1} to {@link PoolParameters}.
     * @param poolParametersCpv1 {@link PoolParametersCpv1} returned by the GRPC V2 API.
     * @return parsed {@link PoolParameters}.
     */
    public static PoolParameters parse(PoolParametersCpv1 poolParametersCpv1) {
        return PoolParameters.builder()
                .passiveFinalizationCommission(PartsPerHundredThousand.parse(poolParametersCpv1.getPassiveFinalizationCommission()))
                .passiveBakingCommission(PartsPerHundredThousand.parse(poolParametersCpv1.getPassiveBakingCommission()))
                .passiveTransactionCommission(PartsPerHundredThousand.parse(poolParametersCpv1.getPassiveTransactionCommission()))
                .finalizationCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getFinalization()))
                .bakingCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getBaking()))
                .transactionCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getTransaction()))
                .minimumEquityCapital(CCDAmount.fromMicro(poolParametersCpv1.getMinimumEquityCapital().getValue()))
                .capitalBound(PartsPerHundredThousand.parse(poolParametersCpv1.getCapitalBound().getValue()))
                .leverageBound(Fraction.parse(poolParametersCpv1.getLeverageBound().getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.POOL_PARAMETERS_CPV_1_UPDATE;
    }
}
