package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.PoolParametersCpv1;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.Range;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class PoolParametersCPV1UpdatePayload implements UpdatePayload {

    /**
     * Fraction of finalization rewards charged by the passive delegation.
     */
    private Fraction passiveFinalizationCommission;
    /**
     * Fraction of baking rewards charged by the passive delegation.
     */
    private Fraction passiveBakingCommission;
    /**
     * Fraction of transaction rewards charged by the L-pool
     */
    private Fraction passiveTransactionCommission;
    /**
     * The range of allowed finalization commissions.
     */
    private Range finalizationRange;
    /**
     * The range of allowed baker commissions.
     */
    private Range bakingRange;
    /**
     * The range of allowed transaction commissions.
     */
    private Range transactionRange;
    /**
     * Maximum fraction of the total staked capital that a new baker can have.
     */
    private Fraction capitalBound;
    /**
     * The maximum leverage that a baker can have as a ratio of total stake to equity capital.
     */
    private Fraction leverageBound;


    /**
     * Parses {@link PoolParametersCpv1} to {@link PoolParametersCPV1UpdatePayload}.
     * @param poolParametersCpv1 {@link PoolParametersCpv1} returned by the GRPC V2 API.
     * @return parsed {@link PoolParametersCPV1UpdatePayload}
     */
    public static PoolParametersCPV1UpdatePayload parse(PoolParametersCpv1 poolParametersCpv1) {
        return PoolParametersCPV1UpdatePayload.builder()
                .passiveFinalizationCommission(Fraction.from(poolParametersCpv1.getPassiveFinalizationCommission()))
                .passiveBakingCommission(Fraction.from(poolParametersCpv1.getPassiveBakingCommission()))
                .passiveTransactionCommission(Fraction.from(poolParametersCpv1.getPassiveTransactionCommission()))
                .finalizationRange(Range.from(poolParametersCpv1.getCommissionBounds().getFinalization()))
                .bakingRange(Range.from(poolParametersCpv1.getCommissionBounds().getBaking()))
                .transactionRange(Range.from(poolParametersCpv1.getCommissionBounds().getTransaction()))
                .capitalBound(Fraction.from(poolParametersCpv1.getCapitalBound().getValue()))
                .leverageBound(Fraction.from(poolParametersCpv1.getLeverageBound().getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.POOL_PARAMETERS_CPV_1_UPDATE;
    }
}
